package com.sx.registry;

import com.sx.model.ServiceMetaInfo;

import java.util.List;

/**
 * RegistryServiceCache
 * 注册中心服务缓存
 *
 * @author SQ
 */
public class RegistryServiceCache {

    /**
     * 服务缓存
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     * @param serviceCache
     */
    void writeCache(List<ServiceMetaInfo> serviceCache) {
        this.serviceCache = serviceCache;
    }

    /**
     * 读缓存
     * @return
     */
    List<ServiceMetaInfo> readCache() {
        return serviceCache;
    }

    /**
     * 清空缓存
     */
    void clearCache() {
        serviceCache = null;
    }
}
