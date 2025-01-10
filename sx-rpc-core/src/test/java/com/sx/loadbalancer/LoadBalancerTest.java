package com.sx.loadbalancer;

import com.sx.model.ServiceMetaInfo;
import dev.failsafe.internal.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LoadBalancerTest {

    final LoadBalancer loadBalancer = new ConsistentHashLoadBalancer();

    @Test
    public void select() {
        // 请求参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", "apple");

        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("myService");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceHost("localhost");
        serviceMetaInfo1.setServicePort(1234);
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("myService");
        serviceMetaInfo2.setServiceVersion("1.0");
        serviceMetaInfo2.setServiceHost("com.sx");
        serviceMetaInfo2.setServicePort(80);
        List<ServiceMetaInfo> serviceMetaInfoList = Arrays.asList(serviceMetaInfo1, serviceMetaInfo2);


        ServiceMetaInfo serviceMetaInfo = loadBalancer.selectServiceAddress(requestParams, serviceMetaInfoList);
        System.out.printf("Selected service: %s\n", serviceMetaInfo);
        Assertions.assertNotNull(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.selectServiceAddress(requestParams, serviceMetaInfoList);
        System.out.printf("Selected service: %s\n", serviceMetaInfo);
        Assertions.assertNotNull(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.selectServiceAddress(requestParams, serviceMetaInfoList);
        System.out.printf("Selected service: %s\n", serviceMetaInfo);
        Assertions.assertNotNull(serviceMetaInfo);
    }
}