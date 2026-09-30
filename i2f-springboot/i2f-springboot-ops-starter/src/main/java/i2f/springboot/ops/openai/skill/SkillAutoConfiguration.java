package i2f.springboot.ops.openai.skill;

import i2f.ai.std.skill.SkillDefinition;
import i2f.ai.std.skill.SkillsHelper;
import i2f.ai.std.skill.SkillsTools;
import i2f.spring.core.SpringContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    private static final ConcurrentHashMap<String, SkillDefinition> skillDefinitionMap = new ConcurrentHashMap<>();
    private static final ReentrantReadWriteLock lock=new ReentrantReadWriteLock();

    protected static final ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

    public static Map<String,SkillDefinition> getSkillDefinitionMap(){
        lock.readLock().lock();
        try {
            return new HashMap<>(skillDefinitionMap);
        }finally {
            lock.readLock().unlock();
        }
    }

    @ConditionalOnExpression("${ai.skills.tool.enable:true}")
    @Bean
    public SkillsTools skillsTools(@Autowired ApplicationContext applicationContext) {
        SkillsTools ret = new SkillsTools();
        ret.setContext(new SpringContext(applicationContext));
        ret.setSkillDefinitionSupplier(() -> getSkillDefinitionMap());
        return ret;
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
        lock.writeLock().lock();
        try {
            skillDefinitionMap.clear();
            skillDefinitionMap.putAll(map);
        }finally {
            lock.writeLock().unlock();
        }
    }
}
