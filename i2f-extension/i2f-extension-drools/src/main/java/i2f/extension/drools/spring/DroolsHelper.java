package i2f.extension.drools.spring;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2026/11/7 18:00
 * @desc
 */
@Data
@NoArgsConstructor
@Component
public class DroolsHelper {
    private KieBase kieBase;

    public <T> T trigger(Function<KieSession, T> worker) {
        KieSession kieSession = kieBase.newKieSession();
        T ret = worker.apply(kieSession);
        kieSession.dispose();
        return ret;
    }
}
