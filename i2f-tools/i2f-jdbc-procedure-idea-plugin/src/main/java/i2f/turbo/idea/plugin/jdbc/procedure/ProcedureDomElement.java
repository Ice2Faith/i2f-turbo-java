package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author Ice2Faith
 * @date 2025/6/26 21:46
 * @desc
 */
public interface ProcedureDomElement extends DomElement {
    @Attribute("id")
    GenericAttributeValue<String> getId();
}
