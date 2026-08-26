function getPresetBaseUrlList() {
    return [
        {
            label: 'OpenAI 官方',
            value: 'https://api.openai.com/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: '阿里云百炼 (DashScope)',
            value: 'https://dashscope.aliyuncs.com/compatible-mode/v1',
            apiKey: {
                url: 'https://bailian.console.aliyun.com/cn-beijing?tab=model#/api-key',
                tips: '登录之后，添加一个 API Key，复制 API Key，形如：sk-********'
            },
            models: [
                {label: "千问-Qwen3.7-Max", value: "qwen3.7-max"},
                {label: "千问-Qwen3.7-Plus", value: "qwen3.7-plus"},
                {label: "千问-Qwen3.6-Plus", value: "qwen3.6-plus"},
                {label: "千问-Qwen3.6-Max", value: "qwen3.6-max-preview"},
                {label: "千问-Qwen3.6-Flash", value: "qwen3.6-flash"},
                {label: "千问-Qwen3.6开源模型", value: "qwen3.6-35b-a3b"},
                {label: "千问-Qwen3.5-Plus", value: "qwen3.5-plus"},
                {label: "千问-Qwen3.5-Flash", value: "qwen3.5-flash"},
                {label: "千问-Qwen3.5开源模型", value: "qwen3.5-35b-a3b"},
                {label: "千问-Qwen3-Max", value: "qwen3-max"},
                {label: "千问-Qwen3-VL-Plus", value: "qwen3-vl-plus"},
                {label: "千问-Qwen3-VL-Flash", value: "qwen3-vl-flash"},
                {label: "千问-Qwen3开源模型", value: "qwen3-vl-32b-thinking"},
                {label: "千问-Qwen-Plus", value: "qwen-plus"},
                {label: "千问-Qwen-Flash", value: "qwen-flash"},
                {label: "千问-Qwen-Max", value: "qwen-max"},
                {label: "千问-Qwen3-Coder-Plus", value: "qwen3-coder-plus"},
                {label: "千问-Qwen3-Coder-Flash", value: "qwen3-coder-flash"},
                {label: "千问-qwen-deep-research", value: "qwen-deep-research-2025-12-15"},
                {label: "千问-Qwen-MT-Plus", value: "qwen-mt-plus"},
                {label: "千问-Qwen-MT-Flash", value: "qwen-mt-flash"},
                {label: "千问-Qwen-MT-Turbo", value: "qwen-mt-turbo"},
                {label: "千问-Qwen-QwQ-Plus", value: "qwq-plus"},
                {label: "千问-QVQ-Max", value: "qvq-max"},
                {label: "千问-Qwen-QVQ-Plus", value: "qvq-plus"},
                {
                    label: "千问-Qwen3-Coder-480B-A35B-Instruct",
                    value: "qwen3-coder-480b-a35b-instruct"
                },
                {label: "千问-Qwen3-Coder-30B-A3B-Instruct", value: "qwen3-coder-30b-a3b-instruct"},
                {label: "千问-Qwen2.5-开源模型", value: "qwen2.5-7b-instruct-1m"},
                {label: "千问-Qwen-Turbo", value: "qwen-turbo"},
                {label: "千问-Qwen-MT-Lite", value: "qwen-mt-lite"},
                {label: "千问-GUI-Plus", value: "gui-plus-2026-02-26"},
                {label: "千问-QwQ-32B", value: "qwq-32b"},
                {label: "千问-通义晓蜜-对话分析-pro", value: "tongyi-xiaomi-analysis-pro"},
                {label: "千问-通义晓蜜-对话分析-flash", value: "tongyi-xiaomi-analysis-flash"},
                {label: "千问-QwQ-32B-Preview", value: "qwq-32b-preview"},
                {label: "千问-Qwen-Math-Plus", value: "qwen-math-plus"},
                {label: "千问-Qwen-Math-Turbo", value: "qwen-math-turbo"},
                {label: "千问-Qwen-Coder-Turbo", value: "qwen-coder-turbo"},
                {label: "千问-意图分类模型", value: "tongyi-intent-detect-v3"},
                {label: "千问-Qwen-Long", value: "qwen-long-latest"},
                {label: "千问-Qwen-Doc-Turbo", value: "qwen-doc-turbo"},
                {label: "千问-Qwen-Flash-Character", value: "qwen-flash-character"},
                {label: "千问-Qwen-Plus-Character", value: "qwen-plus-character"},
                {label: "千问-Qwen-Coder-Plus", value: "qwen-coder-plus"},
                {label: "千问-通义法睿-Plus-32K", value: "farui-plus"},

                {label: 'DeepSeek-阿里云', value: 'deepseek-v4-pro'},
                {label: 'DeepSeek-阿里云', value: 'deepseek-v4-flash'},
                {label: 'DeepSeek-阿里云', value: 'deepseek-v3.2'},
                {label: 'DeepSeek-阿里云', value: 'deepseek-v3.2-exp'},
                {label: 'DeepSeek-阿里云', value: 'deepseek-v3.1'},
                {label: 'DeepSeek-阿里云', value: 'deepseek-r1'},
                {label: 'DeepSeek-阿里云', value: 'deepseek-r1-0528'},
                {label: 'DeepSeek-阿里云', value: 'deepseek-v3'},

                {label: 'DeepSeek-硅基流动', value: 'siliconflow/deepseek-v3.2'},
                {label: 'DeepSeek-硅基流动', value: 'siliconflow/deepseek-v3.1-terminus'},
                {label: 'DeepSeek-硅基流动', value: 'siliconflow/deepseek-r1-0528'},
                {label: 'DeepSeek-硅基流动', value: 'siliconflow/deepseek-v3-0324'},

                {label: 'DeepSeek-快手万擎', value: 'vanchin/deepseek-v3.2-think'},
                {label: 'DeepSeek-快手万擎', value: 'vanchin/deepseek-v3.1-terminus'},
                {label: 'DeepSeek-快手万擎', value: 'vanchin/deepseek-r1'},
                {label: 'DeepSeek-快手万擎', value: 'vanchin/deepseek-v3'},
                {label: 'DeepSeek-快手万擎', value: 'vanchin/deepseek-ocr'},

                {label: 'Kimi-阿里云', value: 'kimi-k2.6'},
                {label: 'Kimi-阿里云', value: 'kimi-k2.5'},
                {label: 'Kimi-阿里云', value: 'kimi-k2-thinking'},
                {label: 'Kimi-阿里云', value: 'Moonshot-Kimi-K2-Instruct'},

                {label: 'Kimi-月之暗面', value: 'kimi/kimi-k2.6'},
                {label: 'Kimi-月之暗面', value: 'kimi/kimi-k2.5'},

                {label: 'GLM', value: 'glm-5.1'},
                {label: 'GLM', value: 'glm-5'},
                {label: 'GLM', value: 'glm-4.7'},
                {label: 'GLM', value: 'glm-4.6'},
                {label: 'GLM', value: 'glm-4.5'},
                {label: 'GLM', value: 'glm-4.5-air'},

                {label: 'MiniMax-阿里云', value: 'MiniMax-M2.5'},
                {label: 'MiniMax-阿里云', value: 'MiniMax-M2.1'},

                {label: 'MiniMax-稀宇科技', value: 'MiniMax/MiniMax-M2.7'},
                {label: 'MiniMax-稀宇科技', value: 'MiniMax/MiniMax-M2.5'},
                {label: 'MiniMax-稀宇科技', value: 'MiniMax/MiniMax-M2.1'},
            ]
        },
        {
            label: '字节跳动火山引擎',
            value: 'https://ark.cn-beijing.volces.com/api/v3',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: '百度千帆',
            value: 'https://qianfan.baidubce.com/v2',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: '腾讯混元',
            value: 'https://api.hunyuan.cloud.tencent.com/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: '讯飞星火',
            value: 'https://spark-api-open.xf-yun.com/v1',
            apiKey: {
                url: 'https://console.xfyun.cn/services/bm35',
                tips: '登录之后，复制 APIPassword'
            },
            models: [
                {label: 'Spark Ultra', value: '4.0Ultra'},
                {label: 'Spark Max', value: 'generalv3.5'},
                {label: 'Spark Max-32K', value: 'max-32k'},
                {label: 'Spark Pro', value: 'generalv3'},
                {label: 'Spark Pro-128K', value: 'pro-128k'},
                {label: 'Spark Lite', value: 'lite'},
            ]
        },
        {
            label: '讯飞星火 X1.5',
            value: 'https://spark-api-open.xf-yun.com/v2',
            apiKey: {
                url: 'https://console.xfyun.cn/services/bmx1',
                tips: '登录之后，复制 APIPassword'
            },
            models: [
                {label: 'Spark X1.5', value: 'spark-x'},
            ]
        },
        {
            label: '讯飞星火 X2',
            value: 'https://spark-api-open.xf-yun.com/x2',
            apiKey: {
                url: 'https://console.xfyun.cn/services/bmx1',
                tips: '登录之后，复制 APIPassword'
            },
            models: [
                {label: 'Spark X2', value: 'spark-x'},
            ]
        },
        {
            label: '讯飞星火 X2-Flash',
            value: 'https://spark-api-open.xf-yun.com/agent/v1',
            apiKey: {
                url: 'https://console.xfyun.cn/services/bmAgent',
                tips: '登录之后，复制 APIPassword'
            },
            models: [
                {label: 'Spark X2-Flash', value: 'spark-x'},
            ]
        },
        {
            label: '智谱 GLM',
            value: 'https://open.bigmodel.cn/api/paas/v4',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: 'DeepSeek 官方',
            value: 'https://api.deepseek.com/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: 'Moonshot (Kimi)',
            value: 'https://api.moonshot.cn/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: 'MiniMax',
            value: 'https://api.minimax.chat/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: 'Groq',
            value: 'https://api.groq.com/openai/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: 'Together AI',
            value: 'https://api.together.xyz/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: 'OpenRouter',
            value: 'https://openrouter.ai/api/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: 'Ollama',
            value: 'http://localhost:11434/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: 'vLLM',
            value: 'http://localhost:8000/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
        {
            label: 'LocalAI',
            value: 'http://localhost:8080/v1',
            apiKey: {
                url: '',
                tips: ''
            },
            models: []
        },
    ]
}
