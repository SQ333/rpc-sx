package com.sx.fault.tolerant;

/**
 * TolerantStrategyKeys
 *
 * @author SQ
 */
public interface TolerantStrategyKeys {
    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";
    /**
     * 故障恢复
     */
    String FAIL_BACK = "failBack";
    /**
     * 故障转移
     */
    String FAIL_OVER = "failOver";
    /**
     * 静默失败
     */
    String FAIL_SAFE = "failSafe";
}
