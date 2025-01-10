package com.sx.loadbalancer;

import com.sx.spi.SpiLoader;

/**
 * LoadBalancerFactory
 *
 * @author SQ
 */
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    // 默认使用轮询算法
    private static final LoadBalancer loadBalancer = new RoundRobinLoadBalancer();

    // 获取负载均衡实例
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
