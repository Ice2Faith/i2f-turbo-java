package org.slf4j.impl;

import org.slf4j.IMarkerFactory;
import org.slf4j.Marker;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/7/23 19:31
 * @desc
 */
public class SimpleMarkerFactory implements IMarkerFactory {
    private final ConcurrentHashMap<String, Marker> markerMap = new ConcurrentHashMap<>();

    public SimpleMarkerFactory() {
    }

    public Marker getMarker(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Marker name cannot be null");
        }
        Marker marker = this.markerMap.computeIfAbsent(name,(nm)->new SimpleMarker(nm));
        return marker;

    }

    @Override
    public boolean exists(String name) {
        return name != null && this.markerMap.containsKey(name);
    }

    @Override
    public boolean detachMarker(String name) {
        if (name == null) {
            return false;
        }
        return this.markerMap.remove(name) != null;
    }

    @Override
    public Marker getDetachedMarker(String name) {
        return new SimpleMarker(name);
    }
}
