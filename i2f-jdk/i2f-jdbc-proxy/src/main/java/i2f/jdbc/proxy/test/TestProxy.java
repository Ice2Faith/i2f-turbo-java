package i2f.jdbc.proxy.test;

import i2f.bql.test.SysUser;
import i2f.jdbc.proxy.ProxySqlExecuteGenerator;
import i2f.jdbc.proxy.provider.impl.SimpleJdbcInvokeContextProvider;
import i2f.jdbc.proxy.provider.impl.SimpleProxyRenderSqlProvider;
import i2f.page.ApiPage;
import i2f.page.Page;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/6 10:54
 * @desc
 */
public class TestProxy {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/i2f_proj?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false",
                "root", "123456");

        TestSimpleMapper mapper = ProxySqlExecuteGenerator.proxy(TestSimpleMapper.class,
                new SimpleJdbcInvokeContextProvider(conn),
                new SimpleProxyRenderSqlProvider()
        );

        SysUser upd = new SysUser();
        upd.setAge(23);

        SysUser cond = new SysUser();
        cond.setId(1L);
        mapper.update(upd, cond);

        SysUser ins = new SysUser();
        ins.setUserName("zhang");
        ins.setAge(12);
        mapper.insert(ins);


        int cnt = mapper.count();

        Map<String, Object> map = mapper.findById(1);

        List<Map<String, Object>> list = mapper.listByGtId(0);

        Page<Map<String, Object>> page = mapper.page(new ApiPage(0, 3));

        conn.close();

        System.out.println("ok");
    }
}
