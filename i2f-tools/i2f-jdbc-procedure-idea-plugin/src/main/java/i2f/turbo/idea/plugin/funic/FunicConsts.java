package i2f.turbo.idea.plugin.funic;

import com.intellij.openapi.util.IconLoader;
import i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicTokenType;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class FunicConsts {
    public static final String LANGUAGE_ID = "Funic";

    public static final Icon ICON = IconLoader.getIcon("/assets/funic/funic.svg", FunicConsts.class);

    public static final String FILE_DESCRIPTION = LANGUAGE_ID + " language file";

    public static final String FILE_EXTENSION = "fic";

    public static final Set<String> KEYWORDS = Collections.unmodifiableSet(getKeywords());

    protected static Set<String> getKeywords() {
        Set<String> completions = new TreeSet<>();
        completions.addAll(Arrays.asList("null", "true", "false", "class"));
        Field[] fields = FunicTypes.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().startsWith("KW_")) {
                try {
                    FunicTokenType kwToken = (FunicTokenType) field.get(null);
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
