package i2f.invokable.method.impl.jdk;

import i2f.invokable.method.IMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2025/3/19 21:17
 * @desc
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JdkExecutable implements IMethod {
    protected Executable executable;

    public JdkExecutable(Executable executable) {
        this.executable = executable;
    }

    public Executable getExecutable() {
        return executable;
    }

    @Override
    public void setAccessible(boolean accessible) {
        executable.setAccessible(accessible);
    }

    @Override
    public String getName() {
        return executable.getName();
    }

    @Override
    public Class<?> getDeclaringClass() {
        return executable.getDeclaringClass();
    }

    @Override
    public int getModifiers() {
        return executable.getModifiers();
    }

    @Override
    public Class<?> getReturnType() {
        return executable.getDeclaringClass();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return executable.getParameterTypes();
    }

    @Override
    public int getParameterCount() {
        return executable.getParameterCount();
    }

    @Override
    public Object invoke(Object obj, Object[] args) throws Throwable {
        if (executable instanceof Constructor) {
            return ((Constructor) executable).newInstance(args);
        } else if (executable instanceof Method) {
            return ((Method) executable).invoke(obj, args);
        }
        throw new ReflectiveOperationException("un-supported executable invoke type: " + executable);
    }
}
