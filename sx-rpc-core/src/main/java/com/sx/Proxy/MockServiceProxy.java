package com.sx.Proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * MockServiceProxy
 *
 * @author SQ
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    /**
     * 代理方法
     *
     * @param proxy  代理对象
     * @param method 方法
     * @param args   参数
     * @return 返回值
     * @throws Throwable 异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * 获取默认对象
     *
     * @param type 类型
     * @return 默认对象
     */
    private Object getDefaultObject(Class<?> type) {
        // 基本类型
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return false;
            } else if (type == short.class) {
                return (short) 0;
            } else if (type == int.class) {
                return 0;
            } else if (type == long.class) {
                return 0L;
            } else if (type == float.class) {
                return 0.0F;
            } else if (type == double.class) {
                return 0.0D;
            } else if (type == char.class) {
                return '\u0000';
            } else {
                return null;
            }
        }
        // 对象类型
        return null;
    }
}
