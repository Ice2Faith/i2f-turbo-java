package i2f.spi.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as an SPI (Service Provider Interface) implementation.
 *
 * <p>When used in conjunction with the {@code i2f:spi} Maven plugin goal, classes annotated
 * with {@code @Spi} are automatically discovered during the build, and corresponding
 * {@code META-INF/services/} descriptor files are generated for each declared interface.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @Spi({PaymentService.class, NotificationService.class})
 * public class AlipayServiceImpl implements PaymentService, NotificationService {
 *     // ...
 * }
 * }</pre>
 *
 * <p>This will generate:</p>
 * <ul>
 *   <li>{@code META-INF/services/com.example.PaymentService} → containing the implementation class name</li>
 *   <li>{@code META-INF/services/com.example.NotificationService} → containing the implementation class name</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/9/9 16:38
 * @see java.util.ServiceLoader
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Spi {

    /**
     * The SPI interface classes that this implementation provides.
     *
     * @return array of interface classes this class implements as an SPI provider
     */
    Class<?>[] value();
}
