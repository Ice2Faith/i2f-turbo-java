package i2f.jdbc.proxy.xml.mybatis.inflater;

import i2f.bindsql.BindSql;
import i2f.bql.core.Bql;
import i2f.compiler.MemoryCompiler;
import i2f.database.type.DatabaseType;
import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;
import i2f.match.regex.RegexUtil;
import i2f.reflect.vistor.Visitor;
import org.w3c.dom.Node;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/10/9 18:43
 */
public class MybatisMapperInflater {
    public static final String DATABASE_TYPE = "databaseType";

    public static final MybatisMapperInflater INSTANCE = new MybatisMapperInflater();

    protected String databaseType;

    public MybatisMapperInflater databaseType(String databaseType) {
        this.databaseType = databaseType;
        return this;
    }

    public boolean testExpression(String expression, Map<String, Object> params) {
        try {
            expression = expression.replaceAll("\\s+and\\s+", " && ");
            expression = expression.replaceAll("\\s+or\\s+", " || ");
            expression = expression.replaceAll("\\s+gte\\s+", " >= ");
            expression = expression.replaceAll("\\s+lte\\s+", " <= ");
            expression = expression.replaceAll("\\s+gt\\s+", " > ");
            expression = expression.replaceAll("\\s+lt\\s+", " < ");
            expression = expression.replaceAll("&gt;", " > ");
            expression = expression.replaceAll("&lt;", " < ");
            expression = "return " + expression + ";";
            String additionalImports = "import " + Visitor.class.getName() + ";";
            String additionalMethods = "\n" +
                    "    public static Object eval(String expression,Object root){\n" +
                    "        return Visitor.visit(expression,root).get();\n" +
                    "    }";
            Object o = MemoryCompiler.evaluateExpression(expression, params, additionalImports, additionalMethods);
            if (o instanceof Boolean) {
                return (Boolean) o;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object evalExpression(String expression, Map<String, Object> params) {
        return Visitor.visit(expression, params).get();
    }

    public BindSql inflateSql(Connection conn, String unqId, Map<String, Object> params, Map<String, MybatisMapperNode> nodeMap) throws SQLException {
        DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
        params.put(DATABASE_TYPE, databaseType);
        return inflateSql(unqId, params, nodeMap);
    }

    public BindSql inflateSql(DatabaseType databaseType, String unqId, Map<String, Object> params, Map<String, MybatisMapperNode> nodeMap) {
        params.put(DATABASE_TYPE, databaseType);
        return inflateSql(unqId, params, nodeMap);
    }

    public BindSql inflateSql(String unqId, Map<String, Object> params, Map<String, MybatisMapperNode> nodeMap) {
        MybatisMapperNode node = nodeMap.get(unqId);
        if (node == null) {
            return new BindSql("");
        }
        return inflateSqlNode(node, params, nodeMap);
    }

    public BindSql inflateSqlNode(Connection conn, MybatisMapperNode node, Map<String, Object> params, Map<String, MybatisMapperNode> nodeMap) throws SQLException {
        DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
        params.put(DATABASE_TYPE, databaseType);
        return inflateSqlNode(node, params, nodeMap);
    }

    public BindSql inflateSqlNode(DatabaseType databaseType, MybatisMapperNode node, Map<String, Object> params, Map<String, MybatisMapperNode> nodeMap) {
        params.put(DATABASE_TYPE, databaseType);
        return inflateSqlNode(node, params, nodeMap);
    }

    public BindSql inflateSqlNode(MybatisMapperNode node, Map<String, Object> params, Map<String, MybatisMapperNode> nodeMap) {
        Object type = params.get(DATABASE_TYPE);
        if (type == null) {
            if (databaseType != null) {
                params.put(DATABASE_TYPE, databaseType);
            }
        }
        Map<String, Object> workParam = new LinkedHashMap<>(params);
        if (!node.isXmlType()) {
            if (Node.COMMENT_NODE == node.getNodeType()) {
                return new BindSql("");
            }
            String sql = node.getContent();
            return replaceParameters(sql, workParam);
        }
        List<MybatisMapperNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            return new BindSql("");
        }
        StringBuilder builder = new StringBuilder();
        List<Object> args = new ArrayList<>();
        for (MybatisMapperNode child : children) {
            if (!child.isXmlType()) {
                BindSql next = inflateSqlNode(child, params, nodeMap);
                builder.append(next.getSql());
                args.addAll(next.getArgs());
                continue;
            }
            String tagName = child.getTagName();
            if ("if".equalsIgnoreCase(tagName)) {
                String test = child.getAttributes().get("test");
                boolean testOk = testExpression(test, params);

                if (testOk) {
                    BindSql next = inflateSqlNode(child, params, nodeMap);
                    builder.append(next.getSql());
                    args.addAll(next.getArgs());
                }

                continue;
            }
            if ("dialect".equalsIgnoreCase(tagName)) {
                String databases = child.getAttributes().get("databases");

                Object databaseType = params.get(DATABASE_TYPE);
                boolean testOk = supportDatabases(databaseType, databases);

                if (testOk) {
                    BindSql next = inflateSqlNode(child, params, nodeMap);
                    builder.append(next.getSql());
                    args.addAll(next.getArgs());
                }

                continue;
            }
            if ("choose".equalsIgnoreCase(tagName)) {
                List<MybatisMapperNode> nexts = child.getChildren();
                for (MybatisMapperNode next : nexts) {
                    String nextTagName = next.getTagName();
                    if ("when".equalsIgnoreCase(nextTagName)) {
                        String test = next.getAttributes().get("test");
                        boolean testOk = testExpression(test, params);

                        if (testOk) {
                            BindSql recur = inflateSqlNode(next, params, nodeMap);
                            builder.append(recur.getSql());
                            args.addAll(recur.getArgs());
                            break;
                        }
                    }
                    if ("otherwise".equalsIgnoreCase(nextTagName)) {
                        BindSql recur = inflateSqlNode(next, params, nodeMap);
                        builder.append(recur.getSql());
                        args.addAll(recur.getArgs());
                    }
                }
                continue;
            }
            if ("dialect-choose".equalsIgnoreCase(tagName)) {
                List<MybatisMapperNode> nexts = child.getChildren();
                for (MybatisMapperNode next : nexts) {
                    String nextTagName = next.getTagName();
                    if ("dialect-when".equalsIgnoreCase(nextTagName)) {
                        String databases = next.getAttributes().get("databases");
                        Object databaseType = params.get(DATABASE_TYPE);
                        boolean testOk = supportDatabases(databaseType, databases);

                        if (testOk) {
                            BindSql recur = inflateSqlNode(next, params, nodeMap);
                            builder.append(recur.getSql());
                            args.addAll(recur.getArgs());
                            break;
                        }
                    }
                    if ("dialect-otherwise".equalsIgnoreCase(nextTagName)) {
                        BindSql recur = inflateSqlNode(next, params, nodeMap);
                        builder.append(recur.getSql());
                        args.addAll(recur.getArgs());
                    }
                }
                continue;
            }

            if ("foreach".equalsIgnoreCase(tagName)) {
                String collection = Optional.ofNullable(child.getAttributes().get("collection")).orElse("");
                String item = Optional.ofNullable(child.getAttributes().get("item")).orElse("");
                String open = Optional.ofNullable(child.getAttributes().get("open")).orElse("");
                String separator = Optional.ofNullable(child.getAttributes().get("separator")).orElse("");
                String close = Optional.ofNullable(child.getAttributes().get("close")).orElse("");

                Object col = evalExpression(collection, params);
                if (col == null) {
                    return new BindSql("");
                }
                Map<String, Object> iterParam = new LinkedHashMap<>(params);
                Class<?> clazz = col.getClass();
                if (col instanceof Iterable) {
                    Iterable<?> iter = (Iterable<?>) col;
                    boolean isFirst = true;
                    for (Object obj : iter) {
                        iterParam.put(item, obj);
                        BindSql next = inflateSqlNode(child, iterParam, nodeMap);
                        if (isFirst) {
                            builder.append(open);
                        }
                        if (!isFirst) {
                            builder.append(separator);
                        }
                        builder.append(next.getSql());
                        args.addAll(next.getArgs());
                        isFirst = false;
                    }
                    if (!isFirst) {
                        builder.append(close);
                    }
                } else if (col instanceof Iterator) {
                    Iterator<?> iter = (Iterator<?>) col;
                    boolean isFirst = true;
                    while (iter.hasNext()) {
                        Object obj = iter.next();
                        iterParam.put(item, obj);
                        BindSql next = inflateSqlNode(child, iterParam, nodeMap);
                        if (isFirst) {
                            builder.append(open);
                        }
                        if (!isFirst) {
                            builder.append(separator);
                        }
                        builder.append(next.getSql());
                        builder.append(next.getArgs());
                        isFirst = false;
                    }
                    if (!isFirst) {
                        builder.append(close);
                    }
                } else if (col instanceof Enumeration) {
                    Enumeration<?> iter = (Enumeration<?>) col;
                    boolean isFirst = true;
                    while (iter.hasMoreElements()) {
                        Object obj = iter.nextElement();
                        iterParam.put(item, obj);
                        BindSql next = inflateSqlNode(child, iterParam, nodeMap);
                        if (isFirst) {
                            builder.append(open);
                        }
                        if (!isFirst) {
                            builder.append(separator);
                        }
                        builder.append(next.getSql());
                        builder.append(next.getArgs());
                        isFirst = false;
                    }
                    if (!isFirst) {
                        builder.append(close);
                    }
                } else if (clazz.isArray()) {
                    int len = Array.getLength(col);
                    boolean isFirst = true;
                    for (int i = 0; i < len; i++) {
                        Object obj = Array.get(col, i);
                        iterParam.put(item, obj);
                        BindSql next = inflateSqlNode(child, iterParam, nodeMap);
                        if (isFirst) {
                            builder.append(open);
                        }
                        if (!isFirst) {
                            builder.append(separator);
                        }
                        builder.append(next.getSql());
                        builder.append(next.getArgs());
                        isFirst = false;
                    }
                    if (!isFirst) {
                        builder.append(close);
                    }
                }

                continue;
            }
            if ("trim".equalsIgnoreCase(tagName)) {
                String prefix = Optional.ofNullable(child.getAttributes().get("prefix")).orElse("");
                String suffix = Optional.ofNullable(child.getAttributes().get("suffix")).orElse("");
                String prefixOverrides = Optional.ofNullable(child.getAttributes().get("prefixOverrides")).orElse("");
                String suffixOverrides = Optional.ofNullable(child.getAttributes().get("suffixOverrides")).orElse("");

                BindSql next = inflateSqlNode(child, params, nodeMap);
                String sql = next.getSql();
                String str = sql.trim();
                if (!str.isEmpty()) {
                    String lstr = str.toLowerCase();
                    if (!prefixOverrides.isEmpty()) {
                        String[] arr = prefixOverrides.split("[|]");
                        for (String item : arr) {
                            item = item.trim().toLowerCase();
                            if (item.isEmpty()) {
                                continue;
                            }
                            if (lstr.startsWith(item + " ")
                                    || lstr.startsWith(item + "\t")
                                    || lstr.startsWith(item + "\r")
                                    || lstr.startsWith(item + "\n")
                            ) {
                                str = str.substring(item.length());
                            }
                        }
                    }
                    if (!suffixOverrides.isEmpty()) {
                        String[] arr = suffixOverrides.split("[|]");
                        for (String item : arr) {
                            item = item.trim().toLowerCase();
                            if (item.isEmpty()) {
                                continue;
                            }
                            if (lstr.endsWith(" " + item)
                                    || lstr.endsWith("\t" + item)
                                    || lstr.endsWith("\r" + item)
                                    || lstr.endsWith("\n" + item)
                            ) {
                                str = str.substring(0, str.length() - item.length());
                            }
                        }
                    }
                    str = str.trim();
                }
                if (!str.isEmpty()) {
                    builder.append(" ").append(prefix).append(" ");
                }
                builder.append(str);
                if (!str.isEmpty()) {
                    builder.append(" ").append(suffix).append(" ");
                }
                args.addAll(next.getArgs());
                continue;
            }
            if ("set".equalsIgnoreCase(tagName)) {
                BindSql next = inflateSqlNode(child, params, nodeMap);
                String sql = next.getSql();
                String str = sql.trim();
                if (!str.isEmpty()) {
                    builder.append(" set ");
                    String lstr = str.toLowerCase();
                    if (lstr.startsWith(",")) {
                        str = str.substring(1);
                    }
                    if (lstr.endsWith(",")) {
                        str = str.substring(0, str.length() - 1);
                    }
                }
                builder.append(str);
                args.addAll(next.getArgs());
                continue;
            }
            if ("where".equalsIgnoreCase(tagName)) {
                BindSql next = inflateSqlNode(child, params, nodeMap);
                String sql = next.getSql();
                String str = sql.trim();
                if (!str.isEmpty()) {
                    builder.append(" where ");
                    String lstr = str.toLowerCase();
                    if (lstr.startsWith("and ")
                            || lstr.startsWith("and\t")
                            || lstr.startsWith("and\r")
                            || lstr.startsWith("and\n")) {
                        str = str.substring(3);
                    }
                    if (lstr.startsWith("or ")
                            || lstr.startsWith("or\t")
                            || lstr.startsWith("or\r")
                            || lstr.startsWith("or\n")) {
                        str = str.substring(2);
                    }
                }
                builder.append(str);
                args.addAll(next.getArgs());
                continue;
            }
            if ("include".equalsIgnoreCase(tagName)) {
                String refid = Optional.ofNullable(child.getAttributes().get("refid")).orElse("");
                if (refid.isEmpty()) {
                    return new BindSql("");
                }
                if (!refid.contains(".")) {
                    refid = node.getNamespace() + "." + refid;
                }
                MybatisMapperNode includeNode = nodeMap.get(refid);
                if (includeNode == null) {
                    return new BindSql("");
                }
                BindSql next = inflateSqlNode(includeNode, params, nodeMap);
                builder.append(next.getSql());
                args.addAll(next.getArgs());
                continue;
            }
            BindSql next = inflateSqlNode(child, params, nodeMap);
            builder.append(next.getSql());
            args.addAll(next.getArgs());

        }
        return new BindSql(builder.toString(), args);
    }

    public BindSql replaceParameters(String sql, Map<String, Object> workParam) {
        List<Object> args = new ArrayList<>();
        String str = RegexUtil.regexFindAndReplace(sql, "[\\$|#]\\{\\s*[^}]+\\s*\\}", (patten) -> {
            boolean isDolar = patten.startsWith("$");
            patten = patten.substring(2, patten.length() - 1);
            String expression = patten.trim();
            if (isDolar) {
                Object obj = evalExpression(expression, workParam);
                if(obj instanceof Bql){
                    Bql<?> bql = (Bql<?>) obj;
                    BindSql ql = bql.$$();
                    args.addAll(ql.getArgs());
                    return ql.getSql();
                }
                if(obj instanceof BindSql){
                    BindSql bql = (BindSql) obj;
                    args.addAll(bql.getArgs());
                    return bql.getSql();
                }
                return obj == null ? "" : String.valueOf(obj);
            } else {
                String[] arr = expression.split(",");
                expression = arr[0];
                // TODO resolve jdbcType=,handler=,...
                Object obj = evalExpression(expression, workParam);
                if(obj instanceof Bql){
                    Bql<?> bql = (Bql<?>) obj;
                    BindSql ql = bql.$$();
                    args.addAll(ql.getArgs());
                    return ql.getSql();
                }
                if(obj instanceof BindSql){
                    BindSql bql = (BindSql) obj;
                    args.addAll(bql.getArgs());
                    return bql.getSql();
                }
                args.add(obj);
                return "?";
            }
        });
        return new BindSql(str, args);
    }

    public boolean supportDatabases(Object databaseType, String databases) {
        if (databases == null || databases.isEmpty()) {
            return true;
        }
        if (databaseType == null) {
            return false;
        }
        if (databaseType instanceof DatabaseType) {
            DatabaseType dt = (DatabaseType) databaseType;
            List<String> databaseTypes = Arrays.asList(dt.db(), String.valueOf(dt));
            return supportDatabases(databaseTypes, Arrays.asList(databases.split(",|;")));
        } else {
            List<String> databaseTypes = Arrays.asList(String.valueOf(databaseType));
            return supportDatabases(databaseTypes, Arrays.asList(databases.split(",|;")));
        }
    }

    public boolean supportDatabases(List<String> databaseTypes, List<String> databases) {
        if (databases == null || databases.isEmpty()) {
            return true;
        }
        if (databaseTypes == null || databaseTypes.isEmpty()) {
            return false;
        }
        for (String databaseType : databaseTypes) {
            if (databaseType == null || databaseType.isEmpty()) {
                continue;
            }
            for (String database : databases) {
                if (database == null || database.isEmpty()) {
                    continue;
                }
                if (databaseType.equalsIgnoreCase(database)) {
                    return true;
                }
            }
        }
        return false;
    }

}
