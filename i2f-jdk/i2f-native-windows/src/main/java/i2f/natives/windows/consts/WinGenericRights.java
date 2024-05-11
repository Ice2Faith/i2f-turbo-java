package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/11 16:18
 * @desc
 */
public interface WinGenericRights {
    long GENERIC_READ = (0x80000000L);
    long GENERIC_WRITE = (0x40000000L);
    long GENERIC_EXECUTE = (0x20000000L);
    long GENERIC_ALL = (0x10000000L);
}
