package i2f.extension.ognl;

import ognl.Ognl;
import ognl.OgnlContext;

/**
 * @author Ice2Faith
 * @date 2024/10/9 13:49
 */
public class OgnlUtil {
    public static Object evaluateExpression(String expression, Object root) throws Exception {
        OgnlContext context = new OgnlContext(null, null, DefaultMemberAccess.INSTANCE);
        context.put("$root", root);
        context.setRoot(root);
        Object tree = Ognl.parseExpression(expression);
        return Ognl.getValue(tree, context, context.getRoot());
    }
}
