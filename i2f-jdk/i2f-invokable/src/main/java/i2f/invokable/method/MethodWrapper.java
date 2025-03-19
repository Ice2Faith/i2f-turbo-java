package i2f.invokable.method;

/**
 * @author Ice2Faith
 * @date 2025/3/19 22:05
 * @desc
 */
public class MethodWrapper implements IMethod {
    protected IMethod method;

    public MethodWrapper(IMethod method) {
        this.method = method;
    }

    @Override
    public void setAccessible(boolean accessible) {
        method.setAccessible(accessible);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Class<?> getDeclaringClass() {
        return method.getDeclaringClass();
    }

    @Override
    public int getModifiers() {
        return method.getModifiers();
    }

    @Override
    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public int getParameterCount() {
        return method.getParameterCount();
    }

    @Override
    public Object invoke(Object ivkObj, Object... args) throws Throwable {
        return method.invoke(ivkObj, args);
    }
}
