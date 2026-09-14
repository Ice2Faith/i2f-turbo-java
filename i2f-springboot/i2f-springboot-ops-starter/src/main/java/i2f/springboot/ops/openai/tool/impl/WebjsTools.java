package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.os.OsUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/9/14 19:59
 * @desc
 */
@ToolIntent(items = @ToolIntentItem(value="webjs",description = "提供用户交互UI弹窗、单选框、多选框等"))
@ConditionalOnExpression("${ai.tools.webjs.enable:true}")
@Data
@NoArgsConstructor
@Component
@Tools(tags = {
        "webjs"
})
public class WebjsTools {

    public static RuntimeException getUnSupportException() {
        return new UnsupportedOperationException("user webjs environment execute error, please try another way!");
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "popup a radio selection dialog to user, returns singleton option or user input content."
    )
    public String webjs_form_radio(@ToolParam(value = "question", description = "the question, for example \"what's your prefer color?\"")
                                   String question,
                                   @ToolParam(value = "options", description = "the selection options, for example [\"white\", \"black\"]")
                                   List<String> options
    )  {
        throw getUnSupportException();
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "popup a checkbox selection dialog to user, returns multiple option, maybe include user input content."
    )
    public List<String> webjs_form_checkbox(@ToolParam(value = "question", description = "the question, for example \"what's your prefer color?\"")
                                            String question,
                                            @ToolParam(value = "options", description = "the selection options, for example [\"white\", \"black\"]")
                                            List<String> options
    ) {
        throw getUnSupportException();
    }
}
