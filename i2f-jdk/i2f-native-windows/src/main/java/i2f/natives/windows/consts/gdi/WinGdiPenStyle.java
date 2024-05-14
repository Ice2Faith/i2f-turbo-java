package i2f.natives.windows.consts.gdi;

/**
 * @author Ice2Faith
 * @date 2024/5/13 15:16
 * @desc
 */
public interface WinGdiPenStyle {

    /* Pen Styles */
    int PS_SOLID = 0;
    int PS_DASH = 1;       /* -------  */
    int PS_DOT = 2;       /* .......  */
    int PS_DASHDOT = 3;       /* _._._._  */
    int PS_DASHDOTDOT = 4;       /* _.._.._  */
    int PS_NULL = 5;
    int PS_INSIDEFRAME = 6;
    int PS_USERSTYLE = 7;
    int PS_ALTERNATE = 8;
    int PS_STYLE_MASK = 0x0000000F;

    int PS_ENDCAP_ROUND = 0x00000000;
    int PS_ENDCAP_SQUARE = 0x00000100;
    int PS_ENDCAP_FLAT = 0x00000200;
    int PS_ENDCAP_MASK = 0x00000F00;

    int PS_JOIN_ROUND = 0x00000000;
    int PS_JOIN_BEVEL = 0x00001000;
    int PS_JOIN_MITER = 0x00002000;
    int PS_JOIN_MASK = 0x0000F000;

    int PS_COSMETIC = 0x00000000;
    int PS_GEOMETRIC = 0x00010000;
    int PS_TYPE_MASK = 0x000F0000;
}
