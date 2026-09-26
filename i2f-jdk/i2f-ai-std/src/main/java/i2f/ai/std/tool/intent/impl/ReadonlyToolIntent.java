package i2f.ai.std.tool.intent.impl;

import i2f.ai.std.tool.intent.IToolIntent;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Ice2Faith
 * @date 2026/8/31 15:55
 * @desc
 */
@EqualsAndHashCode
@ToString
public final class ReadonlyToolIntent implements IToolIntent {
    private final String label;
    private final String description;

    public ReadonlyToolIntent(String label) {
        this.label = label;
        this.description = null;
    }

    public ReadonlyToolIntent(String label, String description) {
        this.label = label;
        this.description = description;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public String description() {
        return description;
    }
}
