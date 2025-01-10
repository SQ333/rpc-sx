package com.sx.example.consumer;

import com.sx.Proxy.ServiceProxyFactory;
import com.sx.bootstrap.ConsumerBootstrap;
import com.sx.config.RpcConfig;
import com.sx.example.common.model.User;
import com.sx.example.common.service.UserService;
import com.sx.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * ConsumerExample
 *
 * @author SQ
 */
@Slf4j
public class ConsumerExample {

    public static void main(String[] args) {
        // 服务消费者初始化
        ConsumerBootstrap.init();

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("SQ");

        // 调用方法
        User result = userService.getUser(user);
        if (result != null) {
            log.info("Get user: {}", result.getName());
        } else {
            log.error("Failed to get user");
        }
    }
}
