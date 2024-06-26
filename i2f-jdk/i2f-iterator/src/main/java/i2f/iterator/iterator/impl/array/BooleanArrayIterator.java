package i2f.iterator.iterator.impl.array;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 8:45
 * @desc
 */
public class BooleanArrayIterator implements Iterator<Boolean> {
    private boolean[] arr;
    private int startIndex = 0;
    private int endIndex;

    public BooleanArrayIterator(boolean[] arr) {
        this.arr = arr;
        this.startIndex = 0;
        this.endIndex = arr.length;
    }

    public BooleanArrayIterator(boolean[] arr, int startIndex) {
        this.arr = arr;
        this.startIndex = startIndex;
        this.endIndex = arr.length;
    }

    public BooleanArrayIterator(boolean[] arr, int startIndex, int endIndex) {
        this.arr = arr;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public boolean hasNext() {
        return this.startIndex < this.endIndex;
    }

    @Override
    public Boolean next() {
        boolean ret = arr[startIndex];
        startIndex++;
        return ret;
    }
}
