package com.sx.protocol;

import lombok.Getter;

/**
 * ProtocolMessageStatusEnum
 *
 * @author SQ
 */
@Getter
public enum ProtocolMessageStatusEnum {
    OK("OK", 20),
    BAD_REQUEST("BAD_REQUEST", 40),
    BAD_RESPONSE("BAD_RESPONSE", 50);

    private final String text;

    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value) {
        for (ProtocolMessageStatusEnum statusEnum : ProtocolMessageStatusEnum.values()) {
            if (statusEnum.value == value) {
                return statusEnum;
            }
        }
        return null;
    }
}
