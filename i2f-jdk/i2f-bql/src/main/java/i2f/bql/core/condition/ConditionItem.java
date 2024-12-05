package i2f.bql.core.condition;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2024/12/5 8:54
 */
@Data
@NoArgsConstructor
public class ConditionItem {
    protected boolean and = true;
    protected String alias;
    protected Serializable column;
    protected Condition value;

    public ConditionItem(boolean and, String alias, Serializable column, Condition value) {
        this.and = and;
        this.alias = alias;
        this.column = column;
        this.value = value;
    }
}
