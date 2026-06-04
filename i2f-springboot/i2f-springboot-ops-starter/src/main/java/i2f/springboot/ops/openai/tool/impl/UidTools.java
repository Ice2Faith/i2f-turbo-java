package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.Tools;
import i2f.uid.SnowflakeLongUid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/6/1 19:13
 * @desc
 */
@ConditionalOnExpression("${ai.tools.uid.enable:true}")
@Component
@Tools
public class UidTools {

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "generate an new uuid."
    )
    public String create_new_uuid() {
        return UUID.randomUUID().toString();
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "generate an new snowflake id."
    )
    public long create_new_snowflake_id() {
        return SnowflakeLongUid.getId();
    }

}
