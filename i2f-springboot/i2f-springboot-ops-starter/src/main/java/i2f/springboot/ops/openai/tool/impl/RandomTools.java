package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * @author Ice2Faith
 * @date 2026/6/1 19:13
 * @desc
 */
@ConditionalOnExpression("${ai.tools.random.enable:true}")
@Component
@Tools(tags = {
        AiTags.RANDOM_VALUE
})
public class RandomTools {

    private SecureRandom random = new SecureRandom();

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "generate an random integer number."
    )
    public int generate_random_int_number() {
        return Math.abs(random.nextInt());
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "generate an random integer number between [0~max)."
    )
    public int generate_random_int_number_max(@ToolParam(value = "max", description = "max integer value, for example: 100 or 12")
                                              int max) {
        return Math.abs(random.nextInt()) % max;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "generate an random integer number between [min~max)."
    )
    public int generate_random_int_number_between(@ToolParam(value = "min", description = "max integer value, for example: 8 or 12")
                                                  int min,
                                                  @ToolParam(value = "max", description = "max integer value, for example: 24 or 100")
                                                  int max) {
        return Math.abs(random.nextInt()) % (max - min) + min;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "generate an random float number,  value between [0.0~1.0]."
    )
    public double generate_random_float_number() {
        return random.nextDouble();
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "generate an random boolean value."
    )
    public boolean generate_random_boolean() {
        return random.nextBoolean();
    }

}
