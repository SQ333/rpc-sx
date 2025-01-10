package com.sx.fault.retry;

import com.sx.model.RpcRequest;
import com.sx.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * NoRetryStrategy
 *
 * @author SQ
 */
public class NoRetryStrategy implements RetryStrategy {
    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
