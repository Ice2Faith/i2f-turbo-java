package i2f.bql.test;

import i2f.bindsql.BindSql;
import i2f.bql.core.bean.Bql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/4/7 16:46
 * @desc
 */
public class TestLambda {

    public static void main(String[] args) {


        testSelect();

        testInsert();

        testBatchInsert();

        testBatchSelectInsert();

        testUpdate();

        testDelete();

        testCreateTable();

        testCreateIndex();

        testLambda();

        testBeanQuery();

        testBeanUpdate();

        testBeanDelete();

        testMapOperation();

        System.out.println("ok");
    }


    public static String location(StackTraceElement[] trace) {
        return trace[1].getClassName() + "." + trace[1].getMethodName() + ":" + trace[1].getLineNumber();
    }

    public static void testMapOperation() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));

        BindSql bql = Bql.$bean()
                .$mapDelete("sys_user",
                        Bql.$valueMap()
                                .put("status", 0)
                                .put("del_flag", 1)
                                .get(),
                        Bql.$colList()
                                .add("username")
                                .get(),
                        Bql.$colList()
                                .add("nickname")
                                .get()
                ).$$();
        System.out.println(bql);

        bql = Bql.$bean()
                .$lambdaDelete(SysUser.class,
                        Bql.$valueMapLambda()
                                .put(Bql.$lm(SysUser::getStatus), 0)
                                .put(Bql.$lm(SysUser::getDelFlag), 1)
                                .get(),
                        Bql.$colListLambda()
                                .add(Bql.$lm(SysUser::getUserName))
                                .get(),
                        Bql.$colListLambda()
                                .add(Bql.$lm(SysUser::getNickName))
                                .get()
                ).$$();
        System.out.println(bql);
    }

    public static void testBeanDelete() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        SysUser user = new SysUser();
        user.setAge(24);
        user.setDelFlag(0);
        user.setStatus(1);

        BindSql bql = Bql.$bean()
                .$beanDelete(user)
                .$$();
        System.out.println(bql);

        bql = Bql.$bean()
                .$beanDelete(user, Bql.$ls(
                        Bql.$lm(SysUser::getRoleId)
                ))
                .$$();
        System.out.println(bql);
    }

    public static void testBeanUpdate() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        SysUser user = new SysUser();
        user.setAge(24);
        user.setDelFlag(0);
        user.setStatus(1);

        SysUser cond = new SysUser();
        cond.setId(101L);

        BindSql bql = Bql.$bean()
                .$beanUpdate(user, cond)
                .$$();
        System.out.println(bql);

        bql = Bql.$bean()
                .$beanUpdate(user, cond,
                        Bql.$ls(Bql.$lm(SysUser::getAge)))
                .$$();
        System.out.println(bql);
    }

    public static void testBeanQuery() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        SysUser user = new SysUser();
        user.setAge(24);
        user.setDelFlag(0);
        user.setStatus(1);

        BindSql bql = Bql.$bean()
                .$beanQuery(user)
                .$$();

        System.out.println(bql);

        bql = Bql.$bean()
                .$beanQuery(user, "su")
                .$$();

        System.out.println(bql);

        bql = Bql.$bean()
                .$beanQuery(user, Bql.$ls(
                        Bql.$lm(SysUser::getId),
                        Bql.$lm(SysUser::getUserName),
                        Bql.$lm(SysUser::getNickName)
                ))
                .$$();

        System.out.println(bql);

        bql = Bql.$bean()
                .$beanQuery(user, null, null, Bql.$ls(
                        Bql.$lm(SysUser::getId),
                        Bql.$lm(SysUser::getUserName),
                        Bql.$lm(SysUser::getNickName)
                ))
                .$$();

        System.out.println(bql);

        bql = Bql.$bean()
                .$beanQuery(user, "su", Bql.$ls(
                        Bql.$lm(SysUser::getId),
                        Bql.$lm(SysUser::getUserName),
                        Bql.$lm(SysUser::getNickName)
                ))
                .$$();

        System.out.println(bql);

        bql = Bql.$bean()
                .$beanQuery(user, "su",
                        null, null,
                        Bql.$ls(Bql.$lm(SysUser::getUpdateTime)),
                        Bql.$ls(Bql.$lm(SysUser::getCreateTime)),
                        Bql.$ls(Bql.$lm(SysUser::getUserName))
                )
                .$$();

        System.out.println(bql);
    }

    public static void testLambda() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = Bql.$lambda()
                .$select(() -> Bql.$lambda().$sepComma()
                        .$col(SysUser::getUserName)
                        .$col(SysUser::getAge)
                )
                .$from(SysUser.class)
                .$where(() -> Bql.$lambda()
                        .$eq(SysUser::getUserName, "admin")
                        .$eq(SysUser::getStatus, 1)
                        .$gte(SysUser::getAge, 18)
                )
                .$$();
        System.out.println(bql);
    }

    public static void testCreateIndex() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = Bql.$_()
                .create().unique().index().$("idx_sys_user_status_del")
                .on().$("sys_user").$bracket(() -> Bql.$_().$sepComma()
                        .$col("status")
                        .$col("del_flag")
                )
                .$$();
        System.out.println(bql);

        bql = Bql.$lambda()
                .create().unique().index().$("idx_sys_user_status_del")
                .on().$(SysUser.class).$bracket(() -> Bql.$lambda().$sepComma()
                        .$col(SysUser::getStatus)
                        .$col(SysUser::getDelFlag)
                )
                .$$();
        System.out.println(bql);
    }

    public static void testCreateTable() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = Bql.$_()
                .create().$table("sys_user")
                .$bracket(() -> Bql.$_().$sepComma()
                        .$each(() -> Bql.$_().$col("id").bigint().primary().key().$comment("ID"),
                                () -> Bql.$_().$col("role_id").bigint().$foreignKeyReferences("sys_role", "id").$comment("角色ID"),
                                () -> Bql.$_().$col("username").varchar(300).not().$null().$comment("用户名"),
                                () -> Bql.$_().$col("password").varchar(300).not().$null().$comment("密码"),
                                () -> Bql.$_().$col("status").tinyint().$default().$("1").$comment("状态：'0' 禁用，1 正常"),
                                () -> Bql.$_().$col("create_time").datetime().$default().$("now()").$comment("创建时间"),
                                () -> Bql.$_().$col("update_time").datetime().$comment("更新时间")
                        )

                ).$comment("用户表")
                .$$();

        System.out.println(bql);

        bql = Bql.$lambda()
                .create().$table(SysUser.class)
                .$bracket(() -> Bql.$_().$sepComma()
                        .$each(() -> Bql.$lambda().$col(SysUser::getId).bigint().primary().key().$comment("ID"),
                                () -> Bql.$lambda().$col(SysUser::getRoleId).bigint().$foreignKeyReferences(SysRole.class, SysRole::getId).$comment("角色ID"),
                                () -> Bql.$lambda().$col(SysUser::getUserName).varchar(300).not().$null().$comment("用户名"),
                                () -> Bql.$lambda().$col(SysUser::getPassword).varchar(300).not().$null().$comment("密码"),
                                () -> Bql.$lambda().$col(SysUser::getStatus).tinyint().$default().$("1").$comment("状态：'0' 禁用，1 正常"),
                                () -> Bql.$lambda().$col(SysUser::getCreateTime).datetime().$default().$("now()").$comment("创建时间"),
                                () -> Bql.$lambda().$col(SysUser::getUpdateTime).datetime().$comment("更新时间")
                        )

                ).$comment("用户表")
                .$$();

        System.out.println(bql);
    }

    public static void testDelete() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = Bql.$_()
                .delete().$from("sys_user")
                .$where(() -> Bql.$_()
                        .$eq("del_flag", 1)
                        .$and(() -> Bql.$_()
                                .$or()
                                .$lt("create_time", 10)
                                .$lt("update_time", 10)
                        )
                )
                .$$();

        System.out.println(bql);

        bql = Bql.$lambda()
                .delete().$from(SysUser.class)
                .$where(() -> Bql.$lambda()
                        .$eq(SysUser::getDelFlag, 1)
                        .$and(() -> Bql.$lambda()
                                .$or()
                                .$lt(SysUser::getCreateTime, 10)
                                .$lt(SysUser::setUpdateTime, 10)
                        )
                )
                .$$();

        System.out.println(bql);
    }

    public static void testUpdate() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = Bql.$_()
                .$update("sys_user")
                .$set(() -> Bql.$_().$sepComma()
                        .$link()
                        .$eq("username", "zhang")
                        .$eq("nickname", "zhang")
                        .$eq("del_flag", 0)
                        .$eqNull("role_id")
                )
                .$where(() -> Bql.$_()
                        .$eq("id", 1)
                        .$eq("status", 1)
                )
                .$$();

        System.out.println(bql);

        bql = Bql.$lambda()
                .$update(SysUser.class)
                .$set(() -> Bql.$lambda().$sepComma()
                        .$link()
                        .$eq(SysUser::getUserName, "zhang")
                        .$eq(SysUser::getNickName, "zhang")
                        .$eq(SysUser::getDelFlag, 0)
                        .$eqNull(SysUser::getRoleId)
                )
                .$where(() -> Bql.$lambda()
                        .$eq(SysUser::getId, 1)
                        .$eq(SysUser::getStatus, 1)
                )
                .$$();

        System.out.println(bql);
    }

    public static void testBatchSelectInsert() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        List<String> names = Arrays.asList("zhang", "li", "wang");
        BindSql bql = Bql.$_()
                .insert().$into("sys_user",
                        () -> Bql.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$trim(names, Arrays.asList("union all", "union"),
                        Arrays.asList("union all", "union"),
                        null, null,
                        (list) -> Bql.$_()
                                .$for(list, " union all ", null,
                                        (i, v) -> Bql.$_()
                                                .$select(() -> Bql.$_().$sepComma()
                                                        .$("? as username", v)
                                                        .$("? as nickname", v)
                                                        .$("? as age", v + i)
                                                        .$("1 as status")
                                                        .$("now() as create_time")
                                                ).$from("dual")
                                )
                )
                .$$();

        System.out.println(bql);

        bql = Bql.$lambda()
                .insert().$into(SysUser.class,
                        () -> Bql.$lambda().$sepComma()
                                .$col(SysUser::getUserName)
                                .$col(SysUser::getNickName)
                                .$col(SysUser::getAge)
                                .$col(SysUser::getStatus)
                                .$col(SysUser::setCreateTime)
                )
                .$trim(names, Arrays.asList("union all", "union"),
                        Arrays.asList("union all", "union"),
                        null, null,
                        (list) -> Bql.$_()
                                .$for(list, " union all ", null,
                                        (i, v) -> Bql.$_()
                                                .$select(() -> Bql.$lambda().$sepComma()
                                                        .$colAs("?", SysUser::getUserName, v)
                                                        .$colAs("?", SysUser::getNickName, v)
                                                        .$colAs("?", SysUser::getAge, v + i)
                                                        .$colAs("1", SysUser::getStatus)
                                                        .$colAs("now()", SysUser::getCreateTime)
                                                ).$fromDual()
                                )
                )
                .$$();

        System.out.println(bql);

        bql = Bql.$_()
                .insert().$into("sys_user",
                        () -> Bql.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$forUnionAll(names, null, (i, v) -> Bql.$_()
                        .$select(() -> Bql.$_().$sepComma()
                                .$("? as username", v)
                                .$("? as nickname", v)
                                .$("? as age", v + i)
                                .$("1 as status")
                                .$("now() as create_time")
                        ).$from("dual")
                )
                .$$();

        System.out.println(bql);

        bql = Bql.$lambda()
                .insert().$into(SysUser.class,
                        () -> Bql.$lambda().$sepComma()
                                .$col(SysUser::getUserName)
                                .$col(SysUser::getNickName)
                                .$col(SysUser::getAge)
                                .$col(SysUser::getStatus)
                                .$col(SysUser::setCreateTime)
                )
                .$forUnionAll(names, null, (i, v) -> Bql.$_()
                        .$select(() -> Bql.$lambda().$sepComma()
                                .$colAs("?", SysUser::getUserName, v)
                                .$colAs("?", SysUser::getNickName, v)
                                .$colAs("?", SysUser::getAge, v + i)
                                .$colAs("1", SysUser::getStatus)
                                .$colAs("now()", SysUser::getCreateTime)
                        ).$fromDual()
                )
                .$$();

        System.out.println(bql);
    }

    public static void testBatchInsert() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        List<String> names = Arrays.asList("zhang", "li", "wang");
        BindSql bql = Bql.$_()
                .insert().$into("sys_user",
                        () -> Bql.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$trim(names, Arrays.asList(","),
                        Arrays.asList(","),
                        "values", null,
                        (list) -> Bql.$_().
                                $for(list, ",", null, (i, v) -> Bql.$_()
                                        .$bracket(() -> Bql.$_().$sepComma()
                                                .$("?", v)
                                                .$("?", v)
                                                .$("?", 24 + i)
                                                .$("?", 1)
                                                .$("now()")
                                        )
                                )
                )
                .$$();

        System.out.println(bql);

        bql = Bql.$_()
                .insert().$into("sys_user",
                        () -> Bql.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$trim(names, Arrays.asList(","),
                        Arrays.asList(","),
                        "values", null,
                        (list) -> Bql.$_().
                                $for(list, ",", null, (i, v) -> Bql.$_()
                                        .$bracket(() -> Bql.$_().$sepComma()
                                                .$("?", v)
                                                .$("?", v)
                                                .$("?", 24 + i)
                                                .$("?", 1)
                                                .$("now()")
                                        )
                                )
                )
                .$$();

        System.out.println(bql);

        bql = Bql.$lambda()
                .insert().$into(SysUser.class,
                        () -> Bql.$lambda().$sepComma()
                                .$col(SysUser::getUserName)
                                .$col(SysUser::getNickName)
                                .$col(SysUser::getAge)
                                .$col(SysUser::getStatus)
                                .$col(SysUser::getCreateTime)
                )
                .$trim(names, Arrays.asList(","),
                        Arrays.asList(","),
                        "values", null,
                        (list) -> Bql.$_().
                                $for(list, ",", null, (i, v) -> Bql.$_()
                                        .$bracket(() -> Bql.$_().$sepComma()
                                                .$("?", v)
                                                .$("?", v)
                                                .$("?", 24 + i)
                                                .$("?", 1)
                                                .$("now()")
                                        )
                                )
                )
                .$$();

        System.out.println(bql);
    }

    public static void testInsert() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = Bql.$_()
                .insert().$into("sys_user",
                        () -> Bql.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$values(() -> Bql.$_().$sepComma()
                        .$("?", "zhang")
                        .$("?", "zhang")
                        .$("?", 24)
                        .$("?", 1)
                        .$("now()")
                )
                .$$();

        System.out.println(bql);

        bql = Bql.$_()
                .insert().$into("sys_user",
                        () -> Bql.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$values(() -> Bql.$_().$sepComma()
                        .$var("zhang")
                        .$var("zhang")
                        .$var(24)
                        .$var(1)
                        .$("now()")
                )
                .$$();

        System.out.println(bql);

        bql = Bql.$lambda()
                .insert().$into(SysUser.class,
                        () -> Bql.$lambda().$sepComma()
                                .$col(SysUser::getUserName)
                                .$col(SysUser::getNickName)
                                .$col(SysUser::getAge)
                                .$col(SysUser::getStatus)
                                .$col(SysUser::getCreateTime)
                )
                .$values(() -> Bql.$_().$sepComma()
                        .$var("zhang")
                        .$var("zhang")
                        .$var(24)
                        .$var(1)
                        .$("now()")
                )
                .$$();

        System.out.println(bql);
    }

    public static void testSelect() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        Map<String, Object> query = new HashMap<>();
        query.put("username", "zhang");
        query.put("age", 24);

        BindSql bql = Bql.$_()
                .$select(() -> Bql.$_().$sepComma()
                        .$alias("su")
                        .$col("*")
                        .$alias("d1")
                        .$col("dict_text", "status_desc")
                        .$col(() -> Bql.$_()
                                        .$select(() -> Bql.$_().$col("name"))
                                        .$from("sys_role", "r")
                                        .$where(() -> Bql.$_()
                                                .$cond("r.id=su.role_id")
                                        )
                                , "role_name")
                        .$col(() -> Bql.$_()
                                        .$case().when().$("su.del_flag=1").then().$var("删除")
                                        .when().$("su.del_flag=0").then().$var("正常")
                                        .end()
                                , "del_flag_desc")
                )
                .$from("sys_user", "su")
                .left().$join(() -> Bql.$_()
                                .$select(() -> Bql.$_().$sepComma()
                                        .$col("dict_value")
                                        .$col("dict_text")
                                )
                                .$from("sys_dict")
                                .$where(() -> Bql.$_()
                                        .$eq("dict_type", "user_status")
                                )
                        , "d1").$on(() -> Bql.$_()
                        .$cond("su.status=d1.dict_value")
                )
                .$where(query, post -> post != null && post.size() > 0,
                        post -> Bql.$_()
                                .$and((String) post.get("username"), v -> v != null && !"".equals(v), v -> Bql.$_("su.username=?", v))
                                .$and((Integer) post.get("age"), v -> v != null && v > 0, v -> Bql.$_("su.age>=?", v))
                                .$alias("su").$and()
                                .$eq("age", (Integer) post.get("age"))
                                .$like("username", "wang")
                                .$neq("age", 12)
                                .$ne("age", 14)
                                .$instr("nickname", "admin")
                                .$and(1, v -> Bql.$_().$or().$alias("su")
                                        .$lt("age", 35)
                                        .$gte("age", 18)
                                        .$or(() -> Bql.$_()
                                                .$alias("su")
                                                .$isNotNull("status")
                                                .$isNull("del_flag"))
                                )

                                .$in("grade", Arrays.asList(6, 4, 8, 9, 10))
                                .$exists(() -> Bql.$_()
                                        .$select(() -> Bql.$_().$sepComma()
                                                .$alias("p")
                                                .$col("dict_value")
                                                .$col("dict_text")

                                        ).$("from sys_dict p")
                                        .$where(() -> Bql.$_()
                                                .$and()
                                                .$eq("dict_type", "user_status")
                                                .$cond(() -> Bql.$_().$alias("p").$col("dict_type").$("= su.status"))
                                        )

                                )
                )
                .$$();

        System.out.println(bql);

        bql = Bql.$lambda()
                .$select(() -> Bql.$lambda().$sepComma()
                        .$alias("su")
                        .$col("*")
                        .$alias("d1")
                        .$col(SysDict::getDictText, "status_desc")
                        .$col(() -> Bql.$lambda()
                                        .$select(() -> Bql.$lambda().$col(SysRole::getName))
                                        .$from(SysRole.class, "r")
                                        .$where(() -> Bql.$_()
                                                .$cond("r.id=su.role_id")
                                        )
                                , "role_name")
                        .$col(() -> Bql.$_()
                                        .$case().when().$("su.del_flag=1").then().$var("删除")
                                        .when().$("su.del_flag=0").then().$var("正常")
                                        .end()
                                , "del_flag_desc")
                )
                .$from(SysUser.class, "su")
                .left().$join(() -> Bql.$lambda()
                                .$select(() -> Bql.$lambda().$sepComma()
                                        .$col(SysDict::getDictValue)
                                        .$col(SysDict::getDictText)
                                )
                                .$from(SysDict.class)
                                .$where(() -> Bql.$lambda()
                                        .$eq(SysDict::getDictType, "user_status")
                                )
                        , "d1").$on(() -> Bql.$_()
                        .$cond("su.status=d1.dict_value")
                )
                .$where(query, post -> post != null && post.size() > 0,
                        post -> Bql.$lambda()
                                .$and((String) post.get("username"), v -> v != null && !"".equals(v), v -> Bql.$_("su.username=?", v))
                                .$and((Integer) post.get("age"), v -> v != null && v > 0, v -> Bql.$_("su.age>=?", v))
                                .$alias("su").$and()
                                .$eq(SysUser::getAge, (Integer) post.get("age"))
                                .$like(SysUser::getUserName, "wang")
                                .$neq(SysUser::getAge, 12)
                                .$ne(SysUser::getAge, 14)
                                .$instr(SysUser::getNickName, "admin")
                                .$and(1, v -> Bql.$lambda().$or().$alias("su")
                                        .$lt(SysUser::getAge, 35)
                                        .$gte(SysUser::getAge, 18)
                                        .$or(() -> Bql.$lambda()
                                                .$alias("su")
                                                .$isNotNull(SysUser::getStatus)
                                                .$isNull(SysUser::getDelFlag))
                                )

                                .$in(SysUser::getGrade, Arrays.asList(6, 4, 8, 9, 10))
                                .$exists(() -> Bql.$_()
                                        .$select(() -> Bql.$lambda().$sepComma()
                                                .$alias("p")
                                                .$col(SysDict::getDictValue)
                                                .$col(SysDict::getDictText)

                                        ).$("from sys_dict p")
                                        .$where(() -> Bql.$lambda()
                                                .$and()
                                                .$eq(SysDict::getDictType, "user_status")
                                                .$cond(() -> Bql.$lambda().$alias("p").$col(SysDict::getDictType).$("= su.status"))
                                        )

                                )
                )
                .$$();

        System.out.println(bql);
    }

    public static void testExecute(Connection conn, BindSql bql) throws SQLException {
        PreparedStatement stat = conn.prepareStatement(bql.getSql());
        List<Object> args = bql.getArgs();
        int idx = 1;
        for (Object arg : args) {
            stat.setObject(idx, arg);
            idx++;
        }
//        int num = stat.executeUpdate();
        ResultSet rs = stat.executeQuery();

        rs.close();
        stat.close();
    }
}
