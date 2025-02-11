# jdbc procedure starter
- 旨在为jdbc procedure提供一个自动将springboot中的datasource，context,environment等环境信息注入到上下文
- 同时将springboot中的bean整合到执行器中参与执行的能力

## 特性
- spring的bean自动识别注册到执行器中
- 带有@JdbcProcedure注解的bean将会被当做一个具名的过程看待
- 可以使用注解指定的名称进行procedure-call直接调用
- 自动扫描配置的路径下面的所有xml文件
- 提供简化的统一的静态调用入口 JdbcProcedureHelper.call()