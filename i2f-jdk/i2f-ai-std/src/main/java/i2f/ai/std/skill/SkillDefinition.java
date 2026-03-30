package i2f.ai.std.skill;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/3/23 18:22
 * @desc
 */
@Data
@NoArgsConstructor
public class SkillDefinition {
    protected String name;
    protected String description;
    protected Set<String> tags;
    protected String version;
    protected String author;
}
