package com.sx.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.sx.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpiLoader
 *
 * @author SQ
 */
@Slf4j
public class SpiLoader {
    /**
     * 存储已经加载的类 接口名 => (key => 实现类)
     */
    private static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象实例缓存 类路径 => 对象实例, 单例模式
     */
    private static Map<String, Object> instantCache = new ConcurrentHashMap<>();


    /**
     * 系统 SPI 目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用书 SPI 目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有类型spi
     */
    public static void loadAll() {
        log.info("load all spi");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    /**
     * 获取某个接口的实例
     *
     * @param tClass
     * @param key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);

        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader not load %s type", tClassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader %s not found type key=%s", tClassName, key));
        }

        // 获取要加载的实例
        Class<?> implClass = keyClassMap.get(key);
        // 从实例缓存中加载指定类型的实例
        String implClassName = implClass.getName();
        if (!instantCache.containsKey(implClassName)) {
            try {
                instantCache.put(implClassName, implClass.newInstance());
            } catch (Exception e) {
                String errorMsg = String.format("%s class instant failed", implClassName);
                throw new RuntimeException(errorMsg);
            }
        }

        return (T) instantCache.get(implClassName);
    }

    /**
     * 加载某个类型
     *
     * @param loadClass
     * @return
     */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("loading type {} by SPI", loadClass.getName());

        // 扫描路径，用户定义SPI优先级高于系统SPI
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());

            // 读取每个资源文件
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strArray = line.split("=");
                        if (strArray.length > 1) {
                            String key = strArray[0];
                            String className = strArray[1];
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("SPI resource load error", e);
                }
            }
        }
        loaderMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }

    public static void main(String[] args) {
        loadAll();
        System.out.println(loaderMap);
        Serializer serializer = getInstance(Serializer.class, "kryo");
        System.out.println(serializer);
    }
}
