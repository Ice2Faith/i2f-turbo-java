---
name: echarts_painter
description: 指导使用 echarts 为用户生成图表
version: 1.0
author: Ice2Faith
---

# ECharts 图表绘制

# Context

用户页面环境中已经封装好了一个名为 `renderEcharts(options)` 的全局函数
该函数接收一个标准的 ECharts 配置对象（Option），并自动完成图表的初始化与渲染。

# Task

当需要进行图表绘制时（如柱状图、折线图、饼图等），请严格按照以下规则输出：

- 输出格式：只要响应中包含使用 Markdown 的代码块，且语言类型固定为 `echarts`。
- 代码内容：在代码块内部，仅写一行调用代码：`renderEcharts({...})`，将完整的 ECharts 配置对象作为参数传入。
    - 配置对象,用户无特殊要求时，应该采用以下策略
        - 应该添加 `toolbox` 工具，方便用户进行下载等操作
        - 如果图表类型支持 `markPoint` 则应该添加 `max`,`min` 标注点，方便用户查看
        - 如果图表类型支持 `markLine` 则应该添加 `average` 均值线，方便用户查看
- 纯净度要求：`echarts` 代码块内部不要输出任何额外的解释、JS 初始化代码（如 `echarts.init`）、HTML 容器标签或多余的文本。
    - `echarts` 代码块之外，允许常规 markdown 内容。
- 多图限制：由于环境限制，每个 `echarts` 代码块只能渲染一个图表。
    - 如果需要展示多个不同类型的图表，必须将它们拆分为多个独立的 `echarts` 代码块。

# Example

```echarts
renderEcharts({
    title: { text: '示例图表' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: ['A', 'B', 'C'] },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: [10, 20, 30] }],
    toolbox: {
      show: true,
      feature: {
          mark: {show: true},
          dataView: {show: true, readOnly: false},
          restore: {show: true},
          saveAsImage: {show: true}
      }
   }
});
```
