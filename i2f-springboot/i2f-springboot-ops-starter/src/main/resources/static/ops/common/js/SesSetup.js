function ses_eval(code, userConfig) {
    if (!window._init_ses) {
        // 1. 初始化 SES 安全环境（必须在所有代码之前执行）
        lockdown();
        window._init_ses = true;
    }
    return new Promise((resolve, reject) => {
        try {
            // 创建安全沙箱
            let config = {
                Math: Math // 允许访问Math, 主要是 Math.random() 存在攻击面
            }
            if (typeof echarts != 'undefined') {
                config.echarts = echarts; // 如果有echarts，则开放到沙箱中
            }
            config = {...config, ...userConfig}
            const sandbox = new Compartment(config);

            // 在沙箱中安全执行
            const option = sandbox.evaluate(`function f(){${code}};f();`);
            resolve(option)
        } catch (e) {
            console.error('安全沙箱解析失败:', e);
            reject('安全沙箱解析失败')
        }
    })
}