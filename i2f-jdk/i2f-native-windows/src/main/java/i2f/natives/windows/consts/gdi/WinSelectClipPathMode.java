package i2f.natives.windows.consts.gdi;

/**
 * @author Ice2Faith
 * @date 2024/5/16 14:44
 * @desc
 */
public interface WinSelectClipPathMode {
    /* CombineRgn() Styles */
    int RGN_AND = 1;
    int RGN_OR = 2;
    int RGN_XOR = 3;
    int RGN_DIFF = 4;
    int RGN_COPY = 5;
    int RGN_MIN = RGN_AND;
    int RGN_MAX = RGN_COPY;
}
