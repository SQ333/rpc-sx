package com.sx.loadbalancer;

import com.sx.model.ServiceMetaInfo;

import java.awt.image.RasterOp;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * RandomLoadBalancer
 * 随机负载均衡
 *
 * @author SQ
 */
public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();

    @Override
    public ServiceMetaInfo selectServiceAddress(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()) {
            return null;
        }

        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }

        return serviceMetaInfoList.get(random.nextInt() % size);
    }
}
