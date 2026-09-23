package i2f.ai.std.tags;

import java.util.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/9/17 19:37
 * @desc
 */
@Data
@NoArgsConstructor
public class AiTagRule {
    protected String pattern;
    protected List<String> tags;
}
