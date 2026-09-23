---
name: pptx_read_write
description: 帮助进行PowerPoint文档(pptx)的读写编辑操作，或通过svg/png生成ppt
version: 1.0
author: Ice2Faith
---

# word 文档读写操作

- 通过使用 python 的 python-pptx 库实现ppt文档的读写编辑操作
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
pip install python-pptx
```

## 职责目标

- 通过编写 python 代码调用 python-pptx 库
- 完成用户目标

## 可选路径

- 适用于已有svg/png文件，想要组装成为ppt
- 或者对排版要求高、但不需要后期能够二次编辑的场景
- 【注意】应该提醒用户不能二次编辑、只能使用原始svg编辑后重新生成

- 如果只有svg文件，需要先使用 svglib 将 svg 转换为 png
- 安装 svglib

```shell
pip install svglib
```

- 通过直接将svg转换为png
- 然后直接作为一个页面编辑到ppt文件中

## 疑难解答

### 如果未安装 python，提示用户下载安装
- 下载地址如下

```shell
https://www.python.org/downloads/
```