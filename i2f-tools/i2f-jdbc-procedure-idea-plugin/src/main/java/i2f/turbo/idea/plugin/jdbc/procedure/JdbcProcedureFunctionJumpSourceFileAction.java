package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlTag;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.turbo.idea.plugin.jdbc.procedure.completion.CompletionHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JdbcProcedureFunctionJumpSourceFileAction extends AnAction {
    public static final Logger log = Logger.getInstance(JdbcProcedureFunctionJumpSourceFileAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        List<PsiElement> elements = getActionJumpSourcePsiElements(event);
        if (elements == null || elements.isEmpty()) {
            return;
        }
        if (elements.size() == 1) {
            navigateToElement(elements.get(0));
        } else {
            showNavigationPopup(event, elements);
        }

    }

    private void navigateToElement(PsiElement element) {
        if (!element.isValid()) return;

        PsiFile containingFile = element.getContainingFile();
        if (containingFile == null) {
            return;
        }

        Project project = element.getProject();
        int offset = element.getTextOffset();

        // 确保文档已同步
        PsiDocumentManager.getInstance(project).commitAllDocuments();

        new OpenFileDescriptor(project, containingFile.getVirtualFile(), offset)
                .navigate(true);
    }

    private void showNavigationPopup(@NotNull AnActionEvent event, @NotNull List<PsiElement> elements) {
        if (elements.isEmpty()) {
            return;
        }
        Editor editor = event.getData(CommonDataKeys.EDITOR);

        JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<PsiElement>("Choose Declaration", elements) {
            @Override
            public @NotNull String getTextFor(PsiElement element) {
                if (element instanceof PsiMethod) {
                    PsiMethod method = (PsiMethod) element;
                    return CompletionHelper.getPsiMethodSignature(method);
                } else if (element instanceof XmlTag) {
                    XmlTag xmlTag = (XmlTag) element;
                    String id = xmlTag.getAttributeValue("id");
                    ProcedureMeta meta = JdbcProcedureProjectMetaHolder.PROCEDURE_META_MAP.get(id);
                    return CompletionHelper.getProcedureMetaSignature(meta);
                }
                return element.getText();
            }

            @Override
            public PopupStep<?> onChosen(PsiElement selectedValue, boolean finalChoice) {
                navigateToElement(selectedValue);
                return FINAL_CHOICE;
            }
        }).showInBestPositionFor(editor);
    }


    public List<PsiElement> getActionJumpSourcePsiElements(AnActionEvent event) {
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
            List<PsiElement> elements = CompletionHelper.getXmlFileFunctionsPsiElementsFast(project, selectedText);
            if (elements != null && !elements.isEmpty()) {
                return elements;
            }
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);
        if (element == null) {
            return null;
        }
        String text = element.getText();
//        log.warn("xml-func:"+element.getClass()+":"+text);
        List<PsiElement> elements = CompletionHelper.getXmlFileFunctionsPsiElementsFast(project, text);
        if (elements != null && !elements.isEmpty()) {
            return elements;
        }
        return new ArrayList<>();
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        List<PsiElement> list = getActionJumpSourcePsiElements(event);
        if (list == null || list.isEmpty()) {
            event.getPresentation().setEnabledAndVisible(false);
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
