package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.form.dialog.DialogBoxes;
import i2f.form.dialog.checkbox.CheckboxResult;
import i2f.form.dialog.radio.RadioResult;
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
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/8/29 14:35
 * @desc
 */
@ToolIntent(items = @ToolIntentItem(value="form",description = "提供用户交互弹窗、单选框、多选框等"))
@ConditionalOnExpression("${ai.tools.form.enable:false}")
@Conditional(FormTools.WindowsFormCondition.class)
@Data
@NoArgsConstructor
@Component
@Tools(tags = {
        "form"
})
public class FormTools {
    public static class WindowsFormCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            if (!OsUtil.isWindows()) {
                return false;
            }
            return true;
        }
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "popup a radio selection dialog to user, returns singleton option or user input content."
    )
    public String form_radio(@ToolParam(value = "question", description = "the question, for example \"what's your prefer color?\"")
                             String question,
                             @ToolParam(value = "options", description = "the selection options, for example [\"white\", \"black\"]")
                             List<String> options
    ) {
        if (!OsUtil.isWindows()) {
            throw new IllegalStateException("current OS is not windows, cannot use form tool!");
        }

        DialogBoxes.enableHeadless(false);

        RadioResult result = DialogBoxes.radio(question, options, true, "radio box");
        if (result.isCancel()) {
            throw new IllegalStateException("user canceled selection.");
        }

        return result.getContent();
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "popup a checkbox selection dialog to user, returns multiple option, maybe include user input content."
    )
    public List<String> form_checkbox(@ToolParam(value = "question", description = "the question, for example \"what's your prefer color?\"")
                                      String question,
                                      @ToolParam(value = "options", description = "the selection options, for example [\"white\", \"black\"]")
                                      List<String> options
    ) {
        if (!OsUtil.isWindows()) {
            throw new IllegalStateException("current OS is not windows, cannot use form tool!");
        }

        DialogBoxes.enableHeadless(false);

        CheckboxResult result = DialogBoxes.checkbox(question, options, true, "checkbox box");
        if (result.isCancel()) {
            throw new IllegalStateException("user canceled selection.");
        }
        List<CheckboxResult.Choice> choices = result.getChoices();
        return choices.stream().map(CheckboxResult.Choice::getContent).collect(Collectors.toList());
    }
}
