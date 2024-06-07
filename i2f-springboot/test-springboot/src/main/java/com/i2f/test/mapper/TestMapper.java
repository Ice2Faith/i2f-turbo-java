package com.i2f.test.mapper;

import com.i2f.test.data.dom.SysUserDo;
import i2f.jdbc.proxy.annotations.SqlScript;
import i2f.jdbc.proxy.basemapper.BaseMapper;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/6/7 16:00
 * @desc
 */
public interface TestMapper extends BaseMapper<SysUserDo> {

    @SqlScript("select * from sys_user")
    List<SysUserDo> listAll();
}
