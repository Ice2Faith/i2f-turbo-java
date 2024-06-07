package i2f.jdbc.proxy.test;

import i2f.bql.test.SysUser;
import i2f.jdbc.proxy.basemapper.BaseMapper;

/**
 * @author Ice2Faith
 * @date 2024/6/7 10:10
 * @desc
 */
public interface ExtendMapper extends BaseMapper<SysUser> {
    SysUser extend();
}
