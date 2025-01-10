package com.sx.bootstrap;

import com.sx.RpcApplication;

/**
 * ConsumerBootstrap
 *
 * @author SQ
 */
public class ConsumerBootstrap {
    /**
     * 初始化
     */
    public static void init() {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
    }
}
