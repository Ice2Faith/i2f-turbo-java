package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/13 10:58
 * @desc
 */
public interface WinGdiClipPrecision {

    int CLIP_DEFAULT_PRECIS = 0;
    int CLIP_CHARACTER_PRECIS = 1;
    int CLIP_STROKE_PRECIS = 2;
    int CLIP_MASK = 0xf;
    int CLIP_LH_ANGLES = (1 << 4);
    int CLIP_TT_ALWAYS = (2 << 4);
    // #if (_WIN32_WINNT >= _WIN32_WINNT_LONGHORN)
    int CLIP_DFA_DISABLE = (4 << 4);
    // #endif // (_WIN32_WINNT >= _WIN32_WINNT_LONGHORN)
    int CLIP_EMBEDDED = (8 << 4);
}
