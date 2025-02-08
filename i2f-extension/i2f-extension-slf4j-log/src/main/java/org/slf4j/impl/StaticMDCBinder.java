package org.slf4j.impl;

import org.slf4j.spi.MDCAdapter;

/**
 * @author Ice2Faith
 * @date 2024/7/23 19:00
 * @desc
 */
public class StaticMDCBinder {
    public static final String BINDER_NAME = "log";

    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    public static StaticMDCBinder getSingleton() {
        return SINGLETON;
    }

    public MDCAdapter getMDCA() {
        return new SimpleMdcAdapter();
    }

    public String getMDCAdapterClassStr() {
        return BINDER_NAME;
    }
}
