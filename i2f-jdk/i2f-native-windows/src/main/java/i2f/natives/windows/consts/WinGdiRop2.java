package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/13 9:13
 * @desc
 */
public interface WinGdiRop2 {
    /* Binary raster ops */
    int R2_BLACK = 1;  /*  0       */
    int R2_NOTMERGEPEN = 2;  /* DPon     */
    int R2_MASKNOTPEN = 3;  /* DPna     */
    int R2_NOTCOPYPEN = 4;  /* PN       */
    int R2_MASKPENNOT = 5;  /* PDna     */
    int R2_NOT = 6;  /* Dn       */
    int R2_XORPEN = 7;  /* DPx      */
    int R2_NOTMASKPEN = 8;  /* DPan     */
    int R2_MASKPEN = 9;  /* DPa      */
    int R2_NOTXORPEN = 10;  /* DPxn     */
    int R2_NOP = 11;  /* D        */
    int R2_MERGENOTPEN = 12;  /* DPno     */
    int R2_COPYPEN = 13;  /* P        */
    int R2_MERGEPENNOT = 14;  /* PDno     */
    int R2_MERGEPEN = 15;  /* DPo      */
    int R2_WHITE = 16;  /*  1       */
    int R2_LAST = 16;
}
