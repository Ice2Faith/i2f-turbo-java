package i2f.turbo.idea.plugin.inject.handlers;

import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.psi.PsiElement;


/**
 * @author Ice2Faith
 * @date 2026/4/16 11:28
 * @desc
 */
public abstract class IProjectInjectHandler<T extends PsiElement> {
    /**
     * 支持注入的元素类型。返回 null 表示当前环境不支持（例如依赖的插件不可用），该 handler 会被整体跳过。
     */
    public abstract Class<T> supportType();

    public void inject(MultiHostRegistrar registrar, PsiElement context) {
        Class<T> supportType = supportType();
        if (supportType == null) {
            return;
        }
        Class<? extends PsiElement> clazz = context.getClass();
        if (!supportType.equals(clazz) && !supportType.isAssignableFrom(clazz)) {
            return;
        }
        doInjectInner(registrar, (T) context);
    }

    protected abstract void doInjectInner(MultiHostRegistrar registrar, T context);
}
