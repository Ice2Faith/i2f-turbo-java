package i2f.bindsql;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2026/10/12 11:02
 * @desc
 */
public class BindSqlTest {
    public static void main(String[] args) {
        /*language=sql*/
        BindSql add = BindSql.of()
                .select(s -> s
                        .add("a.*")
                        .add("/* ? */ ,' ? '' ?'  -- ? \n  as text")
                )
                .from().add("sys_user ").add("a")
                .where(q ->
                        q.and(s -> s.add("a.user_id").eq(1001))
                                .and(s -> s.add("a.status ").in(Arrays.asList(1, 2, 3)))
                                .choose(e -> e.add(
                                                        () -> false,
                                                        s -> s.and(v -> v.add("a.nick_name").like("123"))
                                                )
                                                .add(
                                                        s -> s.and(v -> v.add("a.user_name").eq("admin"))
                                                )
                                )
                );
        System.out.println(add);
        System.out.println("======================");

        String sql = add.toMergeSql();
        System.out.println(sql);
        System.out.println("======================");

        sql = BindSql.trimCommentSql(sql);
        System.out.println(sql);
        System.out.println("======================");

        sql = BindSql.prettySql(sql);
        System.out.println(sql);
        System.out.println("======================");

    }
}
