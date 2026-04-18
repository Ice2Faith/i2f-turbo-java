package i2f.turbo.idea.plugin.inject.handlers;

import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.psi.PsiElement;


/**
 * @author Ice2Faith
 * @date 2026/4/16 11:28
 * @desc
 */
public abstract class IProjectInjectHandler<T extends PsiElement> {
    public abstract Class<T> supportType();

    public void inject(MultiHostRegistrar registrar, PsiElement context) {
        Class<? extends PsiElement> clazz = context.getClass();
        Class<T> supportType = supportType();
        if (!supportType.equals(clazz) && !supportType.isAssignableFrom(clazz)) {
            return;
        }
        doInjectInner(registrar, (T) context);
    }

    protected abstract void doInjectInner(MultiHostRegistrar registrar, T context);
}
