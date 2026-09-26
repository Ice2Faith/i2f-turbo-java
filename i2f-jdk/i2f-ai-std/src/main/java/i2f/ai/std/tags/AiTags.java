package i2f.ai.std.tags;

/**
 * @author Ice2Faith
 * @date 2026/4/1 22:15
 * @desc
 */
public enum AiTags implements AiTagValues {
    // ==========================================
    // 1. 操作敏感度与副作用
    // ==========================================
    READONLY(READONLY_VALUE, "只读操作，仅查询数据，不产生副作用"),
    WRITABLE(WRITABLE_VALUE, "可写操作，会修改数据库状态或产生持久化变更"),
    EXECUTABLE(EXECUTABLE_VALUE, "可执行操作，涉及代码执行、脚本运行或系统命令调用"),

    // ==========================================
    // 2. 网络与环境边界
    // ==========================================
    PUBLIC_NET(PUBLIC_NET_VALUE, "涉及公共网络访问，如搜索互联网、调用外部公开API"),
    PRIVATE_NET(PRIVATE_NET_VALUE, "仅限内部私有网络，访问内网服务、数据库或内部微服务"),
    INTRANET_ONLY(INTRANET_ONLY_VALUE, "仅限局域网/本机，不可访问任何外部资源，安全性极高"),

    // ==========================================
    // 3. 控制粒度与人机协同
    // ==========================================
    HUMAN(HUMAN_VALUE, "需要人类介入，通常用于审批、确认或决策辅助"),
    SANDBOX(SANDBOX_VALUE, "沙箱环境，操作被限制在隔离环境中，风险可控"),
    AUTO(AUTO_VALUE, "全自动执行，无需人工干预，Agent可独立完成闭环"),

    // ==========================================
    // 4. 数据安全与隐私合规
    // ==========================================
    SENSIBLE(SENSIBLE_VALUE, "涉及个人敏感信息，如手机号、身份证、邮箱，需进行脱敏处理"),
    AUTH(AUTH_VALUE, "需要用户授权或鉴权，调用前需检查用户登录态或权限"),
    SECRET(SECRET_VALUE, "涉及商业机密或高敏感数据，严禁输出到公有云模型"),

    // ==========================================
    // 5. 资源成本与性能影响
    // ==========================================
    HIGH_COST(HIGH_COST_VALUE, "高成本操作，消耗大量Token、算力或产生直接金钱费用"),
    SLOW_EXEC(SLOW_EXEC_VALUE, "慢速执行，耗时较长，可能导致请求超时，建议异步处理"),
    RATE_LIMITED(RATE_LIMITED_VALUE, "限流敏感，该工具调用频率受限，需谨慎使用"),

    // ==========================================
    // 6. 基础分类
    // ==========================================
    DATETIME(DATETIME_VALUE, "日期时间"),
    RANDOM(RANDOM_VALUE, "随机"),
    FILE(FILE_VALUE, "文件"),
    DATABASE(DATABASE_VALUE, "数据库"),
    OS(OS_VALUE, "操作系统"),
    COMMAND(COMMAND_VALUE, "命令执行"),
    WEB(WEB_VALUE, "网页"),
    DOWNLOAD(DOWNLOAD_VALUE, "下载"),
    BROWSER(BROWSER_VALUE, "浏览器"),
    MATH(MATH_VALUE, "数学"),
    SCRIPT(SCRIPT_VALUE, "脚本执行"),
    CODEC(CODEC_VALUE, "编解码"),
    RAG(RAG_VALUE, "知识库"),
    SKILL(SKILL_VALUE, "技能"),
    MCP(MCP_VALUE, "工具库");

    private String tag;
    private String description;


    private AiTags(String tag, String description) {
        this.tag = tag;
        this.description = description;
    }

    public String tag() {
        return tag;
    }

    public String description() {
        return description;
    }

}
