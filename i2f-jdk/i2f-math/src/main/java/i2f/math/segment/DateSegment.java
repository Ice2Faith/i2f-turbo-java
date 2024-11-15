package i2f.math.segment;


import java.util.Date;

public class DateSegment extends Segment<Date, Long> {
    public DateSegment() {
    }

    public DateSegment(Date begin, Date end) {
        super(begin, end);
    }

    @Override
    public Long distance() {
        return Math.abs(end.getTime() - begin.getTime());
    }

    @Override
    public Segment<Date, Long> instance(Date min, Date max) {
        return new DateSegment(min, max);
    }

    @Override
    public Long addDistance(Long d1, Long d2) {
        return d1 + d2;
    }

    @Override
    public int compareDistance(Long d1, Long d2) {
        if (d1 == d2) {
            return 0;
        }
        if (d1 == null) {
            return -1;
        }
        if (d2 == null) {
            return 1;
        }
        return d1.compareTo(d2);
    }
}