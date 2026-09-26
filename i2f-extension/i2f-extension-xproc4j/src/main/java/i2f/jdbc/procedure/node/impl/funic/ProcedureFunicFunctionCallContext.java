package i2f.jdbc.procedure.node.impl.funic;

import i2f.extension.antlr4.script.funic.lang.context.FunicFunctionCallContext;
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
public class ProcedureFunicFunctionCallContext extends FunicFunctionCallContext {
    protected JdbcProcedureExecutor executor;
    protected XmlNode node;

}