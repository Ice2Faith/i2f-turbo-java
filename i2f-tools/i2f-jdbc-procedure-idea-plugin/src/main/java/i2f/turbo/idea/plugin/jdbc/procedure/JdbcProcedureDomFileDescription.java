package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2025/6/26 21:44
 * @desc
 */
public class JdbcProcedureDomFileDescription extends DomFileDescription<ProcedureDomElement> {
    public JdbcProcedureDomFileDescription() {
        super(ProcedureDomElement.class, XProc4jConsts.XML_ROOT_TAG_NAME, new String[0]);
    }

    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        XmlTag rootTag = file.getRootTag();
        return null != rootTag && super.getRootTagName().equals(rootTag.getName());
    }

}
