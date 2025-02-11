# jdbc procedure
- 只在提供一个基于xml顺序执行的类似存储过程的执行引擎
- 参考mybatis的xml技术，构建在一个xml中按照xml标签定义的顺序执行各个标签的内容的执行引擎

## XML 语法规范
- 语法说明文件：procedure.xml
    - 说明了支持的语法标签和支持使用的标签以及特性
- 语法辅助定义文件：procedure.dtd
    - 仅用于在IDE编辑器中辅助编写XML脚本
    - 不参与实际的运行

## 使用示例
- 见源码包
```shell
src/main/java/i2f/jdbc/procedure/test
```