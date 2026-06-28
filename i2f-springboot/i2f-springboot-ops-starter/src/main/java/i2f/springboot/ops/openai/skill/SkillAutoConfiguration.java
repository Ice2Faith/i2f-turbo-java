package i2f.springboot.ops.openai.skill;

import i2f.ai.std.skill.SkillDefinition;
import i2f.ai.std.skill.SkillsHelper;
import i2f.ai.std.skill.SkillsTools;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/6/22 14:45
 * @desc
 */
@ConditionalOnExpression("${ai.skills.enable:true}")
@Slf4j
@Data
@Configuration
public class SkillAutoConfiguration implements ApplicationRunner {

    public static final ConcurrentHashMap<String, SkillDefinition> skillDefinitionMap = new ConcurrentHashMap<>();

    protected static final ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

    @ConditionalOnExpression("${ai.skills.tool.enable:true}")
    @Bean
    public SkillsTools skillsTools() {
        return new SkillsTools();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.refreshSkillDefinitions();
        pool.scheduleWithFixedDelay(this::refreshSkillDefinitions,
                0,
                30,
                TimeUnit.SECONDS);
    }

    public void refreshSkillDefinitions() {
        Map<String, SkillDefinition> map = SkillsHelper.scanFileSystemSkills();
        skillDefinitionMap.putAll(map);
    }
}
