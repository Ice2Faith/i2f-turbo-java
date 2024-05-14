package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/9 14:21
 * @desc
 */
public interface WinClassStyle {
    int CS_VREDRAW = 0x0001;
    int CS_HREDRAW = 0x0002;
    int CS_DBLCLKS = 0x0008;
    int CS_OWNDC = 0x0020;
    int CS_CLASSDC = 0x0040;
    int CS_PARENTDC = 0x0080;
    int CS_NOCLOSE = 0x0200;
    int CS_SAVEBITS = 0x0800;
    int CS_BYTEALIGNCLIENT = 0x1000;
    int CS_BYTEALIGNWINDOW = 0x2000;
    int CS_GLOBALCLASS = 0x4000;

    int CS_IME = 0x00010000;
    // #if(_WIN32_WINNT >= 0x0501)
    int CS_DROPSHADOW = 0x00020000;
// #endif /* _WIN32_WINNT >= 0x0501 */

}
