package i2f.natives.windows.consts.gdi;

/**
 * @author Ice2Faith
 * @date 2024/5/16 11:44
 * @desc
 */
public interface WinGdiTextAlign {

    /* Text Alignment Options */
    int TA_NOUPDATECP = 0;
    int TA_UPDATECP = 1;

    int TA_LEFT = 0;
    int TA_RIGHT = 2;
    int TA_CENTER = 6;

    int TA_TOP = 0;
    int TA_BOTTOM = 8;
    int TA_BASELINE = 24;
    // #if (WINVER >= 0x0400)
    int TA_RTLREADING = 256;
    int TA_MASK = (TA_BASELINE + TA_CENTER + TA_UPDATECP + TA_RTLREADING);
// #else
// int TA_MASK       =(TA_BASELINE+TA_CENTER+TA_UPDATECP);
// #endif
}
