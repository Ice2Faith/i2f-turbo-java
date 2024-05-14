package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/14 17:29
 * @desc
 */
public interface WinSystemMenuCommand {
    int SC_SIZE = 0xF000;
    int SC_MOVE = 0xF010;
    int SC_MINIMIZE = 0xF020;
    int SC_MAXIMIZE = 0xF030;
    int SC_NEXTWINDOW = 0xF040;
    int SC_PREVWINDOW = 0xF050;
    int SC_CLOSE = 0xF060;
    int SC_VSCROLL = 0xF070;
    int SC_HSCROLL = 0xF080;
    int SC_MOUSEMENU = 0xF090;
    int SC_KEYMENU = 0xF100;
    int SC_ARRANGE = 0xF110;
    int SC_RESTORE = 0xF120;
    int SC_TASKLIST = 0xF130;
    int SC_SCREENSAVE = 0xF140;
    int SC_HOTKEY = 0xF150;
    // #if(WINVER >= 0x0400)
    int SC_DEFAULT = 0xF160;
    int SC_MONITORPOWER = 0xF170;
    int SC_CONTEXTHELP = 0xF180;
    int SC_SEPARATOR = 0xF00F;
// #endif /* WINVER >= 0x

    int MONITORPOWER_ON = -1; // 显示设备打开

    int MONITORPOWER_POWERSAVE = 1; // 显示设备将要进入节电模式。

    int MONITORPOWER_OFF = 2; // 显示设备将要被关闭
}
