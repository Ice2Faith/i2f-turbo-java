create table limit_rule_item (
    id bigint auto_increment primary key,
    app_name varchar(300),
    limit_type varchar(300),
    type_key varchar(1024),
    limit_count int,
    limit_ttl bigint,
    limit_order int,
    status int default 1
);