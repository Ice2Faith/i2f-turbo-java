package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.extension.antlr4.script.funic.lang.Funic;
import i2f.extension.antlr4.script.funic.lang.resolver.FunicResolver;
import i2f.extension.antlr4.script.funic.lang.resolver.impl.DefaultFunicResolver;
import i2f.extension.antlr4.script.funic.lang.resolver.impl.SafeFunicResolverProxy;
import i2f.extension.antlr4.script.funic.lang.resolver.impl.SandboxFunicResolver;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/6 14:27
 * @desc
 */
@ConditionalOnExpression("${ai.tools.math.enable:true}")
@Component
@Tools
public class MathTools {
    private static final FunicResolver resolver = SandboxFunicResolver.createDefault();

    @Data
    @NoArgsConstructor
    public static class MathEvalResultItem {
        protected int index;
        protected String expression;
        protected boolean success;
        protected Object result;
        protected String errorMessage;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "evaluate math expressions; " +
                    " support `+`,`-`,`*`,`/`,`(`,`)`,`%`(mod double operator, or percent suffix operator) math operators; " +
                    " support sin(a),cos(a),tan(a),asin(a),acos(a),atan(a),atan2(y,x),sinh(x),cosh(x),tanh(x) trigonometric functions, with arguments in radians; " +
                    " support the toRadians(a) function to convert angles to radians, and the toDegrees(a) function to convert radians to angles; " +
                    " support exp(a),log(a),log10(a),sqrt(a),ceil(a),floor(a),round(a),pow(a,b),abs(a),max(a,b),min(a,b) math functions;" +
                    " support max_of(...numbers),min_of(...numbers),avg_of(...numbers),sum_of(...numbers) multiply arguments math functions"
    )
    public List<MathEvalResultItem> eval_math_expression(
            @ToolParam(value = "expressions", description = "the math expression list, for example [\"1+(2-5)*3/4%6*3.14\"] or [\"sin(toRadians(63))+12.125\",\"sum_of(1,2,3,4,5)\"]")
            List<String> expressions) {
        List<MathEvalResultItem> ret = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
            String expression = expressions.get(i);

            MathEvalResultItem item = new MathEvalResultItem();
            item.setIndex(i);
            item.setExpression(expression);
            ret.add(item);

            try {
                Object val = Funic.script(expression, new HashMap<>(),resolver);
                item.setSuccess(true);
                item.setResult(val);
            } catch (Exception e) {
                item.setSuccess(false);
                item.setErrorMessage("evaluate expression error, " + e.getClass() + " : " + e.getMessage());
            }
        }

        return ret;

    }

}
