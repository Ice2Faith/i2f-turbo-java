package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/13 11:00
 * @desc
 */
public interface WinGdiFontQuality {

    int DEFAULT_QUALITY = 0;
    int DRAFT_QUALITY = 1;
    int PROOF_QUALITY = 2;
    // #if(WINVER >= 0x0400)
    int NONANTIALIASED_QUALITY = 3;
    int ANTIALIASED_QUALITY = 4;
// #endif /* WINVER >= 0x0400 */

    // #if (_WIN32_WINNT >= _WIN32_WINNT_WINXP)
    int CLEARTYPE_QUALITY = 5;
    int CLEARTYPE_NATURAL_QUALITY = 6;
// #endif
}
