package com.carsonlius.filter;

import com.carsonlius.utils.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/9/11 10:19
 * @company
 * @description
 */
@Component
public class RequestTimeGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestTimeGatewayFilterFactory.Config> {
    private static final String KEY = "withParams";
    private static final Log log = LogFactory.getLog(GatewayFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    public RequestTimeGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {

        return Arrays.asList(KEY);
//        return super.shortcutFieldOrder();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
                return chain.filter(exchange).then((Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                    StringBuilder logStr = new StringBuilder("耗时统计 " + exchange.getRequest().getURI().getPath());
                    logStr.append(":").append((System.currentTimeMillis() - startTime)).append("ms");
                    if (config.isWithParams()) {
                        logStr.append(" 入参: ").append(exchange.getRequest().getQueryParams()).append(" post入参:").append(JsonUtils.toJson(exchange.getRequest().getBody()));
                    }

                    log.info(logStr.toString());
                })));
            }
        };
    }

    public static class Config {
        private boolean withParams;

        public boolean isWithParams() {
            return withParams;
        }

        public void setWithParams(boolean withParams) {
            this.withParams = withParams;
        }
    }
}
