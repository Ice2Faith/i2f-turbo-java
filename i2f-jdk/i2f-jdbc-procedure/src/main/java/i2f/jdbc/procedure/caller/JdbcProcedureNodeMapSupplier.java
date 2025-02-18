package i2f.jdbc.procedure.caller;

import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/18 22:06
 * @desc
 */
@FunctionalInterface
public interface JdbcProcedureNodeMapSupplier {
    Map<String, XmlNode> getNodeMap();

    default XmlNode getNode(String procedureId) {
        return getNodeMap().get(procedureId);
    }
}
