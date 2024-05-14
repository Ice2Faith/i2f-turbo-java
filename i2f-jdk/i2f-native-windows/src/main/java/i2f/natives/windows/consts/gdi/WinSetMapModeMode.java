package i2f.natives.windows.consts.gdi;

/**
 * @author Ice2Faith
 * @date 2024/5/14 20:49
 * @desc
 */
public interface WinSetMapModeMode {
    /* Mapping Modes */
    int MM_TEXT = 1;
    int MM_LOMETRIC = 2;
    int MM_HIMETRIC = 3;
    int MM_LOENGLISH = 4;
    int MM_HIENGLISH = 5;
    int MM_TWIPS = 6;
    int MM_ISOTROPIC = 7;
    int MM_ANISOTROPIC = 8;

    /* Min and Max Mapping Mode values */
    int MM_MIN = MM_TEXT;
    int MM_MAX = MM_ANISOTROPIC;
    int MM_MAX_FIXEDSCALE = MM_TWIPS;
}
