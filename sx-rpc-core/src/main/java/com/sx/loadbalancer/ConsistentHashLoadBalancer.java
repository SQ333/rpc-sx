package com.sx.loadbalancer;

import com.sx.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ConsistentHashLoadBalancer
 * 一致性哈希负载均衡
 *
 * @author SQ
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点数量
     */
    private static final int VIRTUAL_NODE_SIZE = 100;

    @Override
    public ServiceMetaInfo selectServiceAddress(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()) {
            return null;
        }

        // 构建虚拟节点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        // 获取请求参数的哈希值
        int hash = getHash(requestParams);

        // 获取大于等于该哈希值的第一个虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            // 如果没有大于等于该哈希值的虚拟节点，则返回环的第一个虚拟节点
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    private int getHash(Object key) {
        return key.hashCode();
    }
}
