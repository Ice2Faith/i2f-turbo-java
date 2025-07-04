package i2f.extension.mybatis.ognl;

import i2f.lru.LruMap;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.ognl.OgnlException;

/**
 * @author Ice2Faith
 * @date 2024/10/9 13:49
 */
public class OgnlUtil {
    public static final LruMap<String, Object> EXPRESSION_MAP = new LruMap<>(4096);

    public static Object evaluateExpression(String expression, Object root) throws Exception {
        OgnlContext context = new OgnlContext(null, null, DefaultMemberAccess.INSTANCE);
        context.put("$root", root);
        context.setRoot(root);
        Object tree = parseExpressionTreeNode(expression);
        return Ognl.getValue(tree, context, context.getRoot());
    }

    public static Object parseExpressionTreeNode(String expression) throws OgnlException {
        if (expression != null) {
            expression = expression.trim();
        }
        try {
            Object ret = EXPRESSION_MAP.get(expression);
            if (ret != null) {
                return ret;
            }
        } catch (Exception e) {

        }
        Object tree = Ognl.parseExpression(expression);
        try {
            EXPRESSION_MAP.put(expression, tree);
        } catch (Exception e) {

        }
        return tree;
    }

}
