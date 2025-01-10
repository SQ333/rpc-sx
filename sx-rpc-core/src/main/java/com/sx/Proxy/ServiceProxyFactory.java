package com.sx.Proxy;

import com.sx.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * ServiceProxyFactory
 *
 * @author SQ
 */
public class ServiceProxyFactory {
    /**
     * 获取代理对象
     *
     * @param serviceClass 服务类
     * @param <T>   泛型
     * @return 代理对象
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass},
                new ServiceProxy());
    }

    /**
     * 获取 Mock 代理对象
     *
     * @param serviceClass 服务类
     * @param <T>   泛型
     * @return Mock 代理对象
     */
    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass},
                new MockServiceProxy());
    }
}
