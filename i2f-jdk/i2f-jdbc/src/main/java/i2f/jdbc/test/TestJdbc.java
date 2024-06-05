package i2f.jdbc.test;

import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.NamingOutputParameter;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/5 19:02
 * @desc
 */
public class TestJdbc {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false",
                "root", "123456");

        /**
         * DELIMITER $$
         *
         * CREATE PROCEDURE sp_test(OUT p_nickname VARCHAR(300),OUT p_age INT, p_id BIGINT, p_username VARCHAR(300))
         * BEGIN
         *     select nickname,age into p_nickname,p_age from sys_user where id=p_id and username=p_username;
         * END$$
         *
         * DELIMITER ;
         */

        Map<String, Object> nameMap = JdbcResolver.callNaming(conn,
                "{ call sp_test(?,?,?,?) }",
                Arrays.asList(
                        NamingOutputParameter.of("nickname", JDBCType.VARCHAR, "-"),
                        NamingOutputParameter.of("age", JDBCType.NUMERIC, 0),
                        1,
                        "root"
                ));


        conn.close();

        System.out.println(nameMap);

        System.out.println("ok");
    }
}
