package i2f.jdbc.proxy.test;

import i2f.jdbc.proxy.annotations.SqlScript;
import i2f.page.ApiPage;
import i2f.page.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/6 10:54
 * @desc
 */
public interface TestSimpleMapper extends ExtendMapper {

    @SqlScript("select count(*) from sys_user")
    int count();

    @SqlScript("select * from sys_user where id = <?id?>")
    Map<String, Object> findById(int id);

    @SqlScript("select * from sys_user where id > <?id?>")
    List<Map<String, Object>> listByGtId(int id);

    @SqlScript("select id,username from sys_user")
    Page<Map<String, Object>> page(ApiPage page);
}
