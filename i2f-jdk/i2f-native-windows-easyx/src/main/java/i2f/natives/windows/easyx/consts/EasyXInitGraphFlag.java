package i2f.natives.windows.easyx.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/12 21:54
 * @desc
 */
public interface EasyXInitGraphFlag {
    // 绘图窗口初始化参数
    int DEFAULT = 0;
    int SHOWCONSOLE = 1;        // 创建图形窗口时，保留控制台的显示
    int NOCLOSE = 2;        // 没有关闭功能
    int NOMINIMIZE = 4;        // 没有最小化功能
}
