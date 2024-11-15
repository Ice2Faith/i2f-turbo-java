package i2f.math.segment;


public class DoubleSegment extends Segment<Double, Double> {
    public DoubleSegment() {
    }

    public DoubleSegment(Double begin, Double end) {
        super(begin, end);
    }

    @Override
    public Double distance() {
        return Math.abs(end - begin);
    }

    @Override
    public Segment<Double, Double> instance(Double min, Double max) {
        return new DoubleSegment(min, max);
    }

    @Override
    public Double addDistance(Double d1, Double d2) {
        return d1 + d2;
    }

    @Override
    public int compareDistance(Double d1, Double d2) {
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
