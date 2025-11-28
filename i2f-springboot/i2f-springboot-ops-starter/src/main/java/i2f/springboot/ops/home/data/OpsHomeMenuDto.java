package i2f.springboot.ops.home.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/28 14:37
 */
@Data
@NoArgsConstructor
public class OpsHomeMenuDto {
    protected String title;
    protected String subTitle;
    protected String icon;
    protected String href;
    protected boolean disabled;

    public OpsHomeMenuDto title(String title) {
        this.title = title;
        return this;
    }

    public OpsHomeMenuDto subTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public OpsHomeMenuDto icon(String icon) {
        this.icon = icon;
        return this;
    }

    public OpsHomeMenuDto href(String href) {
        this.href = href;
        return this;
    }

    public OpsHomeMenuDto disabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }
}
