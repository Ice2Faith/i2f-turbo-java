package com.i2f.test.data.dom;

import i2f.annotations.doc.db.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author ICE2FAITH\Ice2Faith
 * @date 2024-04-24 17:15:03
 * @desc sys_user 用户表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Table("sys_user")
public class SysUserDo {

    /**
     * ID
     */
    protected Long id;

    /**
     * 登录用户名
     */
    protected String username;

    /**
     * 登录密码
     */
    protected String password;

    /**
     * 用户名
     */
    protected String nickname;

    /**
     * 年龄
     */
    protected Integer age;

    /**
     * 等级
     */
    protected Integer grade;

    /**
     * 状态：0 禁用，1 启用，99 删除
     */
    protected Integer status;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 创建人
     */
    protected String createBy;

    /**
     * 更新时间
     */
    protected Date updateTime;

    /**
     * 更新时间
     */
    protected String updateBy;


    public <T extends SysUserDo> T convert(T dom) {
        BeanUtils.copyProperties(this, dom);
        return dom;
    }

}
