package i2f.agent.transformer;

import i2f.match.StringMatcher;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/7/31 17:30
 * @desc
 */
@Data
@NoArgsConstructor
public abstract class BasicClassPatternClassFileTransformer implements ClassFileTransformer {
    protected Map<String, Set<String>> actionClassPattensMap = new ConcurrentHashMap<>();

    public BasicClassPatternClassFileTransformer(Map<String, Set<String>> actionClassPattensMap) {
        this.actionClassPattensMap = actionClassPattensMap;
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        Set<String> actions = new LinkedHashSet<>();

        className = className.replaceAll("/", ".");

        for (Map.Entry<String, Set<String>> entry : actionClassPattensMap.entrySet()) {
            String action = entry.getKey();
            Set<String> pattens = entry.getValue();
            if (pattens == null || pattens.isEmpty()) {
                continue;
            }
            for (String patten : pattens) {
                if (StringMatcher.antClass().match(className, patten)) {
                    actions.add(action);
                    break;
                }
            }
        }

        if (actions.isEmpty()) {
            return classfileBuffer;
        }

        return transformActions(actions,
                loader,
                className,
                classBeingRedefined,
                protectionDomain,
                classfileBuffer);
    }

    public abstract byte[] transformActions(Set<String> actions,
                                            ClassLoader loader,
                                            String className,
                                            Class<?> classBeingRedefined,
                                            ProtectionDomain protectionDomain,
                                            byte[] classfileBuffer) throws IllegalClassFormatException;
}
