const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        port: 8080, // 本地开发端口，可根据需要修改
        open: true, // 启动后自动打开浏览器
        proxy: {
            // 核心配置：代理规则
            '/api': {
                target: 'http://localhost:8082/app/', // 【重要】后端接口的真实地址
                changeOrigin: true, // 【重要】修改请求头中的 Host 为 target 的地址，解决跨域
                secure: false,      // 如果后端是 https 且证书自签名，需设置为 false
                ws: true,           // 是否代理 websocket
                pathRewrite: {
                    '^/api': ''       // 【可选】重写路径：去掉请求中的 /api 前缀
                }
            },
        }
    }
})
