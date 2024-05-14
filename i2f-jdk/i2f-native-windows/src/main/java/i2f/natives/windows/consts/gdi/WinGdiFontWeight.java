package i2f.natives.windows.consts.gdi;

/**
 * @author Ice2Faith
 * @date 2024/5/13 10:51
 * @desc
 */
public interface WinGdiFontWeight {

    /* Font Weights */
    int FW_DONTCARE = 0;
    int FW_THIN = 100;
    int FW_EXTRALIGHT = 200;
    int FW_LIGHT = 300;
    int FW_NORMAL = 400;
    int FW_MEDIUM = 500;
    int FW_SEMIBOLD = 600;
    int FW_BOLD = 700;
    int FW_EXTRABOLD = 800;
    int FW_HEAVY = 900;

    int FW_ULTRALIGHT = FW_EXTRALIGHT;
    int FW_REGULAR = FW_NORMAL;
    int FW_DEMIBOLD = FW_SEMIBOLD;
    int FW_ULTRABOLD = FW_EXTRABOLD;
    int FW_BLACK = FW_HEAVY;
}
