package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.Tools;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/6/1 19:13
 * @desc
 */
@Component
@Tools
public class UidTools {

    @Tool(description = "generate an new uuid.")
    public String create_new_uuid() {
        return UUID.randomUUID().toString();
    }

}
