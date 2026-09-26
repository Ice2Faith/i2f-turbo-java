---
name: docx_read_write
description: 帮助进行word文档(docx)的读写编辑操作
version: 1.0
author: Ice2Faith
---

# word 文档读写操作

- 通过使用 python 的 python-docx 库实现word文档的读写编辑操作
- 例如读取内容、写入内容等

## 前置依赖

- 检测python环境

```shell
python --version
```

- 如果检测命令不存在，有可能是工具调用方式的问题
- 也许需要使用 `cmd /c` 或是 `sh -c` 方式调用

- 安装依赖包

```shell
pip install python-docx
```

## 职责目标

- 通过编写 python 代码调用 python-docx 库
- 完成用户目标

## 注意事项

- 内容读取时，如果内容过多，应该避免一次性读取
- 可以分多次读取，避免OOM内存溢出等意外情况

## 疑难解答

### 如果未安装 python，提示用户下载安装

- 下载地址如下

```shell
https://www.python.org/downloads/
```