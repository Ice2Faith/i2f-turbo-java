package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import i2f.jdbc.procedure.context.ProcedureMeta;
import org.jetbrains.annotations.NotNull;

public class JdbcProcedureXmlProcedureJumpSourceFileAction extends AnAction {
    public static final Logger log = Logger.getInstance(JdbcProcedureXmlProcedureJumpSourceFileAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        VirtualFile file = getActionRefidAttributeValueJumpFile(event);
        if(file==null){
            return;
        }
        new OpenFileDescriptor(project, file).navigate(true);
    }

    public VirtualFile getActionRefidAttributeValueJumpFile(AnActionEvent event){
        Project project = event.getProject();
        if (project == null) {
            return null;
        }
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        VirtualFile virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
//        PsiElement element = event.getData(CommonDataKeys.PSI_ELEMENT);
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);

        int offset = editor.getCaretModel().getOffset();
        Document document = editor.getDocument();
//        log.warn("xml-jump-source document:"+document);
        VirtualFile editorFile = FileDocumentManager.getInstance().getFile(document);
//        log.warn("xml-jump-source editorFile:"+editorFile);
        PsiFile editorPisFile = PsiManager.getInstance(project).findFile(editorFile);
//        log.warn("xml-jump-source editorPsiFile:"+editorPisFile);
        XmlAttributeValue element = PsiTreeUtil.findElementOfClassAtOffset(editorPisFile, offset, XmlAttributeValue.class, false);

//        log.warn("xml-jump-source element:"+element);
        if (!(element instanceof XmlAttributeValue)) {
            return null;
        }
//        log.warn("xml-jump-source xml-attr...");
        XmlAttribute xmlAttribute = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);
        if (xmlAttribute == null) {
            return null;
        }
        String name = xmlAttribute.getName();
//        log.warn("xml-jump-source attr-name: " + name);
        if (!"refid".equals(name)
        &&!"id".equals(name)) {
            return null;
        }
        XmlAttributeValue xmlAttributeValue = (XmlAttributeValue) element;
        String value = xmlAttributeValue.getValue();
//        log.warn("xml-jump-source attr-value: " + value);
        ProcedureMeta meta = JdbcProcedureProjectMetaHolder.PROCEDURE_META_MAP.get(value);
//        log.warn("xml-jump-source attr-meta: " + meta);
        if (meta == null) {
            return null;
        }
        VirtualFile file = (VirtualFile) meta.getTarget();
//        log.warn("xml-jump-source attr-meta-file: " + file);
        if (file == null || !file.isValid()) {
            return null;
        }
//        log.warn("xml-jump-source attr-meta-file-path: " + file.getPath());
        return file;
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        VirtualFile file = getActionRefidAttributeValueJumpFile(event);
        if(file==null){
            event.getPresentation().setEnabledAndVisible(false);
        }
    }
}
