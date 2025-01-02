package i2f.image.filter.std.impl;

import i2f.image.filter.std.PixelFilter;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:10
 * @desc
 */
public class MergePixelFilter implements PixelFilter {
    protected final CopyOnWriteArrayList<PixelFilter> filters = new CopyOnWriteArrayList<>();

    public MergePixelFilter(PixelFilter... filters) {
        this.filters.addAll(Arrays.asList(filters));
    }

    public MergePixelFilter add(PixelFilter filter) {
        this.filters.add(filter);
        return this;
    }

    @Override
    public int pixel(int color) {
        int ret = color;
        for (PixelFilter item : filters) {
            if (item == null) {
                continue;
            }
            ret = item.pixel(ret);
        }
        return ret;
    }
}
