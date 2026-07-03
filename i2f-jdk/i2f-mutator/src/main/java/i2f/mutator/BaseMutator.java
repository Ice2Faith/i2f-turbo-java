package i2f.mutator;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:38
 * @desc
 */
public interface BaseMutator<T extends BaseMutator<T>> {
    default Mutator<T> toMutator() {
        return Mutator.of((T) this);
    }
}
