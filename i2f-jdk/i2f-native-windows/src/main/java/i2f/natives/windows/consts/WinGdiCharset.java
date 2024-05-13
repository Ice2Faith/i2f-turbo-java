package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/13 10:54
 * @desc
 */
public interface WinGdiCharset {

    int ANSI_CHARSET = 0;
    int DEFAULT_CHARSET = 1;
    int SYMBOL_CHARSET = 2;
    int SHIFTJIS_CHARSET = 128;
    int HANGEUL_CHARSET = 129;
    int HANGUL_CHARSET = 129;
    int GB2312_CHARSET = 134;
    int CHINESEBIG5_CHARSET = 136;
    int OEM_CHARSET = 255;
    // #if(WINVER >= 0x0400)
    int JOHAB_CHARSET = 130;
    int HEBREW_CHARSET = 177;
    int ARABIC_CHARSET = 178;
    int GREEK_CHARSET = 161;
    int TURKISH_CHARSET = 162;
    int VIETNAMESE_CHARSET = 163;
    int THAI_CHARSET = 222;
    int EASTEUROPE_CHARSET = 238;
    int RUSSIAN_CHARSET = 204;

    int MAC_CHARSET = 77;
    int BALTIC_CHARSET = 186;

    long FS_LATIN1 = 0x00000001L;
    long FS_LATIN2 = 0x00000002L;
    long FS_CYRILLIC = 0x00000004L;
    long FS_GREEK = 0x00000008L;
    long FS_TURKISH = 0x00000010L;
    long FS_HEBREW = 0x00000020L;
    long FS_ARABIC = 0x00000040L;
    long FS_BALTIC = 0x00000080L;
    long FS_VIETNAMESE = 0x00000100L;
    long FS_THAI = 0x00010000L;
    long FS_JISJAPAN = 0x00020000L;
    long FS_CHINESESIMP = 0x00040000L;
    long FS_WANSUNG = 0x00080000L;
    long FS_CHINESETRAD = 0x00100000L;
    long FS_JOHAB = 0x00200000L;
    long FS_SYMBOL = 0x80000000L;
// #endif /* WINVER >= 0x0400 */
}
