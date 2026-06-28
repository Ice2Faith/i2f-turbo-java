package i2f.tools;

import i2f.tools.application.WarBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ice2Faith
 * @date 2026/6/8 9:26
 * @desc
 */
@SpringBootApplication
public class ToolOpsApplication extends WarBootApplication {
    public static void main(String[] args) {
        startup(ToolOpsApplication.class, args);
    }
}
