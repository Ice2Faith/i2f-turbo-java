---
name: js_drawio_diagram
description: 指导使用 draw.io 的XML格式为用户构建任意流程图内容
version: 1.0
author: Ice2Faith
---

# Js Draw.io 流程图绘制

# Context

用户页面环境中已经封装好了显示 draw.io 格式的 XML 内容的功能，
只需要在 markdown 的代码块中使用固定语言为 `drawio` 的类型即可让页面显示出来流程图。

# Task

当需要进行流程图绘制时（如ER图、流程图、数据流图等），请严格按照以下规则输出：

- 输出格式：只要响应中包含使用 Markdown 的代码块，且语言类型固定为 `drawio`。
- 代码内容：在代码块内部，直接使用 drawio 的 XML 格式编写完整的流程图内容，就可以实现在用户界面上显示出绘制的图像内容。
- 纯净度要求：`drawio` 代码块内部不要输出任何额外的解释、HTML 容器标签或多余的文本。
    - `drawio` 代码块之外，允许常规 markdown 内容。
- 多图限制：由于环境限制，每个 `drawio` 代码块只能绘制一个图像。
    - 如果需要展示多个不同图像，必须将它们拆分为多个独立的 `drawio` 代码块。
- 编码要求：节点的value属性值，如果包含 `<`,`>`,`&` 字符，需要进行XML转义，保证符合XML规范。

# Example

```drawio
<mxfile host="app.diagrams.net" modified="2023-10-27T10:00:00.000Z" agent="Qwen" version="22.1.0" type="device">
    <diagram name="扫码登录流程" id="scan-login-flow">
        <mxGraphModel dx="1422" dy="762" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1"
                      page="1" pageScale="1" pageWidth="1169" pageHeight="827" math="0" shadow="0">
            <root>
                <mxCell id="0"/>
                <mxCell id="1" parent="0"/>

                <!-- PC 端节点 -->
                <mxCell id="2" value="点击扫码登录"
                        style="rounded=1;whiteSpace=wrap;fillColor=#dae8fc;strokeColor=#6c8ebf;" vertex="1" parent="1">
                    <mxGeometry x="40" y="60" width="160" height="40" as="geometry"/>
                </mxCell>
               
            </root>
        </mxGraphModel>
    </diagram>
</mxfile>
```