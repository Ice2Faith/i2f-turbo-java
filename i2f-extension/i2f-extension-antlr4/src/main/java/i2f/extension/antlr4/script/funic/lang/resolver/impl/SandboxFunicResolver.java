package i2f.extension.antlr4.script.funic.lang.resolver.impl;

import i2f.extension.antlr4.script.funic.lang.exception.throwable.FunicRejectException;
import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import i2f.invokable.method.IMethod;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2026/4/23 20:00
 * @desc
 */
public class SandboxFunicResolver extends DefaultFunicResolver {

    protected Predicate<String> multilineFeatureFilter;
    protected Predicate<String> renderPlaceholderExpressFilter;
    protected Predicate<IMethod> execMethodFilter;
    protected Predicate<Class<?>> newInstanceFilter;
    protected Predicate<String> findClassFilter;

    @Override
    public Object applyMultilineFeature(Object ret, String feature, DefaultFunicVisitor visitor) {
        if (multilineFeatureFilter == null || multilineFeatureFilter.test(feature)) {
            return super.applyMultilineFeature(ret, feature, visitor);
        }
        throw new FunicRejectException("Multiline feature [" + feature + "] has been reject!");
    }

    @Override
    public Object getRenderPlaceHolderValue(String express, DefaultFunicVisitor visitor) {
        if (renderPlaceholderExpressFilter == null || renderPlaceholderExpressFilter.test(express)) {
            return super.getRenderPlaceHolderValue(express, visitor);
        }
        throw new FunicRejectException("Render placeholder express [" + express + "] has been reject!");
    }

    @Override
    public void setFieldValue(Object target, String fieldName, Object value, DefaultFunicVisitor visitor) {
        super.setFieldValue(target, fieldName, value, visitor);
    }

    @Override
    public void setSquareFieldValue(Object target, Object squareKey, Object value, DefaultFunicVisitor visitor) {
        super.setSquareFieldValue(target, squareKey, value, visitor);
    }

    @Override
    public Object setStaticField(Class<?> type, String fieldName, Object value, DefaultFunicVisitor visitor) {
        return super.setStaticField(type, fieldName, value, visitor);
    }

    @Override
    public Object doExecMethod(Object target, IMethod method, List<Object> args, DefaultFunicVisitor visitor) throws Exception {
        if (execMethodFilter == null || execMethodFilter.test(method)) {
            return super.doExecMethod(target, method, args, visitor);
        }
        throw new FunicRejectException("Execute method [" + method + "] has been reject!");
    }

    @Override
    public Object doNewInstance(Class<?> clazz, List<Object> args, DefaultFunicVisitor visitor) throws Exception {
        if (newInstanceFilter == null || newInstanceFilter.test(clazz)) {
            return super.doNewInstance(clazz, args, visitor);
        }
        throw new FunicRejectException("New instance [" + clazz + "] has been reject!");
    }

    @Override
    public Object newArray(Class<?> elementType, int count, DefaultFunicVisitor visitor) {
        return super.newArray(elementType, count, visitor);
    }

    @Override
    public Class<?> findClass(String className, DefaultFunicVisitor visitor) {
        if (findClassFilter == null || findClassFilter.test(className)) {
            return super.findClass(className, visitor);
        }
        throw new FunicRejectException("Find class [" + className + "] has been reject!");
    }

}
