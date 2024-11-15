package i2f.math.segment;


public class IntegerSegment extends Segment<Integer, Integer> {
    public IntegerSegment() {
    }

    public IntegerSegment(Integer begin, Integer end) {
        super(begin, end);
    }

    @Override
    public Integer distance() {
        return Math.abs(end - begin);
    }

    @Override
    public Segment<Integer, Integer> instance(Integer min, Integer max) {
        return new IntegerSegment(min, max);
    }

    @Override
    public Integer addDistance(Integer d1, Integer d2) {
        return d1 + d2;
    }

    @Override
    public int compareDistance(Integer d1, Integer d2) {
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
