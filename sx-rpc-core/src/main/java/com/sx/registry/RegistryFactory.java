package com.sx.registry;

import com.sx.spi.SpiLoader;

/**
 * RegistryFactory
 *
 * @author SQ
 */
public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    private static final Registry registry = new EtcdRegistry();

    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }
}
