package i2f.page;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/3/20 19:45
 * @desc
 */
@Data
@NoArgsConstructor
public class ApiOffsetSize {
    //取数据的下标
    protected Integer offset;
    //页大小
    protected Integer size;
    //取数据的结束下标
    protected Integer end;

    public ApiOffsetSize(ApiOffsetSize page) {
        pageOffset(page);
    }

    public ApiOffsetSize(Integer offset, Integer size) {
        this.offset = offset;
        this.size = size;
    }

    public ApiOffsetSize pageOffset(ApiOffsetSize page) {
        if (page == null) {
            return this;
        }
        page.prepare();
        this.offset = page.getOffset();
        this.size = page.getSize();
        this.end = page.getEnd();
        return this;
    }

    public static ApiOffsetSize of(Integer offset, Integer size) {
        return new ApiOffsetSize(offset, size);
    }

    public static ApiOffsetSize ofOffsetEnd(Integer offset, Integer end) {
        Integer size = null;
        if (offset != null && end != null) {
            size = end - offset;
        } else if (offset != null) {

        } else if (end != null) {
            offset = 0;
        }
        return of(offset, size);
    }

    public ApiOffsetSize prepare() {
        if (offset != null && size != null) {
            end = offset + size;
        } else if (size != null) {
            offset = 0;
        }
        return this;
    }
}
