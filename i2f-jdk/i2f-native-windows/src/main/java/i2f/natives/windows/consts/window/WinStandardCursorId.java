package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/16 16:50
 * @desc
 */
public interface WinStandardCursorId {

    /*
     * Standard Cursor IDs
     */
    int IDC_ARROW = 1; // MAKEINTRESOURCE(32512)
    int IDC_IBEAM = 2; // MAKEINTRESOURCE(32513)
    int IDC_WAIT = 3; // MAKEINTRESOURCE(32514)
    int IDC_CROSS = 4; // MAKEINTRESOURCE(32515)
    int IDC_UPARROW = 5; // MAKEINTRESOURCE(32516)
    int IDC_SIZE = 6; // MAKEINTRESOURCE(32640)  /* OBSOLETE: use IDC_SIZEALL */
    int IDC_ICON = 7; // MAKEINTRESOURCE(32641)  /* OBSOLETE: use IDC_ARROW */
    int IDC_SIZENWSE = 8; // MAKEINTRESOURCE(32642)
    int IDC_SIZENESW = 9; // MAKEINTRESOURCE(32643)
    int IDC_SIZEWE = 10; // MAKEINTRESOURCE(32644)
    int IDC_SIZENS = 11; // MAKEINTRESOURCE(32645)
    int IDC_SIZEALL = 12; // MAKEINTRESOURCE(32646)
    // #define IDC_NO              MAKEINTRESOURCE(32648) /*not in win3.1 */
// #if(WINVER >= 0x0500)
    int IDC_HAND = 13; // MAKEINTRESOURCE(32649)
    // #endif /* WINVER >= 0x0500 */
    int IDC_APPSTARTING = 14; // MAKEINTRESOURCE(32650) /*not in win3.1 */
    // #if(WINVER >= 0x0400)
    int IDC_HELP = 15; // MAKEINTRESOURCE(32651)
// #endif /* WINVER >= 0x0400 */


}
