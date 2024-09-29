create table i2f_log
(
    id          bigint primary key,
    application varchar(300),
    host        varchar(300),
    location    varchar(800),
    level       int,
    date        datetime,
    msg         blob,
    is_ex       int,
    ex_msg      blob,
    ex_trace    blob,
    thread_name varchar(300),
    thread_id   long,
    class_name  varchar(800),
    method_name varchar(300),
    file_name   varchar(300),
    line_number int,
    trace_id    varchar(300)
);

insert into i2f_log
(id, application, host, location, level, date, msg, is_ex, ex_msg, ex_trace, thread_name, thread_id, class_name,
 method_name, file_name, line_number, trace_id)
values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

