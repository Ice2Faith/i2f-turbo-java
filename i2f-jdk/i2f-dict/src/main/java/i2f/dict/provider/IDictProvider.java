package i2f.dict.provider;

import i2f.dict.data.DictItem;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/2/21 16:04
 * @desc
 */
public interface IDictProvider {
    List<DictItem> getDictItems(Field field, Field dictField);
}
