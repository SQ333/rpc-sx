package com.sx.config;

import com.sx.registry.RegistryKeys;
import lombok.Data;

/**
 * RegistryConfig
 *
 * @author SQ
 */
@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = RegistryKeys.ETCD;

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间
     */
    private Long timeout = 10_000L;
}
