package i2f.functional.base.longs;

import i2f.functional.base.ILongFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:30
 * @desc
 */
@FunctionalInterface
public interface ILongFunction3<V1, V2, V3> extends ILongFunction {
    long apply(V1 v1, V2 v2, V3 v3);
    static<V1, V2, V3> ILongFunction3<V1, V2, V3> of(ILongFunction3<V1, V2, V3> ret){
        return ret;
    }
}
