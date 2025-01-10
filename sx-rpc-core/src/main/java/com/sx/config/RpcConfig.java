package com.sx.config;

import com.sx.fault.retry.RetryStrategyKeys;
import com.sx.fault.tolerant.TolerantStrategyKeys;
import com.sx.loadbalancer.LoadBalancerKeys;
import com.sx.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC配置
 */
@Data
public class RpcConfig {
    /**
     * 服务名称
     */
    private String name = "sx-rpc";

    /**
     * 服务版本
     */
    private String version = "1.0";

    /**
     * 服务主机
     */
    private String serverHost = "localhost";

    /**
     * 服务端口
     */
    private Integer serverPort = 2828;

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 负载均衡
     */
    private String loadBalancer = LoadBalancerKeys.CONSISTENT_HASH;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.FIXED_INTERVAL;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();
}
