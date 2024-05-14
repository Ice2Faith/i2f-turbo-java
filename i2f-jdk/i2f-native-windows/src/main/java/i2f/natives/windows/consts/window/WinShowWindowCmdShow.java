package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/9 9:32
 * @desc
 */
public interface WinShowWindowCmdShow {


    /*
     * ShowWindow() Commands
     */
    int SW_HIDE = 0;
    int SW_SHOWNORMAL = 1;
    int SW_NORMAL = 1;
    int SW_SHOWMINIMIZED = 2;
    int SW_SHOWMAXIMIZED = 3;
    int SW_MAXIMIZE = 3;
    int SW_SHOWNOACTIVATE = 4;
    int SW_SHOW = 5;
    int SW_MINIMIZE = 6;
    int SW_SHOWMINNOACTIVE = 7;
    int SW_SHOWNA = 8;
    int SW_RESTORE = 9;
    int SW_SHOWDEFAULT = 10;
    int SW_FORCEMINIMIZE = 11;
    int SW_MAX = 11;


    /*
     * Old ShowWindow() Commands
     */
    int HIDE_WINDOW = 0;
    int SHOW_OPENWINDOW = 1;
    int SHOW_ICONWINDOW = 2;
    int SHOW_FULLSCREEN = 3;
    int SHOW_OPENNOACTIVATE = 4;

    /*
     * Identifiers for the WM_SHOWWINDOW message
     */
    int SW_PARENTCLOSING = 1;
    int SW_OTHERZOOM = 2;
    int SW_PARENTOPENING = 3;
    int SW_OTHERUNZOOM = 4;

}
