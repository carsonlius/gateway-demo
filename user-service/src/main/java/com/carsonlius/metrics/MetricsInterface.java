package com.carsonlius.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/11/23 9:35
 * @company
 * @description
 */
public interface MetricsInterface {
    /**
     * DEMO 访问次数 Metrics
     */
    Counter METRICS_DEMO_COUNT = Counter
            .builder("user.list.visit.count") // 指标的名字
            .description("用户列表接口 访问次数") // 指标的描述
            .baseUnit("次") // 指标的单位
            .tag("interface", "user.list") // 自定义标签
            .register(Metrics.globalRegistry);
}
