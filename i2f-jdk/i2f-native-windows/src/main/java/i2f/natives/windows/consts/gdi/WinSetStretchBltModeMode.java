package i2f.natives.windows.consts.gdi;

/**
 * @author Ice2Faith
 * @date 2024/5/14 20:44
 * @desc
 */
public interface WinSetStretchBltModeMode {
    int BLACKONWHITE = 1;
    int WHITEONBLACK = 2;
    int COLORONCOLOR = 3;
    int HALFTONE = 4;
    int MAXSTRETCHBLTMODE = 4;

    // #if(WINVER >= 0x0400)
    /* New StretchBlt() Modes */
    int STRETCH_ANDSCANS = BLACKONWHITE;
    int STRETCH_ORSCANS = WHITEONBLACK;
    int STRETCH_DELETESCANS = COLORONCOLOR;
    int STRETCH_HALFTONE = HALFTONE;
// #endif /* WINVER >= 0x0400 */
}
