package i2f.extension.antlr4.funvi.lang.resolver.impl;

import i2f.bindsql.BindSql;
import i2f.extension.antlr4.funvi.grammar.FunviVisitor;
import i2f.extension.antlr4.funvi.lang.exception.impl.FunviEvaluateException;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/8/3 10:53
 * @desc
 */
@Data
@NoArgsConstructor
public class BindSqlFunviResolver extends DefaultFunviResolver {

    @Override
    protected void initBlockHandlers() {
        super.initBlockHandlers();

        blockHandlers.put("where", (parameterList, bodyCtx, context, visitor) -> {
            if (bodyCtx == null) {
                throw new FunviEvaluateException("where block require body!");
            }
            Object ret = visitor.visitBlockBody(bodyCtx);
            if (ret != null) {
                String text = String.valueOf(ret);
                String trim = text.trim();
                trim = trim.replaceFirst("(?i)^(and|or)\\s+", " ");
                trim = trim.trim();
                if (!trim.isEmpty()) {
                    return " where " + trim;
                }
                return null;
            }
            return null;
        });

        blockHandlers.put("set", (parameterList, bodyCtx, context, visitor) -> {
            if (bodyCtx == null) {
                throw new FunviEvaluateException("set block require body!");
            }
            Object ret = visitor.visitBlockBody(bodyCtx);
            if (ret != null) {
                String text = String.valueOf(ret);
                String trim = text.trim();
                if (trim.startsWith(",")) {
                    trim = trim.substring(1);
                }
                if (trim.endsWith(",")) {
                    trim = trim.substring(trim.length() - 1);
                }
                trim = trim.trim();
                if (!trim.isEmpty()) {
                    return " set " + trim;
                }
                return null;
            }
            return null;
        });

    }

    @Override
    public Object concat(Object obj, Object append) {
        if (obj == null) {
            return append;
        }
        if (append == null) {
            return null;
        }
        BindSql left = null;
        BindSql right = null;
        if (obj instanceof BindSql) {
            left = (BindSql) obj;
        } else {
            left = BindSql.of(String.valueOf(obj));
        }
        if (append instanceof BindSql) {
            right = (BindSql) append;
        } else {
            right = BindSql.of(String.valueOf(append));
        }
        return left.concat(right);
    }

    @Override
    protected Object postProcessValue(Object ret, boolean isDollar, String expression, Object context, FunviVisitor<Object> visitor) {
        if (isDollar) {
            return ret == null ? null : String.valueOf(ret);
        }
        return BindSql.of("?", ret);
    }
}
