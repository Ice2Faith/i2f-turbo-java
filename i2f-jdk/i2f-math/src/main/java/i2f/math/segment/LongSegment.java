package i2f.math.segment;


public class LongSegment extends Segment<Long, Long> {
    public LongSegment() {
    }

    public LongSegment(Long begin, Long end) {
        super(begin, end);
    }

    @Override
    public Long distance() {
        return Math.abs(end - begin);
    }

    @Override
    public Segment<Long, Long> instance(Long min, Long max) {
        return new LongSegment(min, max);
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
