package i2f.natives.windows.consts.gdi;

/**
 * @author Ice2Faith
 * @date 2024/5/13 11:02
 * @desc
 */
public interface WinGdiFontPitch {

    int DEFAULT_PITCH = 0;
    int FIXED_PITCH = 1;
    int VARIABLE_PITCH = 2;
    // #if(WINVER >= 0x0400)
    int MONO_FONT = 8;
// #endif /* WINVER >= 0x0400 */
}
