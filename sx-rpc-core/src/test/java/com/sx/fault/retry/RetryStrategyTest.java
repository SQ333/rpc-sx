package com.sx.fault.retry;

import com.sx.model.RpcResponse;
import org.junit.jupiter.api.Test;

class RetryStrategyTest {

    RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("test retry");
                throw new RuntimeException("test");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("retry failed");
            e.printStackTrace();
        }
    }
}