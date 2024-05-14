package i2f.natives.windows.consts.device;

/**
 * @author Ice2Faith
 * @date 2024/5/10 14:52
 * @desc
 */
public interface WinKeyboardEventVk {
    int VK_LBUTTON = 0x01;
    int VK_RBUTTON = 0x02;
    int VK_CANCEL = 0x03;
    int VK_MBUTTON = 0x04;    /* NOT contiguous with L & RBUTTON */

    // #if(_WIN32_WINNT >= 0x0500)
    int VK_XBUTTON1 = 0x05;    /* NOT contiguous with L & RBUTTON */
    int VK_XBUTTON2 = 0x06;    /* NOT contiguous with L & RBUTTON */
// #endif /* _WIN32_WINNT >= 0x0500 */

    /*
     * 0x07 : unassigned
     */

    int VK_BACK = 0x08;
    int VK_TAB = 0x09;

    /*
     * 0x0A - 0x0B : reserved
     */

    int VK_CLEAR = 0x0C;
    int VK_RETURN = 0x0D;

    int VK_SHIFT = 0x10;
    int VK_CONTROL = 0x11;
    int VK_MENU = 0x12;
    int VK_PAUSE = 0x13;
    int VK_CAPITAL = 0x14;

    int VK_KANA = 0x15;
    int VK_HANGEUL = 0x15;  /* old name - should be here for compatibility */
    int VK_HANGUL = 0x15;
    int VK_JUNJA = 0x17;
    int VK_FINAL = 0x18;
    int VK_HANJA = 0x19;
    int VK_KANJI = 0x19;

    int VK_ESCAPE = 0x1B;

    int VK_CONVERT = 0x1C;
    int VK_NONCONVERT = 0x1D;
    int VK_ACCEPT = 0x1E;
    int VK_MODECHANGE = 0x1F;

    int VK_SPACE = 0x20;
    int VK_PRIOR = 0x21;
    int VK_NEXT = 0x22;
    int VK_END = 0x23;
    int VK_HOME = 0x24;
    int VK_LEFT = 0x25;
    int VK_UP = 0x26;
    int VK_RIGHT = 0x27;
    int VK_DOWN = 0x28;
    int VK_SELECT = 0x29;
    int VK_PRINT = 0x2A;
    int VK_EXECUTE = 0x2B;
    int VK_SNAPSHOT = 0x2C;
    int VK_INSERT = 0x2D;
    int VK_DELETE = 0x2E;
    int VK_HELP = 0x2F;

    /*
     * VK_0 - VK_9 are the same as ASCII '0' - '9' (0x30 - 0x39)
     * 0x40 : unassigned
     * VK_A - VK_Z are the same as ASCII 'A' - 'Z' (0x41 - 0x5A)
     */

    int VK_LWIN = 0x5B;
    int VK_RWIN = 0x5C;
    int VK_APPS = 0x5D;

    /*
     * 0x5E : reserved
     */

    int VK_SLEEP = 0x5F;

    int VK_NUMPAD0 = 0x60;
    int VK_NUMPAD1 = 0x61;
    int VK_NUMPAD2 = 0x62;
    int VK_NUMPAD3 = 0x63;
    int VK_NUMPAD4 = 0x64;
    int VK_NUMPAD5 = 0x65;
    int VK_NUMPAD6 = 0x66;
    int VK_NUMPAD7 = 0x67;
    int VK_NUMPAD8 = 0x68;
    int VK_NUMPAD9 = 0x69;
    int VK_MULTIPLY = 0x6A;
    int VK_ADD = 0x6B;
    int VK_SEPARATOR = 0x6C;
    int VK_SUBTRACT = 0x6D;
    int VK_DECIMAL = 0x6E;
    int VK_DIVIDE = 0x6F;
    int VK_F1 = 0x70;
    int VK_F2 = 0x71;
    int VK_F3 = 0x72;
    int VK_F4 = 0x73;
    int VK_F5 = 0x74;
    int VK_F6 = 0x75;
    int VK_F7 = 0x76;
    int VK_F8 = 0x77;
    int VK_F9 = 0x78;
    int VK_F10 = 0x79;
    int VK_F11 = 0x7A;
    int VK_F12 = 0x7B;
    int VK_F13 = 0x7C;
    int VK_F14 = 0x7D;
    int VK_F15 = 0x7E;
    int VK_F16 = 0x7F;
    int VK_F17 = 0x80;
    int VK_F18 = 0x81;
    int VK_F19 = 0x82;
    int VK_F20 = 0x83;
    int VK_F21 = 0x84;
    int VK_F22 = 0x85;
    int VK_F23 = 0x86;
    int VK_F24 = 0x87;

    /*
     * 0x88 - 0x8F : unassigned
     */

    int VK_NUMLOCK = 0x90;
    int VK_SCROLL = 0x91;

    /*
     * NEC PC-9800 kbd definitions
     */
    int VK_OEM_NEC_EQUAL = 0x92;   // '=' key on numpad

    /*
     * Fujitsu/OASYS kbd definitions
     */
    int VK_OEM_FJ_JISHO = 0x92;   // 'Dictionary' key
    int VK_OEM_FJ_MASSHOU = 0x93;   // 'Unregister word' key
    int VK_OEM_FJ_TOUROKU = 0x94;   // 'Register word' key
    int VK_OEM_FJ_LOYA = 0x95;   // 'Left OYAYUBI' key
    int VK_OEM_FJ_ROYA = 0x96;   // 'Right OYAYUBI' key

    /*
     * 0x97 - 0x9F : unassigned
     */

    /*
     * VK_L* & VK_R* - left and right Alt, Ctrl and Shift virtual keys.
     * Used only as parameters to GetAsyncKeyState() and GetKeyState().
     * No other API or message will distinguish left and right keys in this way.
     */
    int VK_LSHIFT = 0xA0;
    int VK_RSHIFT = 0xA1;
    int VK_LCONTROL = 0xA2;
    int VK_RCONTROL = 0xA3;
    int VK_LMENU = 0xA4;
    int VK_RMENU = 0xA5;

    // #if(_WIN32_WINNT >= 0x0500)
    int VK_BROWSER_BACK = 0xA6;
    int VK_BROWSER_FORWARD = 0xA7;
    int VK_BROWSER_REFRESH = 0xA8;
    int VK_BROWSER_STOP = 0xA9;
    int VK_BROWSER_SEARCH = 0xAA;
    int VK_BROWSER_FAVORITES = 0xAB;
    int VK_BROWSER_HOME = 0xAC;

    int VK_VOLUME_MUTE = 0xAD;
    int VK_VOLUME_DOWN = 0xAE;
    int VK_VOLUME_UP = 0xAF;
    int VK_MEDIA_NEXT_TRACK = 0xB0;
    int VK_MEDIA_PREV_TRACK = 0xB1;
    int VK_MEDIA_STOP = 0xB2;
    int VK_MEDIA_PLAY_PAUSE = 0xB3;
    int VK_LAUNCH_MAIL = 0xB4;
    int VK_LAUNCH_MEDIA_SELECT = 0xB5;
    int VK_LAUNCH_APP1 = 0xB6;
    int VK_LAUNCH_APP2 = 0xB7;

// #endif /* _WIN32_WINNT >= 0x0500 */

    /*
     * 0xB8 - 0xB9 : reserved
     */

    int VK_OEM_1 = 0xBA;   // ';:' for US
    int VK_OEM_PLUS = 0xBB;   // '+' any country
    int VK_OEM_COMMA = 0xBC;   // ',' any country
    int VK_OEM_MINUS = 0xBD;   // '-' any country
    int VK_OEM_PERIOD = 0xBE;   // '.' any country
    int VK_OEM_2 = 0xBF;   // '/?' for US
    int VK_OEM_3 = 0xC0;   // '`~' for US

    /*
     * 0xC1 - 0xD7 : reserved
     */

    /*
     * 0xD8 - 0xDA : unassigned
     */

    int VK_OEM_4 = 0xDB;  //  '[{' for US
    int VK_OEM_5 = 0xDC;  //  '\|' for US
    int VK_OEM_6 = 0xDD;  //  ']}' for US
    int VK_OEM_7 = 0xDE;  //  ''"' for US
    int VK_OEM_8 = 0xDF;

    /*
     * 0xE0 : reserved
     */

    /*
     * Various extended or enhanced keyboards
     */
    int VK_OEM_AX = 0xE1;  //  'AX' key on Japanese AX kbd
    int VK_OEM_102 = 0xE2;  //  "<>" or "\|" on RT 102-key kbd.
    int VK_ICO_HELP = 0xE3;  //  Help key on ICO
    int VK_ICO_00 = 0xE4;  //  00 key on ICO

    // #if(WINVER >= 0x0400)
    int VK_PROCESSKEY = 0xE5;
// #endif /* WINVER >= 0x0400 */

    int VK_ICO_CLEAR = 0xE6;


    // #if(_WIN32_WINNT >= 0x0500)
    int VK_PACKET = 0xE7;
// #endif /* _WIN32_WINNT >= 0x0500 */

    /*
     * 0xE8 : unassigned
     */

    /*
     * Nokia/Ericsson definitions
     */
    int VK_OEM_RESET = 0xE9;
    int VK_OEM_JUMP = 0xEA;
    int VK_OEM_PA1 = 0xEB;
    int VK_OEM_PA2 = 0xEC;
    int VK_OEM_PA3 = 0xED;
    int VK_OEM_WSCTRL = 0xEE;
    int VK_OEM_CUSEL = 0xEF;
    int VK_OEM_ATTN = 0xF0;
    int VK_OEM_FINISH = 0xF1;
    int VK_OEM_COPY = 0xF2;
    int VK_OEM_AUTO = 0xF3;
    int VK_OEM_ENLW = 0xF4;
    int VK_OEM_BACKTAB = 0xF5;

    int VK_ATTN = 0xF6;
    int VK_CRSEL = 0xF7;
    int VK_EXSEL = 0xF8;
    int VK_EREOF = 0xF9;
    int VK_PLAY = 0xFA;
    int VK_ZOOM = 0xFB;
    int VK_NONAME = 0xFC;
    int VK_PA1 = 0xFD;
    int VK_OEM_CLEAR = 0xFE;

    /*
     * 0xFF : reserved
     */
}
