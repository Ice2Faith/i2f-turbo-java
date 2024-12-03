-- table ddl

-- -------------------------------------------------
-- 1.1 :   XXL_JOB_GROUP

-- drop table if exists XXL_JOB_GROUP;

create table XXL_JOB_GROUP
(
    ID           BIGINT ( 64 ) not null comment 'ID',
    APP_NAME     VARCHAR(64) not null comment '执行器appname',
    TITLE        VARCHAR(64) not null comment '执行器名称',
    ADDRESS_TYPE INTEGER ( 32 ) default 0 not null comment '执行器地址类型：0=自动注册、1=手动录入',
    ADDRESS_LIST VARCHAR(1000000000) comment '执行器地址列表，多地址逗号分隔',
    UPDATE_TIME  TIMESTAMP(26, 6
) comment '更新时间'

     , primary key ( ID ) 
) comment '任务组';

create unique index PRIMARY_KEY_B
    on XXL_JOB_GROUP (ID);



-- -------------------------------------------------
-- 1.2 :   XXL_JOB_INFO

-- drop table if exists XXL_JOB_INFO;

create table XXL_JOB_INFO
(
    ID        BIGINT ( 64 ) not null comment 'ID',
    JOB_GROUP INTEGER ( 32 ) not null comment '执行器主键id',
    JOB_DESC  VARCHAR(255) not null comment '任务降序',
    ADD_TIME  TIMESTAMP(26, 6
) comment '加时间' ,
         UPDATE_TIME TIMESTAMP ( 26 ,6 ) comment '更新时间' , 
         AUTHOR VARCHAR ( 64 ) comment '作者' , 
         ALARM_EMAIL VARCHAR ( 255 ) comment '报警邮件' , 
         SCHEDULE_TYPE VARCHAR ( 50 ) default 'NONE' not null comment '调度类型' , 
         SCHEDULE_CONF VARCHAR ( 128 ) comment '调度配置，值含义取决于调度类型' , 
         MISFIRE_STRATEGY VARCHAR ( 50 ) default 'DO_NOTHING' not null comment '调度过期策略' , 
         EXECUTOR_ROUTE_STRATEGY VARCHAR ( 50 ) comment '执行器路由策略' , 
         EXECUTOR_HANDLER VARCHAR ( 255 ) comment '执行器任务handler' , 
         EXECUTOR_PARAM VARCHAR ( 512 ) comment '执行器任务参数' , 
         EXECUTOR_BLOCK_STRATEGY VARCHAR ( 50 ) comment '阻塞处理策略' , 
         EXECUTOR_TIMEOUT INTEGER ( 32 ) default 0 not null comment '任务执行超时时间，单位秒' , 
         EXECUTOR_FAIL_RETRY_COUNT INTEGER ( 32 ) default 0 not null comment '失败重试次数' , 
         GLUE_TYPE VARCHAR ( 50 ) not null comment 'glue类型' , 
         GLUE_SOURCE VARCHAR ( 1000000000 ) comment 'glue源代码' , 
         GLUE_REMARK VARCHAR ( 128 ) comment '胶水备注' , 
         GLUE_UPDATETIME TIMESTAMP ( 26 ,6 ) comment 'glue更新时间' , 
         CHILD_JOBID VARCHAR ( 255 ) comment '子任务id，多个逗号分隔' , 
         TRIGGER_STATUS INTEGER ( 32 ) default 0 not null comment '调度状态：0-停止，1-运行' , 
         TRIGGER_LAST_TIME BIGINT ( 64 ) default 0 not null comment '上次调度时间' , 
         TRIGGER_NEXT_TIME BIGINT ( 64 ) default 0 not null comment '下次调度时间' 

     , primary key ( ID ) 
) comment 'XXL任务信息';

create unique index PRIMARY_KEY_2
    on XXL_JOB_INFO (ID);



-- -------------------------------------------------
-- 1.3 :   XXL_JOB_LOCK

-- drop table if exists XXL_JOB_LOCK;

create table XXL_JOB_LOCK
(
    LOCK_NAME VARCHAR(50) not null comment '锁名称',
    primary key (LOCK_NAME)
) comment 'XXL任务锁';

create unique index PRIMARY_KEY_2F02
    on XXL_JOB_LOCK (LOCK_NAME);



-- -------------------------------------------------
-- 1.4 :   XXL_JOB_LOG

-- drop table if exists XXL_JOB_LOG;

create table XXL_JOB_LOG
(
    ID                        BIGINT ( 64 ) not null comment 'ID',
    JOB_GROUP                 INTEGER ( 32 ) not null comment '执行器主键id',
    JOB_ID                    INTEGER ( 32 ) not null comment '任务，主键id',
    EXECUTOR_ADDRESS          VARCHAR(255) comment '执行器地址，本次执行的地址',
    EXECUTOR_HANDLER          VARCHAR(255) comment '执行器任务handler',
    EXECUTOR_PARAM            VARCHAR(512) comment '执行器任务参数',
    EXECUTOR_SHARDING_PARAM   VARCHAR(20) comment '执行器任务分片参数，格式如 1/2',
    EXECUTOR_FAIL_RETRY_COUNT INTEGER ( 32 ) default 0 not null comment '失败重试次数',
    TRIGGER_TIME              TIMESTAMP(26, 6
) comment '调度-时间' ,
         TRIGGER_CODE INTEGER ( 32 ) not null comment '调度-结果' , 
         TRIGGER_MSG VARCHAR ( 1000000000 ) comment '调度-日志' , 
         HANDLE_TIME TIMESTAMP ( 26 ,6 ) comment '执行-时间' , 
         HANDLE_CODE INTEGER ( 32 ) not null comment '执行-状态' , 
         HANDLE_MSG VARCHAR ( 1000000000 ) comment '执行-日志' , 
         ALARM_STATUS INTEGER ( 32 ) default 0 not null comment '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败' 

     , primary key ( ID ) 
) comment 'XXL任务日志';

create unique index PRIMARY_KEY_8
    on XXL_JOB_LOG (ID);



create index IDX_XXL_JOB_LOG_HANDLE_CODE
    on XXL_JOB_LOG (HANDLE_CODE);
create index IDX_XXL_JOB_LOG_TRIGGER_TIME
    on XXL_JOB_LOG (TRIGGER_TIME);


-- -------------------------------------------------
-- 1.5 :   XXL_JOB_LOG_REPORT

-- drop table if exists XXL_JOB_LOG_REPORT;

create table XXL_JOB_LOG_REPORT
(
    ID          BIGINT ( 64 ) not null comment 'ID',
    TRIGGER_DAY TIMESTAMP(26, 6
) comment '调度-时间' ,
         RUNNING_COUNT INTEGER ( 32 ) default 0 not null comment '运行中-日志数量' , 
         SUC_COUNT INTEGER ( 32 ) default 0 not null comment '执行成功-日志数量' , 
         FAIL_COUNT INTEGER ( 32 ) default 0 not null comment '执行失败-日志数量' , 
         UPDATE_TIME TIMESTAMP ( 26 ,6 ) comment '更新时间' 

     , primary key ( ID ) 
) comment 'XXL任务日志报表';

create unique index PRIMARY_KEY_5
    on XXL_JOB_LOG_REPORT (ID);



create index IDX_XXL_JOB_LOG_REPORT_TRIGGER_DAY
    on XXL_JOB_LOG_REPORT (TRIGGER_DAY);


-- -------------------------------------------------
-- 1.6 :   XXL_JOB_LOGGLUE

-- drop table if exists XXL_JOB_LOGGLUE;

create table XXL_JOB_LOGGLUE
(
    ID          BIGINT ( 64 ) not null comment 'ID',
    JOB_ID      INTEGER ( 32 ) not null comment '任务，主键id',
    GLUE_TYPE   VARCHAR(50) comment 'glue类型',
    GLUE_SOURCE VARCHAR(1000000000) comment 'glue源代码',
    GLUE_REMARK VARCHAR(128) not null comment 'glue备注',
    ADD_TIME    TIMESTAMP(26, 6
) comment '加时间' ,
         UPDATE_TIME TIMESTAMP ( 26 ,6 ) comment '更新时间' 

     , primary key ( ID ) 
) comment 'XXL任务logglue';

create unique index PRIMARY_KEY_A
    on XXL_JOB_LOGGLUE (ID);



-- -------------------------------------------------
-- 1.7 :   XXL_JOB_REGISTRY

-- drop table if exists XXL_JOB_REGISTRY;

create table XXL_JOB_REGISTRY
(
    ID             BIGINT ( 64 ) not null comment 'ID',
    REGISTRY_GROUP VARCHAR(50)  not null comment '注册组',
    REGISTRY_KEY   VARCHAR(255) not null comment '注册键',
    REGISTRY_VALUE VARCHAR(255) not null comment '注册值',
    UPDATE_TIME    TIMESTAMP(26, 6
) comment '更新时间'

     , primary key ( ID ) 
) comment 'XXL任务注册';

create unique index PRIMARY_KEY_2F
    on XXL_JOB_REGISTRY (ID);



create index IDX_XXL_JOB_REGISTRY_G_K_V
    on XXL_JOB_REGISTRY (REGISTRY_GROUP, REGISTRY_KEY, REGISTRY_VALUE);


-- -------------------------------------------------
-- 1.8 :   XXL_JOB_USER

-- drop table if exists XXL_JOB_USER;

create table XXL_JOB_USER
(
    ID         BIGINT ( 64 ) not null comment 'ID',
    USERNAME   VARCHAR(50)  not null comment '账号',
    PASSWORD   VARCHAR(300) not null comment '密码',
    ROLE       INTEGER ( 32 ) not null comment '角色：0-普通用户、1-管理员',
    PERMISSION VARCHAR(255) comment '权限：执行器id列表，多个逗号分割',
    primary key (ID)
) comment 'XXL任务用户';

create unique index IDX_XXL_JOB_USER_USERNAME
    on XXL_JOB_USER (USERNAME);

create unique index PRIMARY_KEY_2F0
    on XXL_JOB_USER (ID);



    

