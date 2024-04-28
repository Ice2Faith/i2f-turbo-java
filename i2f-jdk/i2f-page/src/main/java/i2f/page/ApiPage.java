package i2f.page;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/4/28 9:11
 * @desc
 */
@Data
@NoArgsConstructor
public class ApiPage {
    //从0开始，表示页索引
    protected Integer index;
    //页大小
    protected Integer size;
    //取数据的下标
    protected Integer offset;
    //取数据的结束下标
    protected Integer end;

    public ApiPage(Integer index, Integer size) {
        page(index, size);
    }

    public static ApiPage of(Integer index, Integer size) {
        return new ApiPage(index, size);
    }

    public ApiPage page(Integer index, Integer size) {
        this.index = index;
        this.size = size;
        prepare();
        return this;
    }

    public ApiPage prepare() {
        if (this.index != null && this.index < 0) {
            this.index = 0;
        }
        if (this.index != null && this.size < 0) {
            this.size = 20;
        }
        if (this.index != null && this.size != null) {
            this.offset = this.index * this.size;
            this.end = (this.index + 1) * this.size;
        }
        return this;
    }

    public boolean valid() {
        prepare();
        return this.index != null && this.size != null;
    }

    public void beginPage() {
        if (this.index == null) {
            this.index = 0;
        }
        if (this.size == null) {
            this.size = 20;
        }
        prepare();
    }


}
