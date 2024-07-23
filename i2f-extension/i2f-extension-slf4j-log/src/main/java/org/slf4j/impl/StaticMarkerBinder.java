package org.slf4j.impl;

import org.slf4j.IMarkerFactory;
import org.slf4j.spi.MarkerFactoryBinder;

/**
 * @author Ice2Faith
 * @date 2024/7/23 18:57
 * @desc
 */
public class StaticMarkerBinder  implements MarkerFactoryBinder {
    public static final String BINDER_NAME="log";

    public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();
    public static final IMarkerFactory markerFactory = new SimpleMarkerFactory();

    public static StaticMarkerBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return markerFactory;
    }

    @Override
    public String getMarkerFactoryClassStr() {
        return BINDER_NAME;
    }
}
