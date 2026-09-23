package i2f.turbo.idea.plugin.ognl;

import com.intellij.openapi.util.IconLoader;
import i2f.turbo.idea.plugin.ognl.grammar.psi.OgnlTypes;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlTokenType;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class OgnlConsts {
    public static final String LANGUAGE_ID = "Ognl";

    public static final Icon ICON = IconLoader.getIcon("/assets/ognl/ognl.svg", OgnlConsts.class);

    public static final String FILE_DESCRIPTION = LANGUAGE_ID + " language file";

    public static final String FILE_EXTENSION = "ognl";

    public static final Set<String> KEYWORDS = Collections.unmodifiableSet(getKeywords());

    protected static Set<String> getKeywords() {
        Set<String> completions = new TreeSet<>();
        completions.addAll(Arrays.asList("null", "true", "false", "class"));
        Field[] fields = OgnlTypes.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().startsWith("KW_")) {
                try {
                    OgnlTokenType kwToken = (OgnlTokenType) field.get(null);
                    String debugName = kwToken.getDebugName();
                    if (debugName.startsWith("KW_")) {
                        continue;
                    }
                    completions.add(debugName);
                } catch (Exception e) {

                }
            }
        }
        return completions;
    }
}
