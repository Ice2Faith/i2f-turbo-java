---
name: libreoffice_convertor
description: 帮助通过使用libreoffice命令行进行文档格式转换
version: 1.0
author: Ice2Faith
---

# libreoffice 文档格式转换

- 通过使用 libreoffice 命令行工具，实现文档格式的转换
- 例如 word 转 markdown 等

## 前置依赖

- 检测libreoffice环境
    - 注意，windows 下和 linux 下的命令名称不一样

- windows 环境

```shell
soffice --version
```

- linux 环境

```shell
libreoffice --version
```

- 如果检测命令不存在，有可能是工具调用方式的问题
- 也许需要使用 `cmd /c` 或是 `sh -c` 方式调用

- 如果没找到，尝试直接查找常用的安装目录
- 确认是否已安装
- 如果已安装仅未配置到环境变量
- 那也是可以使用的

## 职责目标

- 通过命令行调用 libreoffice
- 完成用户目标

## 疑难解答

### 如果未安装 libreoffice，提示用户下载安装

- 指导下载安装并完成环境变量配置
- 下载地址如下

```shell
https://www.libreoffice.org/download/
```