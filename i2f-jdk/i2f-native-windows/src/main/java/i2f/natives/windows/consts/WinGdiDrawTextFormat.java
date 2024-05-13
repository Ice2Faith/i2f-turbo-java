package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/13 9:26
 * @desc
 */
public interface WinGdiDrawTextFormat {
    int DT_TOP = 0x00000000;
    int DT_LEFT = 0x00000000;
    int DT_CENTER = 0x00000001;
    int DT_RIGHT = 0x00000002;
    int DT_VCENTER = 0x00000004;
    int DT_BOTTOM = 0x00000008;
    int DT_WORDBREAK = 0x00000010;
    int DT_SINGLELINE = 0x00000020;
    int DT_EXPANDTABS = 0x00000040;
    int DT_TABSTOP = 0x00000080;
    int DT_NOCLIP = 0x00000100;
    int DT_EXTERNALLEADING = 0x00000200;
    int DT_CALCRECT = 0x00000400;
    int DT_NOPREFIX = 0x00000800;
    int DT_INTERNAL = 0x00001000;

    // #if(WINVER >= 0x0400)
    int DT_EDITCONTROL = 0x00002000;
    int DT_PATH_ELLIPSIS = 0x00004000;
    int DT_END_ELLIPSIS = 0x00008000;
    int DT_MODIFYSTRING = 0x00010000;
    int DT_RTLREADING = 0x00020000;
    int DT_WORD_ELLIPSIS = 0x00040000;
    // #if(WINVER >= 0x0500)
    int DT_NOFULLWIDTHCHARBREAK = 0x00080000;
    // #if(_WIN32_WINNT >= 0x0500)
    int DT_HIDEPREFIX = 0x00100000;
    int DT_PREFIXONLY = 0x00200000;
// #endif /* _WIN32_WINNT >= 0x0500 */
// #endif /* WINVER >= 0x0500 */
}
