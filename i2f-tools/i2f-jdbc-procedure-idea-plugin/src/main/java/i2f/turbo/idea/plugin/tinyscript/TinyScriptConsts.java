package i2f.turbo.idea.plugin.tinyscript;

import com.intellij.openapi.util.IconLoader;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptTokenType;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class TinyScriptConsts {
    public static final String LANGUAGE_ID = "TinyScript";

    public static final Icon ICON = IconLoader.getIcon("/assets/tinyscript/tinyscript.svg", TinyScriptConsts.class);

    public static final String FILE_DESCRIPTION = LANGUAGE_ID + " language file";

    public static final String FILE_EXTENSION = "tis";


    public static Set<String> KEYWORDS = Collections.unmodifiableSet(getKeywords());

    protected static Set<String> getKeywords() {
        Set<String> completions = new TreeSet<>();
        completions.addAll(Arrays.asList("null", "true", "false", "class"));
        Field[] fields = TinyScriptTypes.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().startsWith("KEY_")) {
                try {
                    TinyScriptTokenType kwToken = (TinyScriptTokenType) field.get(null);
                    String debugName = kwToken.getDebugName();
                    if (debugName.startsWith("KEY_")) {
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
