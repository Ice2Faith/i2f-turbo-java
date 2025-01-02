package i2f.image.filter.std.impl;

import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:10
 * @desc
 */
public class MergeRgbaFilter implements RgbaFilter {
    protected final CopyOnWriteArrayList<RgbaFilter> filters = new CopyOnWriteArrayList<>();

    public MergeRgbaFilter(RgbaFilter... filters) {
        this.filters.addAll(Arrays.asList(filters));
    }

    public MergeRgbaFilter add(RgbaFilter filter) {
        this.filters.add(filter);
        return this;
    }

    @Override
    public Rgba pixel(Rgba color) {
        Rgba ret = color;
        for (RgbaFilter item : filters) {
            if (item == null) {
                continue;
            }
            ret = item.pixel(ret);
        }
        return ret;
    }
}
