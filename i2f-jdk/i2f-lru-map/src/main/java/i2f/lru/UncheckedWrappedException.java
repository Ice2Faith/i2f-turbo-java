package i2f.lru;

/**
 * @author Ice2Faith
 * @date 2026/4/13 11:56
 * @desc
 */
public class UncheckedWrappedException extends RuntimeException {
    protected final Throwable target;

    public UncheckedWrappedException(Throwable target) {
        this.target = target;
    }

    public UncheckedWrappedException(String message, Throwable target) {
        super(message, target);
        this.target = target;
    }

    public Throwable getTarget() {
        return this.target;
    }
}
