package i2f.turbo.idea.plugin.funvi;

import com.intellij.openapi.util.IconLoader;
import i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class FunviConsts {
    public static final String LANGUAGE_ID = "Funvi";

    public static final Icon ICON = IconLoader.getIcon("/assets/funvi/funvi.svg", FunviConsts.class);

    public static final String FILE_DESCRIPTION = LANGUAGE_ID + " language file";

    public static final String FILE_EXTENSION = "fvi";

    public static final Set<String> KEYWORDS = Collections.unmodifiableSet(getKeywords());

    protected static Set<String> getKeywords() {
        Set<String> completions = new TreeSet<>();
        completions.addAll(Arrays.asList("null", "true", "false", "class"));
        completions.addAll(Arrays.asList(
                "#sharp","#dollar",
                "#if","#else","##",
                "#foreach","#for","#while",
                "#break","#continue",
                "#trim","#bind","#set",
                "#where"));
        Field[] fields = FunviTypes.class.getDeclaredFields();
        for (Field field : fields) {

        }
        return completions;
    }
}
