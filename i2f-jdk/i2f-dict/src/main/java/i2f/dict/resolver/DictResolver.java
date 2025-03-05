package i2f.dict.resolver;

import i2f.convert.obj.ObjectConvertor;
import i2f.dict.annotations.DictDecode;
import i2f.dict.annotations.DictEncode;
import i2f.dict.data.DictItem;
import i2f.dict.provider.IDictProvider;
import i2f.dict.provider.impl.DictsAnnotationDictProvider;
import i2f.reflect.ReflectResolver;
import i2f.text.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/2/21 11:40
 * @desc
 */
public class DictResolver {
    public static final IDictProvider AnnotationDictProvider = new DictsAnnotationDictProvider();

    public static void decode(Object bean) throws Exception {
        decode(bean, AnnotationDictProvider);
    }

    public static void decode(Object bean, IDictProvider... providers) throws Exception {
        if (bean == null) {
            return;
        }
        Class<?> clazz = bean.getClass();
        Map<Field, Class<?>> fields = ReflectResolver.getFields(clazz);
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        for (Field item : fields.keySet()) {
            if (!fieldMap.containsKey(item.getName())) {
                fieldMap.put(item.getName(), item);
            }
        }

        for (Field field : fields.keySet()) {
            DictDecode deann = ReflectResolver.getAnnotation(field, DictDecode.class);
            if (deann == null) {
                continue;
            }
            String fieldName = deann.value();
            Field dictField = field;
            if (!StringUtils.isEmpty(fieldName)) {
                Field fd = fieldMap.get(fieldName);
                if (fd != null) {
                    dictField = fd;
                }
            }

            List<DictItem> dicts = new ArrayList<>();
            for (IDictProvider provider : providers) {
                dicts = provider.getDictItems(field, dictField);
                if (!dicts.isEmpty()) {
                    break;
                }
            }
            if (dicts.isEmpty()) {
                continue;
            }

            field.setAccessible(true);
            dictField.setAccessible(true);

            Object val = dictField.get(bean);
            String code = "";
            if (val != null) {
                code = String.valueOf(val);
            }

            for (DictItem dict : dicts) {
                if (code.equals(dict.getCode())) {
                    try {
                        Class<?> type = field.getType();
                        Object sval = ObjectConvertor.tryConvertAsType(dict.getText(), type);
                        field.set(bean, sval);
                        break;
                    } catch (Exception e) {

                    }

                }
            }

        }
    }

    public static void encode(Object bean) throws Exception {
        encode(bean, AnnotationDictProvider);
    }

    public static void encode(Object bean, IDictProvider... providers) throws Exception {
        if (bean == null) {
            return;
        }
        Class<?> clazz = bean.getClass();
        Map<Field, Class<?>> fields = ReflectResolver.getFields(clazz);
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        for (Field item : fields.keySet()) {
            fieldMap.put(item.getName(), item);
        }

        for (Field field : fields.keySet()) {
            DictEncode deann = ReflectResolver.getAnnotation(field, DictEncode.class);
            if (deann == null) {
                continue;
            }
            String fieldName = deann.value();
            Field dictField = field;
            if (!StringUtils.isEmpty(fieldName)) {
                Field fd = fieldMap.get(fieldName);
                if (fd != null) {
                    dictField = fd;
                }
            }

            List<DictItem> dicts = new ArrayList<>();
            for (IDictProvider provider : providers) {
                dicts = provider.getDictItems(field, dictField);
                if (!dicts.isEmpty()) {
                    break;
                }
            }
            if (dicts.isEmpty()) {
                continue;
            }

            field.setAccessible(true);
            dictField.setAccessible(true);

            Object val = dictField.get(bean);
            String text = "";
            if (val != null) {
                text = String.valueOf(val);
            }

            for (DictItem dict : dicts) {
                if (text.equals(dict.getText())) {

                    try {
                        Class<?> type = field.getType();
                        Object sval = null;
                        if (!"".equals(dict.getCode())) {
                            sval = ObjectConvertor.tryConvertAsType(dict.getCode(), type);
                        }
                        field.set(bean, sval);
                        break;
                    } catch (Exception e) {

                    }

                }
            }

        }
    }


}
