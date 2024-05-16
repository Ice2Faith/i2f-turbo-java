package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/16 16:58
 * @desc
 */
public interface WinSystemColorIndex {

    int COLOR_SCROLLBAR = 0;
    int COLOR_BACKGROUND = 1;
    int COLOR_ACTIVECAPTION = 2;
    int COLOR_INACTIVECAPTION = 3;
    int COLOR_MENU = 4;
    int COLOR_WINDOW = 5;
    int COLOR_WINDOWFRAME = 6;
    int COLOR_MENUTEXT = 7;
    int COLOR_WINDOWTEXT = 8;
    int COLOR_CAPTIONTEXT = 9;
    int COLOR_ACTIVEBORDER = 10;
    int COLOR_INACTIVEBORDER = 11;
    int COLOR_APPWORKSPACE = 12;
    int COLOR_HIGHLIGHT = 13;
    int COLOR_HIGHLIGHTTEXT = 14;
    int COLOR_BTNFACE = 15;
    int COLOR_BTNSHADOW = 16;
    int COLOR_GRAYTEXT = 17;
    int COLOR_BTNTEXT = 18;
    int COLOR_INACTIVECAPTIONTEXT = 19;
    int COLOR_BTNHIGHLIGHT = 20;

    // #if(WINVER >= 0x0400)
    int COLOR_3DDKSHADOW = 21;
    int COLOR_3DLIGHT = 22;
    int COLOR_INFOTEXT = 23;
    int COLOR_INFOBK = 24;
// #endif /* WINVER >= 0x0400 */

    // #if(WINVER >= 0x0500)
    int COLOR_HOTLIGHT = 26;
    int COLOR_GRADIENTACTIVECAPTION = 27;
    int COLOR_GRADIENTINACTIVECAPTION = 28;
    // #if(WINVER >= 0x0501)
    int COLOR_MENUHILIGHT = 29;
    int COLOR_MENUBAR = 30;
// #endif /* WINVER >= 0x0501 */
// #endif /* WINVER >= 0x0500 */
}
