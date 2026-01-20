package i2f.turbo.idea.plugin.jdbc.procedure.reference;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ice2Faith
 * @date 2026/1/20 8:46
 * @desc
 */
public class XmlTagReference extends PsiReferenceBase<XmlToken> implements PsiPolyVariantReference {

    public XmlTagReference(@NotNull XmlToken element) {
        super(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        String tagName = myElement.getText();
        String className = convertToClassName(tagName);

        PsiManager manager = myElement.getManager();
        Project project = myElement.getProject();

        // 在整个项目（含依赖库）中查找类
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        PsiClass[] classes = JavaPsiFacade.getInstance(manager.getProject())
                .findClasses(className, scope);

        ResolveResult[] results = new ResolveResult[classes.length];
        for (int i = 0; i < classes.length; i++) {
            results[i] = new PsiElementResolveResult(classes[i]);
        }
        return results;
    }

    @Override
    public PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length == 1 ? results[0].getElement() : null;
    }

    private static String convertToClassName(String tagName) {
        String[] arr = tagName.split("_|-");
        StringBuilder builder=new StringBuilder();
        for (String item : arr) {
            item=item.trim();
            if(item.isEmpty()){
                continue;
            }
            builder.append(item.substring(0,1).toUpperCase()).append(item.substring(1));
        }
        builder.append("Node");
        return builder.toString();
    }
}
