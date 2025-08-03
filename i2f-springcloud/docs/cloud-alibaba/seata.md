# seata
- 同样采用CS模式，使用server作为事务协调者，实现2PC的事务管理
- 使用AT模式（auto-transactional）提供声明式事务
- 对比spring的声明式事务，使用基本一致
- @GlobalTransactional 注解来进行事务管理
## 服务端部署
- 默认文件存储信息
### 改为使用数据库
- 进入配置文件
```
cd  seata/conf
```
- 编辑file.conf
```
vi file.conf
```
- 找到模式行
```
mode = "file"
```
- 改为db
```
mode = "db"
```
- 对应找到db节点
- 修改对应的数据库连接信息即可
- 建立对应的库和表
- 数据库初始化脚本位置
```
https://github.com/seata/seata/tree/develop/script/server/db
```
- 本地已经下载保存为对应的文件
```
seata-server-init-db-mysql.sql
seata-server-init-db-oracle.sql
```


### 使用nacos的分布式服务发现能力
- 分布式事务是需要和其他服务一样
- 在注册中心中注册与发现其他服务
- 同时也有利于进行集群部署
- 实现分布式事务
- 进入配置目录
```
cd seata/conf
```
- 编辑文件
```
vi registry.conf
```
- 找到模式行
```
type = "file"
```
- 改为
```
type = "nacos"
```
- 修改对应的nacos节点的配置即可

### 初始化nacos对应的配置
- 在官网下载响应的脚本和文件
- 已下载到seata-docs中
- 修改seata-docs/config.txt
- 找到如下行
```
#Transaction storage configuration, only for the server. The file, DB, and redis configuration values are optional.
store.mode=file
store.lock.mode=file
store.session.mode=file
#Used for password encryption
store.publicKey=
```
- 更改为db模式
```
#Transaction storage configuration, only for the server. The file, DB, and redis configuration values are optional.
store.mode=db
store.lock.mode=db
store.session.mode=db
#Used for password encryption
store.publicKey=
```
- 对应db部分的信息对应修改即可
- 事务分组概念
```
#Transaction routing rules configuration, only for the client
service.vgroupMapping.default_tx_group=default
```
- 注意这里的这个分组名称default_tx_group，在客户端也会用到
- 两边需要一致
- 可以按照不同的机房来区分事务分组，以便于切换分组，实现异地容灾
```
#Transaction routing rules configuration, only for the client
service.vgroupMapping.guangzhou=default
```
- 如果改成这样，那么事务分组名称就为guangzhou
- 并且这个值必须等于注册中心中的cluster的值
- seata/conf/registry.conf-->nacos:cluster
- 将配置文件同步到nacos中
- 进入seata-docs/nacos
- 运行nacos-config.py或者nacos-config.sh脚本
- 即可将上层目录seata-docs/中的config.txt保存到nacos中
- 默认脚本中的指向为本机中默认端口的nacos
- 如果需要修改，可以修改脚本或者通过脚本参数指定

- 启动seata-server即可
- 其中有一些可选参数
- 指定端口 -p
- 指定日志 -m [file|db|redis]

## 客户端搭建
- 在客户端使用的时候，使用AT模式的情况下，客户端也需要响应的表
```
https://github.com/seata/seata/tree/develop/script/client/at/db
```
- 本地也已经下载
```
seata-at-client-init-db-mysql.sql
seata-at-client-init-db-oracle.sql
```

- 修改配置
- 修改事务分组的配置
```yaml
spring:
  cloud:
    alibaba:
      seata:
        tx-service-group: default_tx_group
```
- 这个值就是之前在服务端seata-docs/config.txt中的这个属性的名称
```
#Transaction routing rules configuration, only for the client
service.vgroupMapping.default_tx_group=default
```
- 就是紧跟service.vgroupMapping的名称，这里就是default_tx_group

- 修改配置，增加与server端的连接配置
- 注意，根配置就是seata,不要加以为在spring.cloud.alibaba.seata下面
```yaml
# 配置seata-server信息
seata:
  # 注册中心
  registry:
    type: nacos
    nacos:
      # seata-server所在的nacos主机，服务名，用户名，密码，分组
      server-addr: 127.0.0.1:8848
      application: seata-server
      username: nacos
      password: nacos
      group: SETA_GROUP
  # 配置中心
  config:
    type: nacos
    nacos:
      # 配置所在的nacos信息
      server-addr: 127.0.0.1:8848
      username: nacos
      password: nacos
      group: SETA_GROUP
```
- 在业务方法中添加事务注解即可
- @GlobalTransactional