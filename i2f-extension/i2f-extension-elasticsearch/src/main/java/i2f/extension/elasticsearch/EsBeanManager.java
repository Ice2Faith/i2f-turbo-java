package i2f.extension.elasticsearch;

import i2f.extension.elasticsearch.annotation.EsField;
import i2f.extension.elasticsearch.annotation.EsId;
import i2f.extension.elasticsearch.annotation.EsIndex;
import i2f.page.Page;
import i2f.reflect.ReflectResolver;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2022/5/8 16:59
 * @desc
 */
public class EsBeanManager {
    protected EsManager manager;

    public EsBeanManager() {

    }

    public EsBeanManager(EsManager manager) {
        this.manager = manager;
    }

    public EsManager getManager() {
        return manager;
    }

    public EsBeanManager setManager(EsManager manager) {
        this.manager = manager;
        return this;
    }

    //////////////////////////////////////////
    public static volatile ConcurrentHashMap<Class, String> esIndexCache = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<Field, String> esFieldNameCache = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<Class, Field> esIdFieldCache = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<Class, List<Field>> esFieldsCache = new ConcurrentHashMap<>();

    public static String getIndex(Class clazz) {
        if (esIndexCache.containsKey(clazz)) {
            return esIndexCache.get(clazz);
        }
        String ret = clazz.getSimpleName();
        EsIndex ann = ReflectResolver.getAnnotation(clazz, EsIndex.class);
        if (ann != null) {
            if (!"".equals(ann.value())) {
                ret = ann.value();
            }
        }
        esIndexCache.put(clazz, ret);
        return ret;
    }

    public static String getName(Field field) {
        if (esFieldNameCache.containsKey(field)) {
            return esFieldNameCache.get(field);
        }
        String ret = field.getName();
        EsField ann = ReflectResolver.findAnnotation(field, EsField.class);
        if (ann != null) {
            if (!"".equals(ann.alias())) {
                ret = ann.alias();
            }
        }
        esFieldNameCache.put(field, ret);
        return ret;
    }

    public static Field getIdField(Class clazz) {
        if (esIdFieldCache.containsKey(clazz)) {
            return esIdFieldCache.get(clazz);
        }
        Map<Field, Set<EsId>> list = ReflectResolver.getFieldsWithAnnotation(clazz, EsId.class);
        Iterator<Field> iterator = list.keySet().iterator();
        Field ret = null;
        if (iterator.hasNext()) {
            ret = iterator.next();
        }
        esIdFieldCache.put(clazz, ret);
        return ret;
    }

    public static Object getIdValue(Object bean) {
        Field accessor = getIdField(bean.getClass());
        try {
            return ReflectResolver.valueGet(bean, accessor);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static List<Field> getEsFields(Class clazz) {
        if (esFieldsCache.containsKey(clazz)) {
            return esFieldsCache.get(clazz);
        }
        Map<Field, Class<?>> list = ReflectResolver.getFields(clazz);
        List<Field> ret = new ArrayList<>();
        for (Field field : list.keySet()) {
            if (field == null) {
                continue;
            }
            EsField ann = ReflectResolver.findAnnotation(field, EsField.class);
            if (ann != null) {
                if (!ann.value()) {
                    continue;
                }
            }
            ret.add(field);
        }
        esFieldsCache.put(clazz, ret);
        return ret;
    }

    public static String getIdName(Class clazz) {
        Field accessor = getIdField(clazz);
        if (accessor == null) {
            return null;
        }

        return getName(accessor);
    }

    public static Map<String, Object> bean2EsMap(Object obj) {
        Map<String, Object> ret = new HashMap<>();
        if (obj == null) {
            return null;
        }
        Class clazz = obj.getClass();
        List<Field> fields = getEsFields(clazz);
        for (Field item : fields) {
            try {
                Object val = ReflectResolver.valueGet(obj, item);
                String name = getName(item);
                ret.put(name, val);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return ret;
    }

    public static <T> T esMap2Bean(Map<String, Object> map, Class<T> beanClass) {
        Map<String, Object> beanMap = new HashMap<>();
        List<Field> fields = getEsFields(beanClass);
        for (Map.Entry<String, Object> item : map.entrySet()) {
            String name = item.getKey();
            Object val = item.getValue();
            for (Field field : fields) {
                String fname = getName(field);
                if (fname.equals(name)) {
                    name = field.getName();
                }
            }
            beanMap.put(name, val);
        }
        try {
            T ret = ReflectResolver.getInstance(beanClass);
            return ReflectResolver.copyWeak(beanMap, ret);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    //////////////////////////////////////////

    public boolean indexCreate(Class clazz) throws IOException {
        String indexName = getIndex(clazz);
        return manager.indexCreate(indexName);
    }

    public GetIndexResponse indexSearch(Class clazz) throws IOException {
        String indexName = getIndex(clazz);
        return manager.indexSearch(indexName);
    }

    public boolean indexDelete(Class clazz) throws IOException {
        String indexName = getIndex(clazz);
        return manager.indexDelete(indexName);
    }

    public boolean indexExists(Class clazz) throws IOException {
        String indexName = getIndex(clazz);
        return manager.indexExists(indexName);
    }

    ////////////////////////////////////////////
    public boolean recordsInsert(Object bean) throws IOException {
        String indexName = getIndex(bean.getClass());
        Object id = getIdValue(bean);
        Map<String, Object> map = bean2EsMap(bean);
        return manager.recordsInsert(indexName, String.valueOf(id), map);
    }

    public boolean recordsUpdate(Object bean) throws IOException {
        String indexName = getIndex(bean.getClass());
        Object id = getIdValue(bean);
        Map<String, Object> map = bean2EsMap(bean);
        return manager.recordsUpdate(indexName, String.valueOf(id), map);
    }

    public boolean recordsDelete(Object bean) throws IOException {
        String indexName = getIndex(bean.getClass());
        Object id = getIdValue(bean);
        return manager.recordsDelete(indexName, String.valueOf(id));
    }

    public GetResponse recordsGet(Object bean) throws IOException {
        String indexName = getIndex(bean.getClass());
        Object id = getIdValue(bean);
        return manager.recordsGet(indexName, String.valueOf(id));
    }

    public <T> T recordsGetAsBean(T bean) throws IOException {
        String indexName = getIndex(bean.getClass());
        Object id = getIdValue(bean);
        Map<String, Object> map = manager.recordsGetAsMap(indexName, String.valueOf(id));
        Class<T> clazz = (Class<T>) bean.getClass();
        return esMap2Bean(map, clazz);
    }

    ////////////////////////////////////////////
    public <T> boolean recordsBatchInsert(List<T> list) throws IOException {
        T bean = list.get(0);
        String indexName = getIndex(bean.getClass());
        Map<String, Map<String, Object>> docs = new HashMap<>();
        for (T item : list) {
            Object id = getIdValue(item);
            Map<String, Object> map = bean2EsMap(item);
            docs.put(String.valueOf(id), map);
        }
        return manager.recordsBatchInsertMap(indexName, docs);
    }

    public <T> boolean recordsBatchDelete(List<T> list) throws IOException {
        T bean = list.get(0);
        String indexName = getIndex(bean.getClass());
        List<String> ids = new ArrayList<>();
        for (T item : list) {
            Object id = getIdValue(item);
            ids.add(String.valueOf(id));
        }
        return manager.recordsBatchDelete(indexName, ids);
    }

    public <T> boolean recordsBatchUpdate(List<T> list) throws IOException {
        T bean = list.get(0);
        String indexName = getIndex(bean.getClass());
        Map<String, Map<String, Object>> docs = new HashMap<>();
        for (T item : list) {
            Object id = getIdValue(item);
            Map<String, Object> map = bean2EsMap(item);
            docs.put(String.valueOf(id), map);
        }
        return manager.recordsBatchUpdateMap(indexName, docs);
    }

    ///////////////////////////////////////////
    public <T> Page<T> search(Class<T> beanClass, SearchSourceBuilder builder) throws IOException {
        String indexName = getIndex(beanClass);
        Page<Map<String, Object>> rpage = manager.searchAsMap(indexName, builder);
        List<T> list = new ArrayList<>();
        for (Map<String, Object> item : rpage.getList()) {
            T bean = esMap2Bean(item, beanClass);
            list.add(bean);
        }
        Page<T> ret = new Page<>();
        ret.setTotal(rpage.getTotal());
        ret.setList(list);
        return ret;
    }

    public <T> Page<T> searchAll(Class<T> beanClass) throws IOException {
        String indexName = getIndex(beanClass);
        Page<Map<String, Object>> rpage = manager.searchAllAsMap(indexName);
        List<T> list = new ArrayList<>();
        for (Map<String, Object> item : rpage.getList()) {
            T bean = esMap2Bean(item, beanClass);
            list.add(bean);
        }
        Page<T> ret = new Page<>();
        ret.setTotal(rpage.getTotal());
        ret.setList(list);
        return ret;
    }

    public EsQuery query() {
        return EsQuery.queryBean(this);
    }


}
