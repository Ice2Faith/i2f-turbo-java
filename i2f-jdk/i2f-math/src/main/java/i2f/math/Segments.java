package i2f.math;

import i2f.math.segment.DateSegment;
import i2f.math.segment.DoubleSegment;
import i2f.math.segment.IntegerSegment;
import i2f.math.segment.LongSegment;

import java.util.Date;

public class Segments {
    public static DateSegment of(Date begin, Date end) {
        return new DateSegment(begin, end);
    }

    public static IntegerSegment of(Integer begin, Integer end) {
        return new IntegerSegment(begin, end);
    }

    public static LongSegment of(Long begin, Long end) {
        return new LongSegment(begin, end);
    }

    public static DoubleSegment of(Double begin, Double end) {
        return new DoubleSegment(begin, end);
    }
}
