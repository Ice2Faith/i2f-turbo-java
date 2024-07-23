package org.slf4j.impl;

import org.slf4j.Marker;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/7/23 19:33
 * @desc
 */
public class SimpleMarker implements Marker {
    private static final long serialVersionUID = -2849567615646933777L;
    private final String name;
    private CopyOnWriteArrayList<Marker> referenceList = new CopyOnWriteArrayList<>();
    private static String OPEN = "[ ";
    private static String CLOSE = " ]";
    private static String SEP = ", ";

    public SimpleMarker(String name) {
        if (name == null) {
            throw new IllegalArgumentException("A marker name cannot be null");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void add(Marker reference) {
        if (reference == null) {
            throw new IllegalArgumentException("A null value cannot be added to a Marker as reference.");
        }
        if (this.contains(reference)) {
            return;
        }
        if (reference.contains(this)) {
            return;
        }
        this.referenceList.add(reference);

    }

    @Override
    public boolean hasReferences() {
        return !this.referenceList.isEmpty();
    }

    @Override
    public boolean hasChildren() {
        return this.hasReferences();
    }

    @Override
    public Iterator<Marker> iterator() {
        return this.referenceList.iterator();
    }

    @Override
    public boolean remove(Marker referenceToRemove) {
        return this.referenceList.remove(referenceToRemove);
    }

    @Override
    public boolean contains(Marker other) {
        if (other == null) {
            throw new IllegalArgumentException("Other cannot be null");
        }
        if (this.equals(other)) {
            return true;
        }
        if (this.hasReferences()) {
            for (Marker item : this.referenceList) {
                if (item.contains(other)) {
                    return true;
                }
            }
        }

        return false;

    }

    @Override
    public boolean contains(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Other cannot be null");
        }
        if (this.name.equals(name)) {
            return true;
        }
        if (this.hasReferences()) {
            for (Marker item : this.referenceList) {
                if (item.contains(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof Marker)) {
            return false;
        } else {
            Marker other = (Marker) obj;
            return this.name.equals(other.getName());
        }
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        if (!this.hasReferences()) {
            return this.getName();
        }

        StringBuilder sb = new StringBuilder(this.getName());
        sb.append(' ').append(OPEN);

        boolean isFirst = true;
        for (Marker item : this.referenceList) {
            if (!isFirst) {
                sb.append(SEP);
            }
            sb.append(item.getName());
            isFirst = false;
        }

        sb.append(CLOSE);
        return sb.toString();

    }
}

