---
name: js_canvas_painter
description: 指导使用 js 的 canvas 为用户绘制任意图像内容
version: 1.0
author: Ice2Faith
---

# Js Canvas 图像绘制

# Context

用户页面环境中已经封装好了一个钩子函数，定义为：`drawCanvas(dom)`， 入参 `dom` 是给你用来绘制的 `canvas` 元素
你可以通过这个dom元素获取2d图形环境或者3d图形环境(如果用户的浏览器支持3d图形环境的话)。

# Task

当需要进行图像绘制时（如画图、创意图像等），请严格按照以下规则输出：

- 输出格式：只要响应中包含使用 Markdown 的代码块，且语言类型固定为 `canvas`。
- 代码内容：在代码块内部，仅写实现这个函数体：`drawCanvas(dom){...}`，就可以实现在用户界面上显示出绘制的图像内容。
    - 配置对象,用户无特殊要求时，应该采用以下策略
        - 应该直接使用 dom 的宽高，如果用户明确要求宽高，可以通过操作 document 改变宽高
		- 也可以自由操作为合适的宽高，宽高比限制范围为：240*240~1280*1280，超过此范围，用户页面显示会出现问题
- 纯净度要求：`canvas` 代码块内部不要输出任何额外的解释、JS 函数头代码（如 `function `）、HTML 容器标签或多余的文本。
    - `canvas` 代码块之外，允许常规 markdown 内容。
- 多图限制：由于环境限制，每个 `canvas` 代码块只能绘制一个图像。
    - 如果需要展示多个不同图像，必须将它们拆分为多个独立的 `canvas` 代码块。

# Example

```canvas
drawCanvas(dom){
  const ctx = dom.getContext('2d');
  const w = dom.width;
  const h = dom.height;
  const s = Math.min(w, h) * 0.22;
  const cx = w / 2;
  const cy = h / 2;

  // 背景
  ctx.fillStyle = '#F8F9FA';
  ctx.fillRect(0, 0, w, h);

  // 矩形（左下）
  ctx.fillStyle = '#4A90D9';
  ctx.fillRect(cx - s * 1.5, cy + s * 0.1, s * 1.1, s * 1.1);

  // 圆形（右上）
  ctx.beginPath();
  ctx.arc(cx + s * 0.9, cy - s * 0.4, s * 0.6, 0, Math.PI * 2);
  ctx.fillStyle = '#F5A623';
  ctx.fill();
}
```
