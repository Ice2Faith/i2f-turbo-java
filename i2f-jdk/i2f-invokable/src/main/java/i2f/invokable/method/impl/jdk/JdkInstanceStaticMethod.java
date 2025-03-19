package i2f.invokable.method.impl.jdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2025/3/19 21:30
 * @desc
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JdkInstanceStaticMethod extends JdkMethod {
    public Object target;

    public JdkInstanceStaticMethod(Object target, Method method) {
        super(method);
        this.target = target;
    }

    @Override
    public int getModifiers() {
        return super.getModifiers() | Modifier.STATIC;
    }

    @Override
    public Object invoke(Object obj, Object[] args) throws Throwable {
        return super.invoke(obj != null ? obj : target, args);
    }
}
