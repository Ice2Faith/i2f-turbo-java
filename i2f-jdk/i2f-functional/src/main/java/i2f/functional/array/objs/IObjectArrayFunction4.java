package i2f.functional.array.objs;

import i2f.functional.array.IObjectArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:02
 * @desc
 */
@FunctionalInterface
public interface IObjectArrayFunction4<T, V1, V2, V3, V4> extends IObjectArrayFunction {
    T[] apply(V1 v1, V2 v2, V3 v3, V4 v4);
    static<T, V1, V2, V3, V4> IObjectArrayFunction4<T, V1, V2, V3, V4> of(IObjectArrayFunction4<T, V1, V2, V3, V4> ret){
        return ret;
    }
}
