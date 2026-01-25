package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import i2f.jdbc.procedure.context.ProcedureMeta;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;

public class JdbcProcedureXmlProcedureJumpSourceFileAction extends AnAction {
    public static final Logger log = Logger.getInstance(JdbcProcedureXmlProcedureJumpSourceFileAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        Map.Entry<VirtualFile, Integer> file = getActionRefidAttributeValueJumpFile(event);
        if (file == null) {
            return;
        }
        FileEditorManager editorManager = FileEditorManager.getInstance(project);
        if (editorManager == null) {
            new OpenFileDescriptor(project, file.getKey()).navigate(true);
            return;
        }
        Editor editor = editorManager.openTextEditor(new OpenFileDescriptor(project, file.getKey()), true);
        if (editor == null) {
            new OpenFileDescriptor(project, file.getKey()).navigate(true);
            return;
        }
        Integer lineNumber = file.getValue();
        if (lineNumber != null && lineNumber > 0) {
            editor.visualLineToY(lineNumber);
        }

    }

    public Map.Entry<VirtualFile, Integer> getActionRefidAttributeValueJumpFile(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return null;
        }
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        VirtualFile virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
//        PsiElement element = event.getData(CommonDataKeys.PSI_ELEMENT);
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);

        SelectionModel selectionModel = editor.getSelectionModel();
        if (selectionModel != null) {
            String selectedText = selectionModel.getSelectedText();
            Map.Entry<VirtualFile, Integer> file = getProcedureFileByProcedureId(selectedText);
            if (file != null) {
                return file;
            }
        }

        int offset = editor.getCaretModel().getOffset();
        Document document = editor.getDocument();
//        log.warn("xml-jump-source document:"+document);
        VirtualFile editorFile = FileDocumentManager.getInstance().getFile(document);
//        log.warn("xml-jump-source editorFile:"+editorFile);
        if (editorFile == null) {
            return null;
        }
        PsiFile editorPisFile = PsiManager.getInstance(project).findFile(editorFile);
//        log.warn("xml-jump-source editorPsiFile:"+editorPisFile);
        if (editorPisFile == null) {
            return null;
        }
        XmlAttributeValue element = PsiTreeUtil.findElementOfClassAtOffset(editorPisFile, offset, XmlAttributeValue.class, false);

        if (element == null) {
            return null;
        }

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
                && !"id".equals(name)) {
            return null;
        }
        XmlAttributeValue xmlAttributeValue = (XmlAttributeValue) element;
        String value = xmlAttributeValue.getValue();
        return getProcedureFileByProcedureId(value);
    }

    public static Map.Entry<VirtualFile, Integer> getProcedureFileByProcedureId(String value) {
        if (value == null) {
            return null;
        }
        String[] arr = value.split(":");
        Integer lineNumber = null;
        if (arr.length >= 2) {
            try {
                value = arr[0];
                lineNumber = Integer.parseInt(arr[1]);
            } catch (Exception e) {
            }
        }
        if (value.endsWith(".xml")) {
            value = value.substring(0, value.length() - ".xml".length());
        }
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
        return new AbstractMap.SimpleEntry<>(file, lineNumber);
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        Map.Entry<VirtualFile, Integer> file = getActionRefidAttributeValueJumpFile(event);
        if (file == null) {
            event.getPresentation().setEnabledAndVisible(false);
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
