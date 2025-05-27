package i2f.spring.ai.chat.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.security.SecureRandom;

/**
 * @author Ice2Faith
 * @date 2025/5/27 22:22
 * @desc
 */
public class MathTools {
    protected SecureRandom random = new SecureRandom();

    @Tool(description = "get a random integer number/" +
            "获取一个随机整数")
    public int nextRandomInteger(@ToolParam(description = "minimal integer number, default: 0/" +
            "最小整数，默认：0") Integer min,
                                 @ToolParam(description = "maximal integer number, default: " + Integer.MAX_VALUE + "/" +
                                         "最大整数，默认：" + Integer.MAX_VALUE) Integer max) {
        if (min == null) {
            min = 0;
        }
        if (max == null) {
            max = Integer.MAX_VALUE;
        }
        return random.nextInt(max - min) + min;
    }

    @Tool(description = "get a random double number/" +
            "获取一个随机小数")
    public double nextRandomDouble() {
        return random.nextDouble();
    }
}
