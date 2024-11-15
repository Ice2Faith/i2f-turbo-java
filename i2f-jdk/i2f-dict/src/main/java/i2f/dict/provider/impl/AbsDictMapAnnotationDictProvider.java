package i2f.dict.provider.impl;


import i2f.dict.annotations.DictMap;
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
public abstract class AbsDictMapAnnotationDictProvider implements IDictProvider {

    @Override
    public List<DictItem> getDictItems(Field field, Field dictField) {
        List<DictItem> ret = new ArrayList<>();
        DictMap dann = ReflectResolver.getAnnotation(dictField, DictMap.class);
        if (dann == null) {
            dann = ReflectResolver.getAnnotation(field, DictMap.class);
        }
        if (dann == null) {
            return ret;
        }
        List<DictItem> list = findDictList(dann);
        return list;
    }

    public abstract List<DictItem> findDictList(DictMap ann);
}
