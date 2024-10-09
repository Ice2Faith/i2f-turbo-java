package i2f.extension.ognl;

import ognl.MemberAccess;
import ognl.OgnlContext;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2024/10/9 13:49
 */
public class DefaultMemberAccess implements MemberAccess {

    public static final DefaultMemberAccess INSTANCE = new DefaultMemberAccess();

    protected boolean allowPrivateAccess = false;
    protected boolean allowProtectedAccess = false;
    protected boolean allowPackageProtectedAccess = false;

    public DefaultMemberAccess() {
        this(true);
    }

    public DefaultMemberAccess(boolean allowAllAccess) {
        this(allowAllAccess, allowAllAccess, allowAllAccess);
    }

    public DefaultMemberAccess(boolean allowPrivateAccess, boolean allowProtectedAccess, boolean allowPackageProtectedAccess) {
        this.allowPrivateAccess = allowPrivateAccess;
        this.allowProtectedAccess = allowProtectedAccess;
        this.allowPackageProtectedAccess = allowPackageProtectedAccess;
    }

    @Override
    public Object setup(OgnlContext context, Object target, Member member, String propertyName) {
        Object result = null;
        if (isAccessible(context, target, member, propertyName)) {
            AccessibleObject accessible = (AccessibleObject) member;
            if (!accessible.isAccessible()) {
                result = Boolean.FALSE;
                accessible.setAccessible(true);
            }
        }
        return result;
    }

    @Override
    public void restore(OgnlContext context, Object target, Member member, String propertyName, Object state) {
        if (state == null) {
            return;
        }
        ((AccessibleObject) member).setAccessible(((Boolean) state));

    }

    @Override
    public boolean isAccessible(OgnlContext map, Object o, Member member, String s) {
        int modifiers = member.getModifiers();
        if (Modifier.isPublic(modifiers)) {
            return true;
        }

        if (Modifier.isPrivate(modifiers)) {
            return allowPrivateAccess;
        }
        if (Modifier.isProtected(modifiers)) {
            return allowProtectedAccess;
        }
        return allowPackageProtectedAccess;
    }

}
