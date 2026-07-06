package i2f.extension.antlr4.script.funic.lang.resolver.impl;

import i2f.extension.antlr4.script.funic.lang.exception.throwable.FunicRejectException;
import i2f.extension.antlr4.script.funic.lang.functions.FunicFunctionHelper;
import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import i2f.invokable.method.IMethod;
import i2f.mixin.MixinProxyFactory;
import i2f.mixin.impl.*;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2026/4/23 20:00
 * @desc
 */
@Data
@NoArgsConstructor
public class SandboxFunicResolver extends DefaultFunicResolver implements BaseMutator<SandboxFunicResolver> {

    protected volatile Predicate<String> multilineFeatureFilter = SandboxFunicResolver::isSafeMultilineFeature;
    protected volatile Predicate<String> renderPlaceholderExpressFilter;
    protected volatile Predicate<IMethod> execMethodFilter = SandboxFunicResolver::isSafeExecMethod;
    protected volatile Predicate<Class<?>> newInstanceFilter = SandboxFunicResolver::isSafeClass;
    protected volatile Predicate<Class<?>> newArrayFilter = SandboxFunicResolver::isSafeClass;
    protected volatile Predicate<String> findClassFilter = SandboxFunicResolver::isSafeClassName;
    protected final AtomicBoolean useStaticGlobalMethods = new AtomicBoolean(false);
    protected final AtomicBoolean useBuiltInGlobalMethods = new AtomicBoolean(false);
    protected final AtomicBoolean useVisitorRegistryGlobalMethods = new AtomicBoolean(false);
    protected final ConcurrentHashMap<String, CopyOnWriteArrayList<IMethod>> globalMethods = new ConcurrentHashMap<>();

    public static SandboxFunicResolver createDefault() {
        SandboxFunicResolver resolver = new SandboxFunicResolver();
        resolver.resetGlobalMethods();
        return resolver;
    }

    public static <T> boolean rejectAll(T obj) {
        return false;
    }

    public static boolean isSafeMultilineFeature(String feature) {
        return Arrays.asList("render", "align", "trim").contains(feature);
    }

    public static boolean isSafeClassName(String className) {
        if (DangerousConsts.DEFAULT_DANGEROUS_CLASS_NAMES.contains(className)) {
            return false;
        }
        for (String pkg : DangerousConsts.DEFAULT_DANGEROUS_PACKAGES) {
            if (className.startsWith(pkg)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSafeClass(Class<?> clazz) {
        String className = clazz.getName();
        return isSafeClassName(className);
    }

    public static boolean isSafeExecMethod(IMethod method) {
        if (DangerousConsts.DEFAULT_GLOBAL_DANGEROUS_METHODS.contains(method.getName())) {
            return false;
        }
        Class<?> clazz = method.getDeclaringClass();
        if (!isSafeClass(clazz)) {
            return false;
        }
        String name = clazz.getName();
        Set<String> names = DangerousConsts.DEFAULT_CLASS_DANGEROUS_METHODS.get(name);
        if (names != null) {
            if (names.contains(method.getName())) {
                return false;
            }
        }
        return true;
    }

    public void resetGlobalMethods() {
        globalMethods.clear();
        registryDefaultMethods();
    }

    public void registryDefaultMethods() {
        registryMethods(System.out, e -> e.getName().toLowerCase().contains("print"));
        registryMethods(Math.class, e -> !e.getName().toLowerCase().contains("extra"));

        registryMethods(MixinProxyFactory.getMixinInstance(ArrayMixins.class));
        registryMethods(MixinProxyFactory.getMixinInstance(CollectionMixins.class));
        registryMethods(MixinProxyFactory.getMixinInstance(DateMixins.class));
        registryMethods(MixinProxyFactory.getMixinInstance(MapMixins.class));
        registryMethods(MixinProxyFactory.getMixinInstance(MatchMixins.class));
        registryMethods(MixinProxyFactory.getMixinInstance(MathMixins.class));
        registryMethods(MixinProxyFactory.getMixinInstance(RandomMixins.class));
        registryMethods(MixinProxyFactory.getMixinInstance(RegexMixins.class));
        registryMethods(MixinProxyFactory.getMixinInstance(StringMixins.class));
        registryMethods(MixinProxyFactory.getMixinInstance(UuidMixins.class));
    }

    public void registryMethods(Object target) {
        registryMethods(target, null);
    }

    public void registryMethods(Object target, Predicate<Method> filter) {
        List<IMethod> methods = FunicFunctionHelper.ofInstanceMethods(target, filter);
        registryMethods(methods);
    }

    public void registryMethods(Iterable<? extends IMethod> iterable) {
        if (iterable == null) {
            return;
        }
        for (IMethod item : iterable) {
            String name = item.getName();
            CopyOnWriteArrayList<IMethod> list = globalMethods.computeIfAbsent(name, key -> new CopyOnWriteArrayList<>());
            list.add(0, item);
        }
    }

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
        if (newArrayFilter == null || newArrayFilter.test(elementType)) {
            return super.newArray(elementType, count, visitor);
        }
        throw new FunicRejectException("New Array of element type [" + elementType + "] has been reject!");
    }

    @Override
    public Class<?> findClass(String className, DefaultFunicVisitor visitor) {
        if (findClassFilter == null || findClassFilter.test(className)) {
            return super.findClass(className, visitor);
        }
        throw new FunicRejectException("Find class [" + className + "] has been reject!");
    }

    @Override
    public List<IMethod> getStaticGlobalRegistryMethods(String methodName, DefaultFunicVisitor visitor) {
        List<IMethod> ret = new ArrayList<>();
        if (globalMethods != null && !globalMethods.isEmpty()) {
            List<IMethod> next = globalMethods.get(methodName);
            if (next != null) {
                ret.addAll(next);
            }
        }
        if (useStaticGlobalMethods.get()) {
            List<IMethod> next = super.getStaticGlobalRegistryMethods(methodName, visitor);
            if (next != null) {
                ret.addAll(next);
            }
        }
        return ret;
    }

    @Override
    public List<IMethod> getVisitorRegistryMethods(String methodName, DefaultFunicVisitor visitor) {
        if (useVisitorRegistryGlobalMethods.get()) {
            return super.getVisitorRegistryMethods(methodName, visitor);
        }
        return null;
    }

    @Override
    public List<IMethod> getBuiltinGlobalMethods(String methodName, DefaultFunicVisitor visitor) {
        if (useBuiltInGlobalMethods.get()) {
            return super.getBuiltinGlobalMethods(methodName, visitor);
        }
        return null;
    }

}
