package i2f.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/28 9:11
 * @desc
 */
@Data
@NoArgsConstructor
public class Page<T> extends ApiPage {
    //总数据条数
    private Long total;
    //数据列表
    private List<T> list;

    public Page(Integer index, Integer size) {
        super(index, size);
    }

    public static <T> Page<T> of(ApiPage page, Long total, List<T> list) {
        Page<T> ret = new Page<>(page.getIndex(), page.getSize());
        ret.data(total, list);
        return ret;
    }

    public Page<T> data(long total, List<T> list) {
        this.total = total;
        this.list = list;
        return this;
    }


    public Page<T> data(List<T> list) {
        this.list = list;
        return this;
    }

}
