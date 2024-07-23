package org.slf4j.impl;

import i2f.extension.slf4j.log.Slf4jLogLoggerFactoryAdapter;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * @author Ice2Faith
 * @date 2024/7/23 18:54
 * @desc
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {
    public static final String BINDER_NAME="log";

    public static final String REQUESTED_API_VERSION = "1.7.30";
    public static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    public static StaticLoggerBinder getSingleton(){
        return SINGLETON;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return Slf4jLogLoggerFactoryAdapter.INSTANCE;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return BINDER_NAME;
    }
}
