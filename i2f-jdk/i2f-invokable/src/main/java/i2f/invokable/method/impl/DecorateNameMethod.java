package i2f.invokable.method.impl;

import i2f.invokable.method.IMethod;
import i2f.invokable.method.MethodWrapper;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/3/19 22:05
 * @desc
 */
public class DecorateNameMethod extends MethodWrapper {
    protected Function<String, String> decorator;

    public DecorateNameMethod(IMethod method, Function<String, String> decorator) {
        super(method);
    }

    @Override
    public String getName() {
        String name = super.getName();
        return decorator == null ? name : decorator.apply(name);
    }
}
