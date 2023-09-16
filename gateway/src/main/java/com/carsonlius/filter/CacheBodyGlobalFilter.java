package com.carsonlius.filter;


/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/9/12 18:06
 * @company https://blog.csdn.net/sowei2009/article/details/126104934
 * @description CacheBodyGlobalFilter这个全局过滤器的目的就是把原有的request请求中的body内容读出来，并且使用ServerHttpRequestDecorator这个请求装饰器对request进行包装，重写getBody方法，并把包装后的请求放到过滤器链中传递下去。这样后面的过滤器中再使用exchange.getRequest().getBody()来获取body时，实际上就是调用的重载后的getBody方法，获取的最先已经缓存了的body数据。这样就能够实现body的多次读取了。
 *
 */

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;


@Slf4j
@Component
public class CacheBodyGlobalFilter implements Ordered, GlobalFilter {

    private List<HttpMessageReader<?>> messageReaders;
    private ParameterizedTypeReference<MultiValueMap<String, Part>> MULTI_PART = new ParameterizedTypeReference<MultiValueMap<String, Part>>(){};

    public CacheBodyGlobalFilter(ServerCodecConfigurer configurer) {
        this.messageReaders = configurer.getReaders();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        GatewayContext gatewayContext = new GatewayContext();
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
        ServerHttpRequest request = exchange.getRequest();
        MediaType contentType = request.getHeaders().getContentType();
        // 目前只缓存 json 和 multipart 表单两种请求类型
        if (Objects.nonNull(contentType) && Objects.nonNull(request.getMethod()) && request.getMethod().equals(HttpMethod.POST)) {
            if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
                return readMultiPartFormData(exchange, chain, gatewayContext);
            } else if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                return readBody(exchange, chain, gatewayContext);
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private Mono<Void> readMultiPartFormData(ServerWebExchange exchange, GatewayFilterChain chain, GatewayContext gatewayContext) {
        // 当body为空时，只会执行这一个拦截器， 原因是fileMap中的代码没有执行，所以需要在body为空时构建一个空的缓存
        DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
        DefaultDataBuffer defaultDataBuffer = defaultDataBufferFactory.allocateBuffer(0);
        Mono<DataBuffer> mono = Flux.from(exchange.getRequest().getBody().defaultIfEmpty(defaultDataBuffer))
                .collectList().filter(list -> {
                    log.info("请求体缓存过滤器：body为空");
                    return true;
                }).map(list -> list.get(0).factory().join(list)).doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
        return mono.flatMap(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            ServerHttpRequestDecorator mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return Flux.defer(() -> {
                        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                        DataBufferUtils.retain(buffer);
                        return Mono.just(buffer);
                    });
                }
            };
            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            return ServerRequest.create(mutatedExchange, messageReaders).bodyToMono(MULTI_PART)
                    .doOnNext(multiPartMap -> {
                        gatewayContext.setMultiPartParams(multiPartMap);
                    }).then(chain.filter(mutatedExchange));
        });
    }

    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, GatewayContext gatewayContext) {
        // 当body为空（请求体中"{}"都不存在）时，只会执行这一个拦截器， 原因是fileMap中的代码没有执行，所以需要在body为空时构建一个空的缓存
        DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
        DefaultDataBuffer defaultDataBuffer = defaultDataBufferFactory.allocateBuffer(0);
        Mono<DataBuffer> mono = Flux.from(exchange.getRequest().getBody().defaultIfEmpty(defaultDataBuffer))
                .collectList().filter(list -> {
                    log.info("请求体缓存过滤器：body为空");
                    return true;
                }).map(list -> list.get(0).factory().join(list)).doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
        return mono.flatMap(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            ServerHttpRequestDecorator mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return Flux.defer(() -> {
                        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                        DataBufferUtils.retain(buffer);
                        return Mono.just(buffer);
                    });
                }
            };
            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            return ServerRequest.create(mutatedExchange, messageReaders)
                    .bodyToMono(String.class)
                    .doOnNext(objectValue -> {
                        gatewayContext.setJsonBody(objectValue);
                    }).then(chain.filter(mutatedExchange));
        });
    }
}
