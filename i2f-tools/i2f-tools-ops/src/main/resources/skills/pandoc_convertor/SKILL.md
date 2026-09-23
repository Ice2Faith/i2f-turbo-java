---
name: pandoc_convertor
description: 帮助通过使用pandoc命令行进行文档格式转换
version: 1.0
author: Ice2Faith
---

# pandoc 文档格式转换

- 通过使用 pandoc 命令行工具，实现文档格式的转换
- 例如 markdown 转 word 等

## 前置依赖

- 检测pandoc环境

```shell
pandoc -v
```

- 如果检测命令不存在，有可能是工具调用方式的问题
- 也许需要使用 `cmd /c` 或是 `sh -c` 方式调用

### 如果环境没有安装
- 可以尝试直接使用 python 进行安装与操作

- 检测python环境

```shell
python --version
```

- 安装依赖包

```shell
pip install pypandoc
```

- 直接安装二进制包

```shell
pip install pypandoc-binary
```

## 职责目标

- 通过命令行调用 pandoc
- 或者，通过编写 python 代码调用 pypandoc 库
- 完成用户目标

## 疑难解答

### 如果未安装 pandoc，提示用户下载安装

- 指导下载安装于环境变量配置
- 下载地址如下

```shell
https://pandoc.org/installing.html
```

### 如果未安装 python，提示用户下载安装
- 下载地址如下

```shell
https://www.python.org/downloads/
```