package i2f.extension.freemarker.stringify.impl;

import i2f.convert.obj.ObjectConvertor;
import i2f.extension.freemarker.stringify.Stringifier;

/**
 * @author Ice2Faith
 * @date 2025/4/2 8:52
 */
public class DefaultStringifier implements Stringifier {
    public static final DefaultStringifier INSTANCE = new DefaultStringifier();

    @Override
    public boolean support(Object obj) {
        return true;
    }

    @Override
    public String stringify(Object obj) {
        return ObjectConvertor.stringify(obj, "null");
    }
}
