package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.Tools;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * @author Ice2Faith
 * @date 2026/6/1 19:13
 * @desc
 */
@Component
@Tools
public class RandomTools {

    private SecureRandom random = new SecureRandom();

    @Tool(description = "generate an random integer number.")
    public int generate_random_int_number() {
        return random.nextInt();
    }

    @Tool(description = "generate an random float number,  value between [0.0~1.0].")
    public double generate_random_float_number() {
        return random.nextDouble();
    }

    @Tool(description = "generate an random boolean value.")
    public boolean generate_random_boolean() {
        return random.nextBoolean();
    }

}
