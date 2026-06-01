package i2f.springboot.ops.openai.tool;

import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawDefinitionsProvider;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.annotations.Tools;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/1 19:09
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@Component
public class SpringContextToolDefinitionProvider implements ToolRawDefinitionsProvider,
        ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public List<ToolRawDefinition> getTools() {

        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(Tools.class);
        Map<String, ToolRawDefinition> definitionMap = ToolRawHelper.parseTools(null, beanMap.values());

        return new ArrayList<>(definitionMap.values());
    }
}
