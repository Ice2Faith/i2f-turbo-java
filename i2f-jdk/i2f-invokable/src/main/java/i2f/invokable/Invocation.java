package i2f.invokable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/7/3 13:53
 */
@Data
@NoArgsConstructor
public class Invocation {
    protected Object target;
    protected IInvokable invokable;
    protected Object[] args;

    public Invocation(Object ivkObj, IInvokable invokable, Object... args) {
        this.target = ivkObj;
        this.invokable = invokable;
        this.args = args;
    }
}
