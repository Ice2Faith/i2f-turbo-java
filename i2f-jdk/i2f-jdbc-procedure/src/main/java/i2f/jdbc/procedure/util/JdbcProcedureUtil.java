package i2f.jdbc.procedure.util;

import i2f.jdbc.procedure.executor.impl.BasicJdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/5/8 10:13
 */
public class JdbcProcedureUtil {
    public static final String ABS_EXEC_NODE_CLASS_NAME = AbstractExecutorNode.class.getName();
    public static final String BASIC_EXECUTOR_CLASS_NAME = BasicJdbcProcedureExecutor.class.getName();

    public static <T extends Throwable> T purifyStackTrace(T re,boolean deep) {
        if (re == null) {
            return re;
        }
        StackTraceElement[] arr = Arrays.stream(re.getStackTrace())
                .filter(v -> {
                    String className = v.getClassName();
                    if (className.startsWith(ABS_EXEC_NODE_CLASS_NAME)
                            || className.startsWith(BASIC_EXECUTOR_CLASS_NAME)) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList())
                .toArray(new StackTraceElement[0]);
        re.setStackTrace(arr);
        if(deep){
            Throwable cause = re.getCause();
            if(cause!=null){
                purifyStackTrace(cause,deep);
            }
        }
        return re;
    }
}
