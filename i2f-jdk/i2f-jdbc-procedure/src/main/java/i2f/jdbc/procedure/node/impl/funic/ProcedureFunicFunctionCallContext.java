package i2f.jdbc.procedure.node.impl.tinyscript;

import i2f.extension.antlr4.script.funic.lang.context.FunicFunctionCallContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:59
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class ProcedureFunicFunctionCallContext extends FunicFunctionCallContext {
    protected JdbcProcedureExecutor executor;
    protected XmlNode node;

}