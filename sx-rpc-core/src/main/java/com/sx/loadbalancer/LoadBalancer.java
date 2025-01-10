package com.sx.loadbalancer;

import com.sx.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * LoadBalancer
 * 负载均衡接口
 *
 * @author SQ
 */
public interface LoadBalancer {

    /**
     * 选择服务地址
     *
     * @param requestParams 请求参数
     * @param serviceMetaInfoList 服务地址列表
     * @return
     */
    ServiceMetaInfo selectServiceAddress(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
