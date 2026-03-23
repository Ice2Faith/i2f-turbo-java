---
name: weather_analysis_skill
description: 获取指定城市天气并分析是否适合户外运动
tags: [ weather, sports, analysis ]
version: 1.0.0
author: your_name
---

# 技能说明：天气运动分析

## 1. 触发条件 (When to use)

当用户询问某地天气是否适合跑步、踢球等户外活动时，或明确请求“天气分析”时触发。

## 2. 执行步骤 (How to execute)

本技能需按顺序编排以下 MCP 工具：

1. **调用 `get_weather`**：传入用户提供的城市名。
    - *输入*：`{ "city": "string" }`
    - *输出*：`{ "temp": number, "condition": "string", "wind": number }`
2. **逻辑判断**：
    - 若 `condition` 包含 "雨" 或 "雪"，直接返回“不适合户外”。
    - 若 `wind` > 5级，返回“风大，建议谨慎”。
3. **调用 `get_air_quality`** (可选)：若天气晴朗，进一步查询空气质量。
4. **生成结论**：综合上述信息，输出最终建议。

## 3. 错误处理 (Error Handling)

- 若天气 API 超时，重试 1 次；若仍失败，返回“暂时无法获取天气数据”。
- 若用户未提供城市，主动追问“请问您想查询哪个城市的天气？”

## 4. 示例 (Examples)

User: "北京今天适合跑步吗？"
Assistant: [调用 weather_analysis_skill] -> "北京今天晴，微风，空气质量优，非常适合跑步。"