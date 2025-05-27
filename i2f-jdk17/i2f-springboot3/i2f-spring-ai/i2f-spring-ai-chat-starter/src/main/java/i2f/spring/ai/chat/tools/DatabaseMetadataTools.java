package i2f.spring.ai.chat.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * test chat:
 * 给我基于系统表写一条SQL，统计前三种角色的用户数，显示角色名和用户数，回答结果只保留SQL语句，不要其他内容
 *
 * @author Ice2Faith
 * @date 2025/5/27 22:35
 * @desc
 */
public class DatabaseMetadataTools {
    @Tool(description = "get all system table names/" +
            "获取所有的系统表名称")
    public String getAllSystemTableNames() {
        return "用户表：sys_user\n角色表：sys_role";
    }

    @Tool(description = "get all system table names/" +
            "获取指定系统表表的列结构信息")
    public String getTableColumns(String tableName) {
        if ("sys_user".equals(tableName)) {
            /*language=sql*/
            return "create table sys_user (\n" +
                    "    id bigint primary key comment '主键',\n" +
                    "    username varchar(300) unique not null comment '用户名',\n" +
                    "    status tinyint default 1 comment '状态：0 禁用，1 启用',\n" +
                    "    role_id bigint comment '角色ID'\n" +
                    ") comment '用户表';";
        } else if ("sys_role".equals(tableName)) {
            /*language=sql*/
            return "create table sys_role (\n" +
                    "                          id bigint primary key comment '主键',\n" +
                    "                          name varchar(300) unique not null comment '角色名',\n" +
                    "                          status tinyint default 1 comment '状态：0 禁用，1 启用'\n" +
                    ") comment '角色表';";
        }
        return "not found table columns of :" + tableName;
    }
}
