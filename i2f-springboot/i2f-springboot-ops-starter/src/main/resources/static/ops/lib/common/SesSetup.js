function ses_eval(code) {
    if (!window._init_ses) {
        // 1. 初始化 SES 安全环境（必须在所有代码之前执行）
        lockdown();
        window._init_ses = true;
    }
    return new Promise((resolve, reject) => {
        try {
            // 创建安全沙箱
            const sandbox = new Compartment({});

            // 在沙箱中安全执行
            const option = sandbox.evaluate(`function f(){${code}};f();`);
            resolve(option)
        } catch (e) {
            console.error('安全沙箱解析失败:', e);
            reject('安全沙箱解析失败')
        }
    })
}