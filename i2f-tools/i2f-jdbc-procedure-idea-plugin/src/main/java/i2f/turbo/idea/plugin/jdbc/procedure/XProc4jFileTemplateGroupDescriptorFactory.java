package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;

/**
 * @author Ice2Faith
 * @date 2025/6/24 14:47
 */
public class XProc4jFileTemplateGroupDescriptorFactory implements FileTemplateGroupDescriptorFactory {
    public static final String XPROC4J_XML_TEMPLATE = XProc4jConsts.NAME+" file.xml";


    @Override
    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor(XProc4jConsts.NAME, XProc4jConsts.ICON); //NON-NLS

        group.addTemplate(new FileTemplateDescriptor(XPROC4J_XML_TEMPLATE, XProc4jConsts.ICON));

        return group;
    }
}
