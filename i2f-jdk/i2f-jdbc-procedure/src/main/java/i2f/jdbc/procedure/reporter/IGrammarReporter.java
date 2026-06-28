package i2f.jdbc.procedure.reporter;

import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2026/5/14 16:03
 * @desc
 */
public interface IGrammarReporter {
    default void reportGrammar(JdbcProcedureExecutor executor,
                               Map<String, ProcedureMeta> metaMap,
                               Consumer<String> warnPoster) {
        reportGrammar(executor, new LinkedHashSet<>(metaMap.keySet()), metaMap, warnPoster);
    }

    void reportGrammar(JdbcProcedureExecutor executor,
                       Set<String> validMetaKeys,
                       Map<String, ProcedureMeta> metaMap,
                       Consumer<String> warnPoster);

    void reportGrammar(XmlNode node,
                       Map<String, ProcedureMeta> metaMap,
                       JdbcProcedureExecutor executor,
                       Consumer<String> warnPoster,
                       AtomicInteger reportCount,
                       AtomicInteger nodeCount);

    void reportAttributeFeatureGrammar(String attr,
                                       XmlNode node,
                                       String defaultFeature,
                                       Consumer<String> warnPoster);

    void reportExprFeatureGrammar(String expr,
                                  String feature,
                                  XmlNode node,
                                  String location,
                                  Consumer<String> warnPoster);
}
