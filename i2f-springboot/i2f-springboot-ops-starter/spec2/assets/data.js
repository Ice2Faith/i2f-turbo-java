function getDemoMarkdown() {
    return `
# 标题

- 列表

## mermaid 图表
` +
        '```mermaid' +
        `
    sequenceDiagram
    participant U as 用户浏览器
    participant A as 应用A
    participant B as 应用B
    participant S as SSO认证中心

    Note over U,S: === 首次访问应用A（未登录） ===

    U->>A: 1. 访问应用A受保护资源
    A-->>U: 2. 未登录，302重定向到SSO认证中心
    U->>S: 3. 携带service=A的回调地址，请求登录页
    S-->>U: 4. 返回登录页面
    U->>S: 5. 提交用户名和密码
    S->>S: 6. 验证凭证，创建全局会话
    S-->>U: 7. 302重定向回应用A，携带Ticket(ST)
    U->>A: 8. 携带Ticket请求应用A
    A->>S: 9. 向SSO中心验证Ticket有效性
    S-->>A: 10. 返回验证结果及用户信息
    A->>A: 11. 创建本地会话
    A-->>U: 12. 返回受保护资源

    Note over U,S: === 访问应用B（已登录SSO） ===

    U->>B: 13. 访问应用B受保护资源
    B-->>U: 14. 未登录，302重定向到SSO认证中心
    U->>S: 15. 携带service=B的回调地址，请求认证
    S->>S: 16. 检测到已有全局会话，无需再次登录
    S-->>U: 17. 302重定向回应用B，携带新Ticket(ST)
    U->>B: 18. 携带Ticket请求应用B
    B->>S: 19. 向SSO中心验证Ticket有效性
    S-->>B: 20. 返回验证结果及用户信息
    B->>B: 21. 创建本地会话
    B-->>U: 22. 返回受保护资源
` +
        '```' +
        `

- 列表

`
}