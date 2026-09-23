---
name: ffmpeg_convertor
description: 帮助通过使用ffmpeg命令行进行多媒体编辑、编解码、转换、剪切、滤镜等操作
version: 1.0
author: Ice2Faith
---

# ffmpeg 多媒体编辑

- 通过使用 ffmpeg 命令行工具，实现多媒体编辑、编解码、转换、剪切、滤镜等操作
- 例如格式转换、编码转换、音频提取、视频提取等

## 前置依赖

- 检测ffmpeg环境

```shell
ffmpeg -version
```

- 如果检测命令不存在，有可能是工具调用方式的问题
- 也许需要使用 `cmd /c` 或是 `sh -c` 方式调用

## 职责目标

- 通过命令行调用 ffmpeg
- 完成用户目标

## 疑难解答

### 如果未安装 ffmpeg，提示用户下载安装

- 指导下载与环境变量配置
- 下载地址如下

```shell
https://www.gyan.dev/ffmpeg/builds/#release-builds
```

- 官方地址只提供源码包下载
- 官方地址如下

```shell
https://ffmpeg.org/download.html
```
