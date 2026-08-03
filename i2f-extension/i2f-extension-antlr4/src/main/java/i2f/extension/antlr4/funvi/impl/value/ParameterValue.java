package i2f.extension.antlr4.funvi.impl.value;

import i2f.extension.antlr4.funvi.grammar.FunviParser;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/8/3 10:19
 * @desc
 */
@Data
@NoArgsConstructor
public class ParameterValue {
    protected FunviParser.ParameterContext node;
    protected String name;
    protected String expression;
}
