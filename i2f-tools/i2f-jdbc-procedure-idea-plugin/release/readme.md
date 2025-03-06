# IDEA 插件 Jdbc-Procedure (IDEA plugin named Jdbc-Procedure)

- 为jdbc-procedure框架开发的JetBrains系列IDE的插件
- 适用于IDEA，PyCharm，DataGrip等JetBrains集成开发环境

- a jdbc-procedure framework plugin for JetBrains's IDE
- adapt IDEA,PyCharm,DataGrip,etc. develop tool

## 使用限制 (Dependency)

- IDEA 版本 >= 2021.1
- 其他JetBrains的IDE的构建时间应该晚于2021-03月
- 但是，你也可以试试直接安装

- IDEA version require >= 2021.1
- other JetBrains's IDE should built date after than 2021-03
- but also, you could try to install it.

## 使用教程 (How to use?)

### 安装 (Install)
- 菜单栏->文件->设置->插件->设置(小图标)->从本地磁盘安装->选择这个jar包-完成
- Menu Bar->File->Setting->Plugins->Setting(little icon)->Install Plugin from Disk...->Choice this jar file->finish

```shell
jdbc-procedure-plugin-2021.1-211.jar
```

### 检查 (Check)

- 在已安装(Installed)的插件里面搜索:jdbc
- 如果能看到[Jdbc-Procedure]插件，则安装成功
- 然后，打开你的procedure.xml文件中，查看sql-/-eval-java节点
- 看下节点是否关键字已经改变了颜色，是否会有自动补全的提示，则插件运行正常

- search in your installed plugins by : jdbc
- if you see [Jdbc-Procedure] plugin, means install success.
- then, view sql-/-eval-java xml node in you procedure.xml file
- if keywords was changed color and cloud provide auto-completion hint, means it's work normal

## 重要说明 (Notice!)

- 插件提供的能力可能不完全，以框架实际运行为准
- 插件提示的错误，也可能是没有问题的，以实际运行为准

- please run you application to test, because of plugin hint maybe wrong
- sometimes, plugin hint wrong, but actually isn't any possible when run application