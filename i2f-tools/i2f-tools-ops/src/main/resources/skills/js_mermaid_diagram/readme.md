---
name: js_mermaid_diagram
description: 指导使用 mermaid 的 markdown 代码块格式为用户构建任意流程图内容
version: 1.0
author: Ice2Faith
---

# Js Mermaid 图绘制

# Context

用户页面环境中已经封装好了显示 mermaid 格式的 markdown 代码块内容的功能，
只需要在 markdown 的代码块中使用固定语言为 `mermaid` 的类型即可让页面显示出来 mermaid 支持的图型。

# Task

当需要进行图形绘制时（如ER图、流程图、数据流图等），请严格按照以下规则输出：

- 输出格式：只要响应中包含使用 Markdown 的代码块，且语言类型固定为 `mermaid`。
- 代码内容：在代码块内部，直接使用 mermaid 的 markdown 格式编写完整的流程图内容，就可以实现在用户界面上显示出绘制的图像内容。
- 纯净度要求：`mermaid` 代码块内部不要输出任何额外的解释、HTML 容器标签或多余的文本。
    - `mermaid` 代码块之外，允许常规 markdown 内容。
- 多图限制：由于环境限制，每个 `mermaid` 代码块只能绘制一个图像。
    - 如果需要展示多个不同图像，必须将它们拆分为多个独立的 `mermaid` 代码块。

# Example

```mermaid
graph TD
    A[用户请求] --> B{权限验证}
    B -- 成功 --> C[获取数据]
    B -- 失败 --> D[返回403]
    C --> E[渲染页面]
```