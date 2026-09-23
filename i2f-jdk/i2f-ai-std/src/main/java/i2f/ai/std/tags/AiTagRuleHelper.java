package i2f.ai.std.tags;

import i2f.match.impl.AntMatcher;
import i2f.match.std.IMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/9/17 19:38
 * @desc
 */
public class AiTagRuleHelper {
    public static IMatcher matcher=new AntMatcher(".");
    public static List<String> resolveTags(String toolName, Collection<AiTagRule> rules){
        List<String> tags = new ArrayList<>();
        if(toolName==null||toolName.isEmpty()){
            return tags;
        }
        if(rules==null||rules.isEmpty()){
            return tags;
        }
        for (AiTagRule rule : rules) {
            if(rule==null){
                continue;
            }
            if (rule.getTags()==null) {
                continue;
            }
            String pattern = rule.getPattern();
            if(matcher.matches(toolName,pattern)){
                tags.addAll(rule.getTags());
            }
        }
        return tags;
    }
}
