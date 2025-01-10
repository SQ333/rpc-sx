package com.sx.serializer;

import com.sx.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * SerializerFactory
 *
 * @author SQ
 */
public class SerializerFactory {

    /**
     * 获取序列化器
     *
     * @param key 序列化器 key
     * @return 序列化器
     */
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取序列化器
     *
     * @param key 序列化器 key
     * @return 序列化器
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
