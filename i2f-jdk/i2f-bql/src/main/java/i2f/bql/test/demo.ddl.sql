create table sys_user
(
    id          bigint auto_increment primary key,
    username    varchar(300) not null,
    password    varchar(300),
    role_id     bigint,
    nickname    varchar(300),
    age         int,
    grade       int,
    status      int      default 1,
    create_time datetime default now(),
    update_time datetime,
    del_flag    int      default 0
);

create table sys_role
(
    id   bigint auto_increment primary key,
    name varchar(300) not null
);

create table sys_dict
(
    id         bigint auto_increment primary key,
    dict_type  varchar(300),
    dict_value int,
    dict_text  varchar(300)
);
