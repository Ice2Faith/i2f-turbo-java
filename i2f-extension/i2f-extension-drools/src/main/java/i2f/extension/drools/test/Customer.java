package i2f.extension.drools.test;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/11/7 16:09
 * @desc
 */
@Data
@NoArgsConstructor
public class Customer {
    protected String name;
    protected List<Order> orders;
}
