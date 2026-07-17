package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.extension.antlr4.script.funic.lang.Funic;
import i2f.extension.antlr4.script.funic.lang.resolver.FunicResolver;
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
@Tools(tags = {
        AiTags.MATH_VALUE,
        AiTags.SCRIPT_VALUE
})
public class MathTools {
    private static final FunicResolver resolver = SandboxFunicResolver.createDefault().toMutator()
            .apply(u->{u.getUseVisitorRegistryGlobalMethods().set(true);})
            .done();

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
            /*language=markdown*/
            description = "Math/logic evaluator (BigDecimal/BigInteger).\n" +
                    "- Multi-statements via `;` (returns final).\n" +
                    "- Vars auto-declare (null).\n" +
                    "- Return JSON `[a,b]` or `{a:1}` for multiples.\n" +
                    "- allow `//`,`/**/` comment, allow line sep.\n" +
                    "Ops: \n" +
                    "- `+,-,*,/,%,()` for normal, `~,&,|,^,>>,<<,>>>` for bitwise,`==,!=,>,<,>=,<=,eq,ne,gt,lt,gte,lte,in,not in` for compare, `||,&&,!,and,or,not` for logic. \n" +
                    "- Note: `^`=XOR, use `pow(a,b)` for powers.\n" +
                    "Strings: `\"` or `'`. Escape same-type quotes only. `+` concatenates (coerces to string), support Java String methods (e.g., `indexOf()`). \n" +
                    "Funcs:\n" +
                    "- Trig(rad): sin,cos,tan,asin,acos,atan,atan2,sinh,cosh,tanh,to_radians,to_degrees.\n" +
                    "- Math: exp,log,log10,sqrt,ceil,floor,round,pow,abs,max,min.\n" +
                    "- Aggregates(arr/args): max_of,min_of,avg_of,sum_of.\n" +
                    "- Radix(2-36): from_radix(text,radix),to_radix(num,radix).\n" +
                    "- Misc: fibonacci(n),factorial(n).\n" +
                    "- Oracle-compat(lowercase): numbers,strings,regex category functions(e.g., `trunc()`,`ltrim()`,`regexp_like()`). `substr` is 0-based.\n" +
                    "Control: \n" +
                    "- `for(i=0;i<n;i++){...};` or `for(i : arr){...};` or `while(cond){};` (continue/break, no labels).\n" +
                    "- `if(cond){...}else if{...}else{...};`. \n" +
                    "- `def add(a,b){...};` (udf, declare before use, no embed, overload by args cnt, return one). \n" +
                    "    - Strict isolation: outer var `a` to function inside `global.a`, outer not has `global`.\n" +
                    "- Blocks need `{}`, stmts must end with `;`.\n" +
                    "Collections:\n" +
                    "- JSON arrays/objects support Java List/Map methods (e.g., `add()`,`put()`,`get()`). \n" +
                    "- Direct access: `arr[index]` (get/set), `map.name` (get/put)."
    )
    public List<MathEvalResultItem> eval_math_expression(
            @ToolParam(value = "expressions", description = "the math expression list, for example [\"1+(2-5)*3/4%6*3.14\"] or [\"sin(to_radians(63))+12.125\",\"sum_of(1,2,3,4,5)\"] or [\"a=0;b=1;\nc=2;(a+b)/c\",\"a=1;b=2;\nc=a/b;\nc\"]")
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
                item.setErrorMessage("evaluate expression error, maybe your variable/function name is internal reserved keywords or block not end with '};', " + e.getClass() + " : " + e.getMessage());
            }
        }

        return ret;

    }


}
