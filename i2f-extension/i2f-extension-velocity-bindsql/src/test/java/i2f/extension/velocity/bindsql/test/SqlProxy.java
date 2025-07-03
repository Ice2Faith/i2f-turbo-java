package i2f.extension.velocity.bindsql.test;

import i2f.bindsql.BindSql;
import i2f.extension.velocity.bindsql.VelocitySqlGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/6/1 17:34
 * @desc
 */
public class SqlProxy {

    public static interface MapperApi {
        BindSql query(String table, Map<String, Object> map);
    }

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(".\\i2f-extension\\i2f-velocity-bindsql\\src\\main\\java\\i2f\\velocity\\bindsql\\test\\test.xml.vm");

        // 读取XML中的节点数据
        Map<String, String> sqlMap = new LinkedHashMap<>();
        NodeList nodeList = document.getElementsByTagName("sql");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            Node idNode = item.getAttributes().getNamedItem("id");
            if (idNode == null) {
                continue;
            }
            String id = idNode.getTextContent();
            String body = item.getTextContent();
            sqlMap.put(id, body);
        }


        MapperApi mapperApi = proxy(MapperApi.class, sqlMap);

        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("username", "zhang");
        map.put("age", 12);
        BindSql bql = mapperApi.query("sys_user", map);

        System.out.println(bql);

        System.out.println("ok");
    }

    public static <T> T proxy(Class<T> clazz, Map<String, String> templateMap) {
        Object ret = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Class<?> clazz = method.getDeclaringClass();
                String methodId = clazz.getName().replaceAll("\\$", ".") + "." + method.getName();

                Parameter[] parameters = method.getParameters();
                String[] names = new String[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    names[i] = parameters[i].getName();
                }
                Map<String, Object> params = new HashMap<>();
                for (int i = 0; i < names.length; i++) {
                    params.put(names[i], args[i]);
                }

                String template = templateMap.get(methodId);
                BindSql bindSql = VelocitySqlGenerator.renderSql(template, params);

                // 实际执行SQL查询与返回值封装
//                Object ret=executeSql(bindSql,method);
//                return ret;

                return bindSql;
            }
        });
        return (T) ret;
    }

    public static Object executeSql(BindSql bindSql, Method method) throws Exception {
        // 执行SQL语句
        Connection conn = null;
        PreparedStatement stat = conn.prepareStatement(bindSql.getSql());
        int idx = 1;
        for (Object value : bindSql.getArgs()) {
            stat.setObject(idx, value);
            idx++;
        }
        // 实际情况应该区分是否是查询类
        if (bindSql.getType() == BindSql.Type.UPDATE) {
            int ret = stat.executeUpdate();
            stat.close();
            return ret;
        }
        ResultSet rs = stat.executeQuery();

        // TODO 实际情况可能有如下几种情况，单一返回值，返回bean、Map；集合返回值；执行查询但无返回值或者int返回值
        // TODO 这里，只演示最复杂的返回集合的情况的处理方式
        // TODO 注意，这里的处理方式是不完全的，仅提供大体的思路和方向
        // 一般查询类语句，返回多条记录，都使用集合来返回
        // 所以，返回值就是一个泛型集合
        Type returnType = method.getGenericReturnType();
        ParameterizedType parameterizedType = (ParameterizedType) returnType;
        Class<?> collectionClass = (Class<?>) parameterizedType.getRawType();
        // 而集合中元素的类型，就需要得到真实的元素类型，一般是一个bean类型
        Type typeArgument = parameterizedType.getActualTypeArguments()[0];
        Class<?> beanClass = null;
        if (typeArgument instanceof ParameterizedType) {
            beanClass = (Class<?>) ((ParameterizedType) typeArgument).getRawType();
        } else {
            beanClass = (Class<?>) typeArgument;
        }

        // 解析 ResultSet 为 returnType 类型返回
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<String> columnNames = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            columnNames.add(metaData.getColumnLabel(i + 1));
        }
        Collection list = null;
        // 根据集合类型，实例化集合
        // 判断实际的集合类型可否直接实例化，不行的话默认按照List进行实例化集合
        try {
            list = (Collection) collectionClass.newInstance();
        } catch (Exception e) {
            list = new LinkedList();
        }
        // 判断是否是Map类型
        boolean isMapBean = (Map.class.isAssignableFrom(beanClass));
        while (rs.next()) {
            Object bean = isMapBean ? new LinkedHashMap<>() : beanClass.newInstance();
            for (int i = 0; i < columnCount; i++) {
                String columnName = columnNames.get(i);
                Object value = rs.getObject(i + 1);
                if (isMapBean) {
                    ((Map) bean).put(columnName, value);
                } else {
                    Field field = beanClass.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(bean, value);
                }
            }
            list.add(bean);
        }

        rs.close();
        stat.close();

        return list;
    }
}
