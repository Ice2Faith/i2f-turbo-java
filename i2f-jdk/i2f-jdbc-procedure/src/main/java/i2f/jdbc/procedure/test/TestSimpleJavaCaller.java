package i2f.jdbc.procedure.test;


import i2f.jdbc.procedure.annotations.JdbcProcedure;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
//import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/11 9:24
 */
//@Component("simpleBlock")
@JdbcProcedure(
        value="SP_SIMPLE",
        arguments = {

        }
)
public class TestSimpleJavaCaller implements JdbcProcedureJavaCaller {
    @Override
    public Object exec(JdbcProcedureExecutor executor, Map<String, Object> params) throws Throwable {
        if(false) {
            String inCondition = executor.visitAs("IN_CONDITION",params);
            int idx = inCondition.toUpperCase().indexOf("PAYMENT_MONTH BETWEEN");
            if (idx >= 0) {
                String vPaymentMonth = inCondition.substring(idx + 33, idx + 33 + 6);
                executor.visitSet(params,"v_PAYMENT_MONTH", vPaymentMonth);
            } else {
                idx = inCondition.toUpperCase().indexOf("PAYMENT_MONTH = ");
                int len = "PAYMENT_MONTH = ".length();
                String vPaymentMonth = inCondition.substring(idx + len, idx + len + 6);
                executor.visitSet(params,"v_PAYMENT_MONTH", vPaymentMonth);
            }
        }
        System.out.println("hello java caller");
        return null;
    }
}
