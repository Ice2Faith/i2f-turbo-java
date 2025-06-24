package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author Ice2Faith
 * @date 2025/6/24 14:19
 */
public class XProc4jConsts {
    public static final String NAME="XProc4J";
    public static final String EXTENSION="xml";
    public static final String DEFAULT_MIME="text/xproc4j";
    public static final String[] MIME={DEFAULT_MIME,"text/jdbc-procedure"};
    public static final Icon ICON= IconLoader.getIcon("/assets/action.svg", JdbcProcedureXmlCompletionContributor.class);

}
