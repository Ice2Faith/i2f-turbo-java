package i2f.jdbc.procedure.node.impl.tinyscript;

import i2f.extension.antlr4.script.tiny.impl.context.DefaultFunctionCallContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:59
 * @desc
 */
@Data
@NoArgsConstructor
public class ProcedureFunctionCallContext extends DefaultFunctionCallContext {
    protected JdbcProcedureExecutor executor;
    protected XmlNode node;

}