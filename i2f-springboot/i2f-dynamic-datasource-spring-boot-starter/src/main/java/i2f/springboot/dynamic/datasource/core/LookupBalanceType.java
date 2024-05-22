package i2f.springboot.dynamic.datasource.core;

/**
 * @author Ice2Faith
 * @date 2024/5/22 21:49
 * @desc
 */
public enum LookupBalanceType {
    RING("ring"),
    RANDOM("random"),
    UNKNOWN("unknown");

    private String type;

    private LookupBalanceType(String type) {
        this.type = type;
    }

    public String type() {
        return this.type;
    }

    public static LookupBalanceType of(String type) {
        for (LookupBalanceType item : LookupBalanceType.values()) {
            if (item.type().equalsIgnoreCase(type)) {
                return item;
            }
        }
        return LookupBalanceType.UNKNOWN;
    }
}
