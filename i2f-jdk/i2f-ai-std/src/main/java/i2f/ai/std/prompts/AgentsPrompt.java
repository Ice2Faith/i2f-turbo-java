package i2f.ai.std.prompts;

import i2f.resources.ResourceUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Ice2Faith
 * @date 2026/11/6 14:55
 * @desc
 */
public class AgentsPrompt {
    public static String getAgentsMarkdown() {
        try {
            String text = ResourceUtil.getResourceAsString("./AGENTS.md", StandardCharsets.UTF_8.name());
            if (text != null && !text.isEmpty()) {
                return text;
            }
        } catch (IOException e) {
            // ignore
        }

        try {
            String text = ResourceUtil.getClasspathResourceAsString("/AGENTS.md", StandardCharsets.UTF_8.name());
            if (text != null && !text.isEmpty()) {
                return text;
            }
        } catch (IOException e) {
            // ignore
        }

        return null;
    }
}
