package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/13 11:03
 * @desc
 */
public interface WinGdiFontFamily {
    /* Font Families */
    int FF_DONTCARE = (0 << 4);  /* Don't care or don't know. */
    int FF_ROMAN = (1 << 4);  /* Variable stroke width, serifed. */
    /* Times Roman, Century Schoolbook, etc. */
    int FF_SWISS = (2 << 4);  /* Variable stroke width, sans-serifed. */
    /* Helvetica, Swiss, etc. */
    int FF_MODERN = (3 << 4);  /* Constant stroke width, serifed or sans-serifed. */
    /* Pica, Elite, Courier, etc. */
    int FF_SCRIPT = (4 << 4);  /* Cursive, etc. */
    int FF_DECORATIVE = (5 << 4);  /* Old English, etc. */
}
