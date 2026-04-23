package i2f.extension.antlr4.script.funic.lang.method;

import i2f.extension.antlr4.script.funic.grammar.FunicParser;
import i2f.extension.antlr4.script.funic.lang.Funic;
import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import i2f.invokable.method.IMethod;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/4/23 15:29
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class FunicMethod implements IMethod {
    protected String name;
    protected Class<?> returnType;
    protected List<Map.Entry<Class, String>> parameters;
    protected FunicParser.ScriptBlockContext body;
    protected DefaultFunicVisitor visitor;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return Funic.class;
    }

    @Override
    public int getModifiers() {
        return Modifier.PUBLIC | Modifier.STATIC;
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameters.stream().map(e -> e.getKey()).collect(Collectors.toList()).toArray(new Class[0]);
    }

    @Override
    public int getParameterCount() {
        return parameters.size();
    }

    @Override
    public Object invoke(Object ivkObj, Object... args) throws Throwable {
        Map<String, Object> callContext = new HashMap<>();
        callContext.put("global", visitor.getContext());
        for (int i = 0; i < args.length; i++) {
            Object value = args[i];
            Map.Entry<Class, String> entry = parameters.get(i);
            String name = entry.getValue();
            callContext.put(name, value);
        }
        DefaultFunicVisitor newVisitor = new DefaultFunicVisitor(callContext, visitor.getResolver());
        newVisitor.getImportPackages().addAll(visitor.getImportPackages());
        ConcurrentHashMap<String, CopyOnWriteArrayList<IMethod>> registryMethods = visitor.getRegistryMethods();
        for (Map.Entry<String, CopyOnWriteArrayList<IMethod>> entry : registryMethods.entrySet()) {
            newVisitor.getRegistryMethods().put(entry.getKey(), new CopyOnWriteArrayList<>(entry.getValue()));
        }
        FunicValue retValue = newVisitor.visitScriptBlock(body);
        return retValue.get();
    }

    @Override
    public void setAccessible(boolean accessible) {

    }

    public FunicMethod copy() {
        FunicMethod ret = FunicMethod.builder()
                .name(name)
                .parameters(new ArrayList<>(parameters))
                .returnType(returnType)
                .body(body)
                .visitor(visitor)
                .build();
        return ret;
    }
}
