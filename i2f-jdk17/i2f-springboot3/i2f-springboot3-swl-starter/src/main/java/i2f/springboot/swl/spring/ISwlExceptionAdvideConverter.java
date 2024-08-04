package i2f.springboot.swl.spring;

import i2f.swl.exception.SwlException;

/**
 * @author Ice2Faith
 * @date 2024/7/12 14:36
 * @desc
 */
public interface ISwlExceptionAdvideConverter {
    Object convert(SwlException exception);
}
