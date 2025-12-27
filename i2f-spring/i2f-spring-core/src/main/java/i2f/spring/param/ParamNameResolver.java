package i2f.spring.param;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ParamNameResolver {
    public static ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    public static String[] getParamNames(Method method) {
        return discoverer.getParameterNames(method);
    }

    public static String[] getParamNames(Constructor constructor) {
        return discoverer.getParameterNames(constructor);
    }
}
