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
            description = "evaluate math expressions; \n" +
                    " support `+`,`-`,`*`,`/`,`(`,`)`,`%`(mod double operator, or percent suffix operator) math operators; \n" +
                    " support sin(a),cos(a),tan(a),asin(a),acos(a),atan(a),atan2(y,x),sinh(x),cosh(x),tanh(x) trigonometric functions, with arguments in radians; \n" +
                    " support the toRadians(a) function to convert angles to radians, and the toDegrees(a) function to convert radians to angles; \n" +
                    " support exp(a),log(a),log10(a),sqrt(a),ceil(a),floor(a),round(a),pow(a,b),abs(a),max(a,b),min(a,b) math functions;\n" +
                    " support max_of(...numbers),min_of(...numbers),avg_of(...numbers),sum_of(...numbers) multiply arguments math functions; \n" +
                    " Note: The ^ operator performs a bitwise XOR, not exponentiation; use the pow function for powers;\n "+
                    " The expression supports multi-statement composition, \n" +
                    " with each statement separated by a semicolon. \n" +
                    " Variables can be used and assigned to directly without prior declaration;\n" +
                    " undeclared variables will default to null ;\n" +
                    " Returns the result of the final statement,  \n" +
                    " for example `a=0;b=1;c=a+b; c/2`.\n" +
                    " It returns the result of the final statement `c/2`, which evaluates to `(0 + 1)/2` here. \n" +
                    " To return multiple values, format the expression as a JSON array. For example, `a=1;b=2;[a,b,3]` evaluates to `[1,2,3]`, \n" +
                    " or use JSON Object likely format. For example `a=1;b=2;{a: a,b: b}` evaluates to `{a: 1,b: 2}`. \n" +
                    " Supports `for(i=0;i<10;i++){};` and `for(i in [1,2,3]){};` loop statements. \n" +
                    " Note that the loop body must be wrapped in {}, and the statement must end with a semicolon (i.e., };). \n"+
                    " for example `arr=[1,2,3];sum=0;for(i=0;i<arr.size();i++){sum+=arr[i];};sum` or `arr=[1,2,3];sum=0;for(i=0;i<arr.size();i++){sum+=arr.get(i);};sum` or `arr=[1,2,3];sum=0;for(i in arr){sum+=i;};sum`\n" +
                    " support `if(i>0){}else if(i<5){} else {};` condition statements.\n" +
                    " Note that the condition body must be wrapped in {}, and the statement must end with a semicolon (i.e., };). \n" +
                    " for example `a=1;if(a>0){a--};a` or `a=1;b=0;if(a>5){b=1;}else{b=0};[a,b]`"
    )
    public List<MathEvalResultItem> eval_math_expression(
            @ToolParam(value = "expressions", description = "the math expression list, for example [\"1+(2-5)*3/4%6*3.14\"] or [\"sin(toRadians(63))+12.125\",\"sum_of(1,2,3,4,5)\"] or [\"a=0;b=1;\nc=2;(a+b)/c\",\"a=1;b=2;\nc=a/b;\nc\"]")
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
