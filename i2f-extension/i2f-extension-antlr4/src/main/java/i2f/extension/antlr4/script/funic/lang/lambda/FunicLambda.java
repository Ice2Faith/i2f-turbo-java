package i2f.extension.antlr4.script.funic.lang.lambda;

import i2f.extension.antlr4.script.funic.grammar.FunicParser;
import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import i2f.invokable.method.IMethod;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/4/23 16:50
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class FunicLambda {
    protected List<Map.Entry<String, Object>> arguments;
    protected FunicParser.ScriptBlockContext body;

    public Object invoke(DefaultFunicVisitor visitor) {
        Map<String, Object> callContext = new HashMap<>();
        callContext.put("global", visitor.getContext());
        for (int i = 0; i < arguments.size(); i++) {
            Map.Entry<String, Object> entry = arguments.get(i);
            String name = entry.getKey();
            if (name == null) {
                name = "arg" + i;
            }
            callContext.put(name, entry.getValue());
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

}
