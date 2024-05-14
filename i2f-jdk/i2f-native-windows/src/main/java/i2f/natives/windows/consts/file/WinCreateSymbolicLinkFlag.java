package i2f.natives.windows.consts.file;

/**
 * @author Ice2Faith
 * @date 2024/5/14 15:45
 * @desc
 */
public interface WinCreateSymbolicLinkFlag {
    int SYMBOLIC_LINK_FLAG_FILE = 0x0; // 链接目标是文件。
    int SYMBOLIC_LINK_FLAG_DIRECTORY = 0x1; // 链接目标是目录。
    int SYMBOLIC_LINK_FLAG_ALLOW_UNPRIVILEGED_CREATE = 0x2; // 指定此标志以允许在进程未提升时创建符号链接。 必须先在计算机上启用开发人员模式，此选项才能正常工作。
}
