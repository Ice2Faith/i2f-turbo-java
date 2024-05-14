package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/9 14:14
 * @desc
 */
public interface WinLayeredWindowAttributesFlag {
    int LWA_COLORKEY = 0x00000001;
    int LWA_ALPHA = 0x00000002;


    int ULW_COLORKEY = 0x00000001;
    int ULW_ALPHA = 0x00000002;
    int ULW_OPAQUE = 0x00000004;

    int ULW_EX_NORESIZE = 0x00000008;
}
