package i2f.extension.ognl;

import i2f.lru.LruMap;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Ice2Faith
 * @date 2024/10/9 13:49
 */
public class OgnlUtil {
    public static final String PROPERTY_CACHE_EXPRESSION_SIZE="lru.ognl.util.cache-expression-size";
    public static final LruMap<String, Object> EXPRESSION_MAP = new LruMap<>(LruMap.getSysPropMaxSize(PROPERTY_CACHE_EXPRESSION_SIZE,4096));

    public static Object evaluateExpression(String expression, Object root) throws Exception {
        OgnlContext context = new OgnlContext(null, null, DefaultMemberAccess.INSTANCE);
        context.put("$root", root);
        context.setRoot(root);
        Object tree = parseExpressionTreeNode(expression);
        return Ognl.getValue(tree, context, context.getRoot());
    }

    public static Object parseExpressionTreeNode(String expression) throws OgnlException {
        if (expression == null) {
            throw new OgnlException("expression cannot be null");
        }
        expression = expression.trim();

        try {
            Object ret = EXPRESSION_MAP.get(expression);
            if (ret != null) {
                return ret;
            }
        } catch (Exception e) {

        }
        Object tree = Ognl.parseExpression(expression);
        try {
            boolean needCache = true;
            int keyLen = expression.length();
            if (keyLen < 20) {
                needCache = ThreadLocalRandom.current().nextDouble() < 0.05;
            } else if (keyLen < 40) {
                needCache = ThreadLocalRandom.current().nextDouble() < 0.15;
            } else if (keyLen < 60) {
                needCache = ThreadLocalRandom.current().nextDouble() < 0.25;
            } else if (keyLen < 80) {
                needCache = ThreadLocalRandom.current().nextDouble() < 0.5;
            } else if (keyLen < 100) {
                needCache = ThreadLocalRandom.current().nextDouble() < 0.8;
            }
            if (needCache) {
                EXPRESSION_MAP.put(expression, tree);
            }
        } catch (Exception e) {

        }
        return tree;
    }

}
