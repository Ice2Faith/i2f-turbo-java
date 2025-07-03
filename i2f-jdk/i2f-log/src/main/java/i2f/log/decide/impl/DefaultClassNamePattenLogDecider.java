package i2f.log.decide.impl;

import i2f.log.decide.ILogDecider;
import i2f.log.std.enums.LogLevel;
import i2f.match.StringMatcher;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:41
 * @desc
 */
@Data
@NoArgsConstructor
public class DefaultClassNamePattenLogDecider implements ILogDecider {
    private LogLevel rootLevel = LogLevel.INFO;
    private Map<String, LogLevel> pattenMapping = new ConcurrentHashMap<>();

    private StringMatcher matcher = StringMatcher.antClass();

    public void registry(String patten, LogLevel level) {
        pattenMapping.put(patten, level);
    }

    public void remove(String patten) {
        pattenMapping.remove(patten);
    }

    @Override
    public boolean enableLevel(LogLevel level, String location) {
        if (pattenMapping.isEmpty()) {
            return level.level() <= rootLevel.level();
        }
        List<String> priored = matcher.priorMatches(location, pattenMapping.keySet());
        if (priored.isEmpty()) {
            return level.level() <= rootLevel.level();
        }
        String pattern = priored.get(0);
        LogLevel pattenLevel = pattenMapping.get(pattern);

        return level.level() <= pattenLevel.level();
    }
}
