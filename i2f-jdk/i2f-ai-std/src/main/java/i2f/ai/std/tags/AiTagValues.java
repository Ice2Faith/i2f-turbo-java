package i2f.ai.std.tags;

/**
 * @author Ice2Faith
 * @date 2026/6/2 14:04
 * @desc
 */
public interface AiTagValues {
    // ==========================================
    // 1. 操作敏感度与副作用
    // ==========================================
    String READONLY_VALUE = "readonly";
    String WRITABLE_VALUE = "writable";
    String EXECUTABLE_VALUE = "executable";

    // ==========================================
    // 2. 网络与环境边界
    // ==========================================
    String PUBLIC_NET_VALUE = "public_net";
    String PRIVATE_NET_VALUE = "private_net";
    String INTRANET_ONLY_VALUE = "intranet_only";

    // ==========================================
    // 3. 控制粒度与人机协同
    // ==========================================
    String HUMAN_VALUE = "human";
    String SANDBOX_VALUE = "sandbox";
    String AUTO_VALUE = "auto";

    // ==========================================
    // 4. 数据安全与隐私合规
    // ==========================================
    String SENSIBLE_VALUE = "sensible";
    String AUTH_VALUE = "auth";
    String SECRET_VALUE = "secret";

    // ==========================================
    // 5. 资源成本与性能影响
    // ==========================================
    String HIGH_COST_VALUE = "high_cost";
    String SLOW_EXEC_VALUE = "slow_exec";
    String RATE_LIMITED_VALUE = "rate_limited";
}
