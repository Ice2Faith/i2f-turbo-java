package i2f.functional.base.doubles;

import i2f.functional.base.IDoubleFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:27
 * @desc
 */
@FunctionalInterface
public interface IDoubleFunction3<V1, V2, V3> extends IDoubleFunction {
    double apply(V1 v1, V2 v2, V3 v3);
    static<V1, V2, V3> IDoubleFunction3<V1, V2, V3> of(IDoubleFunction3<V1, V2, V3> ret){
        return ret;
    }
}
