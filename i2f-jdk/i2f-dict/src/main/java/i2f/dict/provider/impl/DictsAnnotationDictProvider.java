package i2f.dict.provider.impl;


import i2f.dict.annotations.Dict;
import i2f.dict.annotations.Dicts;
import i2f.dict.data.DictItem;
import i2f.dict.provider.IDictProvider;
import i2f.reflect.ReflectResolver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/2/21 16:07
 * @desc
 */
public class DictsAnnotationDictProvider implements IDictProvider {
    @Override
    public List<DictItem> getDictItems(Field field, Field dictField) {
        List<DictItem> ret = new ArrayList<>();
        Dicts dann = ReflectResolver.getAnnotation(dictField, Dicts.class);
        if (dann == null) {
            dann = ReflectResolver.getAnnotation(field, Dicts.class);
        }
        if (dann == null) {
            return ret;
        }
        for (Dict ann : dann.value()) {
            DictItem item = new DictItem();
            item.setCode(ann.code());
            item.setText(ann.text());
            item.setDesc(ann.desc());
            ret.add(item);
        }
        return ret;
    }
}
