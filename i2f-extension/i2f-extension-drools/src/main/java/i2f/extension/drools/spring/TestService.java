package i2f.extension.drools.spring;

import i2f.extension.drools.test.Order;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2026/11/7 17:57
 * @desc
 */
@Data
@NoArgsConstructor
@Component
public class TestService {
    @Autowired
    private KieBase kieBase;

    public void test() {
        KieSession ksession = kieBase.newKieSession();

        Order order = new Order();
        order.setAmount(76);

        ksession.insert(order);

        ksession.fireAllRules();

        ksession.dispose();
    }
}
