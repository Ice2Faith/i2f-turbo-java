package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/9 10:04
 * @desc
 */
public interface WinSendMessageMsg {

    int WM_NULL = 0x0000;
    int WM_CREATE = 0x0001;
    int WM_DESTROY = 0x0002;
    int WM_MOVE = 0x0003;
    int WM_SIZE = 0x0005;

    int WM_ACTIVATE = 0x0006;
    /*
     * WM_ACTIVATE state values
     */
    int WA_INACTIVE = 0;
    int WA_ACTIVE = 1;
    int WA_CLICKACTIVE = 2;

    int WM_SETFOCUS = 0x0007;
    int WM_KILLFOCUS = 0x0008;
    int WM_ENABLE = 0x000A;
    int WM_SETREDRAW = 0x000B;
    int WM_SETTEXT = 0x000C;
    int WM_GETTEXT = 0x000D;
    int WM_GETTEXTLENGTH = 0x000E;
    int WM_PAINT = 0x000F;
    int WM_CLOSE = 0x0010;
    // #ifndef _WIN32_WCE
    int WM_QUERYENDSESSION = 0x0011;
    int WM_QUERYOPEN = 0x0013;
    int WM_ENDSESSION = 0x0016;
    // #endif
    int WM_QUIT = 0x0012;
    int WM_ERASEBKGND = 0x0014;
    int WM_SYSCOLORCHANGE = 0x0015;
    int WM_SHOWWINDOW = 0x0018;
    int WM_WININICHANGE = 0x001A;
    // #if(WINVER >= 0x0400)
    int WM_SETTINGCHANGE = WM_WININICHANGE;
// #endif /* WINVER >= 0x0400 */


    int WM_DEVMODECHANGE = 0x001B;
    int WM_ACTIVATEAPP = 0x001C;
    int WM_FONTCHANGE = 0x001D;
    int WM_TIMECHANGE = 0x001E;
    int WM_CANCELMODE = 0x001F;
    int WM_SETCURSOR = 0x0020;
    int WM_MOUSEACTIVATE = 0x0021;
    int WM_CHILDACTIVATE = 0x0022;
    int WM_QUEUESYNC = 0x0023;

    int WM_GETMINMAXINFO = 0x0024;


    int WM_PAINTICON = 0x0026;
    int WM_ICONERASEBKGND = 0x0027;
    int WM_NEXTDLGCTL = 0x0028;
    int WM_SPOOLERSTATUS = 0x002A;
    int WM_DRAWITEM = 0x002B;
    int WM_MEASUREITEM = 0x002C;
    int WM_DELETEITEM = 0x002D;
    int WM_VKEYTOITEM = 0x002E;
    int WM_CHARTOITEM = 0x002F;
    int WM_SETFONT = 0x0030;
    int WM_GETFONT = 0x0031;
    int WM_SETHOTKEY = 0x0032;
    int WM_GETHOTKEY = 0x0033;
    int WM_QUERYDRAGICON = 0x0037;
    int WM_COMPAREITEM = 0x0039;
    // #if(WINVER >= 0x0500)
// #ifndef _WIN32_WCE
    int WM_GETOBJECT = 0x003D;
    // #endif
// #endif /* WINVER >= 0x0500 */
    int WM_COMPACTING = 0x0041;
    int WM_COMMNOTIFY = 0x0044;  /* no longer suported */
    int WM_WINDOWPOSCHANGING = 0x0046;
    int WM_WINDOWPOSCHANGED = 0x0047;

    int WM_POWER = 0x0048;
    /*
     * wParam for WM_POWER window message and DRV_POWER driver notification
     */
    int PWR_OK = 1;
    int PWR_FAIL = (-1);
    int PWR_SUSPENDREQUEST = 1;
    int PWR_SUSPENDRESUME = 2;
    int PWR_CRITICALRESUME = 3;

    int WM_COPYDATA = 0x004A;
    int WM_CANCELJOURNAL = 0x004B;


    // #if(WINVER >= 0x0400)
    int WM_NOTIFY = 0x004E;
    int WM_INPUTLANGCHANGEREQUEST = 0x0050;
    int WM_INPUTLANGCHANGE = 0x0051;
    int WM_TCARD = 0x0052;
    int WM_HELP = 0x0053;
    int WM_USERCHANGED = 0x0054;
    int WM_NOTIFYFORMAT = 0x0055;

    int NFR_ANSI = 1;
    int NFR_UNICODE = 2;
    int NF_QUERY = 3;
    int NF_REQUERY = 4;

    int WM_CONTEXTMENU = 0x007B;
    int WM_STYLECHANGING = 0x007C;
    int WM_STYLECHANGED = 0x007D;
    int WM_DISPLAYCHANGE = 0x007E;
    int WM_GETICON = 0x007F;
    int WM_SETICON = 0x0080;
// #endif /* WINVER >= 0x0400 */

    int WM_NCCREATE = 0x0081;
    int WM_NCDESTROY = 0x0082;
    int WM_NCCALCSIZE = 0x0083;
    int WM_NCHITTEST = 0x0084;
    int WM_NCPAINT = 0x0085;
    int WM_NCACTIVATE = 0x0086;
    int WM_GETDLGCODE = 0x0087;
    // #ifndef _WIN32_WCE
    int WM_SYNCPAINT = 0x0088;
    // #endif
    int WM_NCMOUSEMOVE = 0x00A0;
    int WM_NCLBUTTONDOWN = 0x00A1;
    int WM_NCLBUTTONUP = 0x00A2;
    int WM_NCLBUTTONDBLCLK = 0x00A3;
    int WM_NCRBUTTONDOWN = 0x00A4;
    int WM_NCRBUTTONUP = 0x00A5;
    int WM_NCRBUTTONDBLCLK = 0x00A6;
    int WM_NCMBUTTONDOWN = 0x00A7;
    int WM_NCMBUTTONUP = 0x00A8;
    int WM_NCMBUTTONDBLCLK = 0x00A9;


    // #if(_WIN32_WINNT >= 0x0500)
    int WM_NCXBUTTONDOWN = 0x00AB;
    int WM_NCXBUTTONUP = 0x00AC;
    int WM_NCXBUTTONDBLCLK = 0x00AD;
// #endif /* _WIN32_WINNT >= 0x0500 */


    // #if(_WIN32_WINNT >= 0x0501)
    int WM_INPUT_DEVICE_CHANGE = 0x00FE;
// #endif /* _WIN32_WINNT >= 0x0501 */

    // #if(_WIN32_WINNT >= 0x0501)
    int WM_INPUT = 0x00FF;
// #endif /* _WIN32_WINNT >= 0x0501 */

    int WM_KEYFIRST = 0x0100;
    int WM_KEYDOWN = 0x0100;
    int WM_KEYUP = 0x0101;
    int WM_CHAR = 0x0102;
    int WM_DEADCHAR = 0x0103;
    int WM_SYSKEYDOWN = 0x0104;
    int WM_SYSKEYUP = 0x0105;
    int WM_SYSCHAR = 0x0106;
    int WM_SYSDEADCHAR = 0x0107;
    // #if(_WIN32_WINNT >= 0x0501)
    int WM_UNICHAR = 0x0109;
    int WM_KEYLAST = 0x0109;
    int UNICODE_NOCHAR = 0xFFFF;
// #else
// int WM_KEYLAST                      =0x0108;
// #endif /* _WIN32_WINNT >= 0x0501 */

    // #if(WINVER >= 0x0400)
    int WM_IME_STARTCOMPOSITION = 0x010D;
    int WM_IME_ENDCOMPOSITION = 0x010E;
    int WM_IME_COMPOSITION = 0x010F;
    int WM_IME_KEYLAST = 0x010F;
// #endif /* WINVER >= 0x0400 */

    int WM_INITDIALOG = 0x0110;
    int WM_COMMAND = 0x0111;
    int WM_SYSCOMMAND = 0x0112;
    int WM_TIMER = 0x0113;
    int WM_HSCROLL = 0x0114;
    int WM_VSCROLL = 0x0115;
    int WM_INITMENU = 0x0116;
    int WM_INITMENUPOPUP = 0x0117;
    // #if(WINVER >= 0x0601)
    int WM_GESTURE = 0x0119;
    int WM_GESTURENOTIFY = 0x011A;
    // #endif /* WINVER >= 0x0601 */
    int WM_MENUSELECT = 0x011F;
    int WM_MENUCHAR = 0x0120;
    int WM_ENTERIDLE = 0x0121;
    // #if(WINVER >= 0x0500)
// #ifndef _WIN32_WCE
    int WM_MENURBUTTONUP = 0x0122;
    int WM_MENUDRAG = 0x0123;
    int WM_MENUGETOBJECT = 0x0124;
    int WM_UNINITMENUPOPUP = 0x0125;
    int WM_MENUCOMMAND = 0x0126;

    // #ifndef _WIN32_WCE
// #if(_WIN32_WINNT >= 0x0500)
    int WM_CHANGEUISTATE = 0x0127;
    int WM_UPDATEUISTATE = 0x0128;
    int WM_QUERYUISTATE = 0x0129;

    /*
     * LOWORD(wParam) values in WM_*UISTATE*
     */
    int UIS_SET = 1;
    int UIS_CLEAR = 2;
    int UIS_INITIALIZE = 3;

    /*
     * HIWORD(wParam) values in WM_*UISTATE*
     */
    int UISF_HIDEFOCUS = 0x1;
    int UISF_HIDEACCEL = 0x2;
    // #if(_WIN32_WINNT >= 0x0501)
    int UISF_ACTIVE = 0x4;
// #endif /* _WIN32_WINNT >= 0x0501 */
// #endif /* _WIN32_WINNT >= 0x0500 */
// #endif

// #endif
// #endif /* WINVER >= 0x0500 */

    int WM_CTLCOLORMSGBOX = 0x0132;
    int WM_CTLCOLOREDIT = 0x0133;
    int WM_CTLCOLORLISTBOX = 0x0134;
    int WM_CTLCOLORBTN = 0x0135;
    int WM_CTLCOLORDLG = 0x0136;
    int WM_CTLCOLORSCROLLBAR = 0x0137;
    int WM_CTLCOLORSTATIC = 0x0138;
    int MN_GETHMENU = 0x01E1;

    int WM_MOUSEFIRST = 0x0200;
    int WM_MOUSEMOVE = 0x0200;
    int WM_LBUTTONDOWN = 0x0201;
    int WM_LBUTTONUP = 0x0202;
    int WM_LBUTTONDBLCLK = 0x0203;
    int WM_RBUTTONDOWN = 0x0204;
    int WM_RBUTTONUP = 0x0205;
    int WM_RBUTTONDBLCLK = 0x0206;
    int WM_MBUTTONDOWN = 0x0207;
    int WM_MBUTTONUP = 0x0208;
    int WM_MBUTTONDBLCLK = 0x0209;
    // #if (_WIN32_WINNT >= 0x0400) || (_WIN32_WINDOWS > 0x0400)
    int WM_MOUSEWHEEL = 0x020A;
    // #endif
// #if (_WIN32_WINNT >= 0x0500)
    int WM_XBUTTONDOWN = 0x020B;
    int WM_XBUTTONUP = 0x020C;
    int WM_XBUTTONDBLCLK = 0x020D;
    // #endif
// #if (_WIN32_WINNT >= 0x0600)
    int WM_MOUSEHWHEEL = 0x020E;
// #endif

    // #if (_WIN32_WINNT >= 0x0600)
    int WM_MOUSELAST = 0x020E;
// #elif (_WIN32_WINNT >= 0x0500)
// int WM_MOUSELAST                    =0x020D;
// #elif (_WIN32_WINNT >= 0x0400) || (_WIN32_WINDOWS > 0x0400)
// int WM_MOUSELAST                    =0x020A;
// #else
// int WM_MOUSELAST                    =0x0209;
// #endif /* (_WIN32_WINNT >= 0x0600) */


    // #if(_WIN32_WINNT >= 0x0400)
    /* Value for rolling one detent */
    int WHEEL_DELTA = 120;
// #define GET_WHEEL_DELTA_WPARAM(wParam)  ((short)HIWORD(wParam))

    /* Setting to scroll one page for SPI_GET/SETWHEELSCROLLLINES */
    int UINT_MAX = 0xffffffff;    /* maximum unsigned int value */
    int WHEEL_PAGESCROLL = UINT_MAX;
// #endif /* _WIN32_WINNT >= 0x0400 */

// #if(_WIN32_WINNT >= 0x0500)
// #define GET_KEYSTATE_WPARAM(wParam)     (LOWORD(wParam))
// #define GET_NCHITTEST_WPARAM(wParam)    ((short)LOWORD(wParam))
// #define GET_XBUTTON_WPARAM(wParam)      (HIWORD(wParam))

    /* XButton values are WORD flags */
    int XBUTTON1 = 0x0001;
    int XBUTTON2 = 0x0002;
    /* Were there to be an XBUTTON3, its value would be 0x0004 */
// #endif /* _WIN32_WINNT >= 0x0500 */

    int WM_PARENTNOTIFY = 0x0210;
    int WM_ENTERMENULOOP = 0x0211;
    int WM_EXITMENULOOP = 0x0212;

    // #if(WINVER >= 0x0400)
    int WM_NEXTMENU = 0x0213;
    int WM_SIZING = 0x0214;
    int WM_CAPTURECHANGED = 0x0215;
    int WM_MOVING = 0x0216;
// #endif /* WINVER >= 0x0400 */

// #if(WINVER >= 0x0400)


    int WM_POWERBROADCAST = 0x0218;

    // #ifndef _WIN32_WCE
    int PBT_APMQUERYSUSPEND = 0x0000;
    int PBT_APMQUERYSTANDBY = 0x0001;

    int PBT_APMQUERYSUSPENDFAILED = 0x0002;
    int PBT_APMQUERYSTANDBYFAILED = 0x0003;

    int PBT_APMSUSPEND = 0x0004;
    int PBT_APMSTANDBY = 0x0005;

    int PBT_APMRESUMECRITICAL = 0x0006;
    int PBT_APMRESUMESUSPEND = 0x0007;
    int PBT_APMRESUMESTANDBY = 0x0008;

    int PBTF_APMRESUMEFROMFAILURE = 0x00000001;

    int PBT_APMBATTERYLOW = 0x0009;
    int PBT_APMPOWERSTATUSCHANGE = 0x000A;

    int PBT_APMOEMEVENT = 0x000B;


    int PBT_APMRESUMEAUTOMATIC = 0x0012;
    // #if (_WIN32_WINNT >= 0x0502)
// #ifndef PBT_POWERSETTINGCHANGE
    int PBT_POWERSETTINGCHANGE = 0x8013;


// #endif // PBT_POWERSETTINGCHANGE

// #endif // (_WIN32_WINNT >= 0x0502)
// #endif

// #endif /* WINVER >= 0x0400 */

    // #if(WINVER >= 0x0400)
    int WM_DEVICECHANGE = 0x0219;
// #endif /* WINVER >= 0x0400 */

    int WM_MDICREATE = 0x0220;
    int WM_MDIDESTROY = 0x0221;
    int WM_MDIACTIVATE = 0x0222;
    int WM_MDIRESTORE = 0x0223;
    int WM_MDINEXT = 0x0224;
    int WM_MDIMAXIMIZE = 0x0225;
    int WM_MDITILE = 0x0226;
    int WM_MDICASCADE = 0x0227;
    int WM_MDIICONARRANGE = 0x0228;
    int WM_MDIGETACTIVE = 0x0229;


    int WM_MDISETMENU = 0x0230;
    int WM_ENTERSIZEMOVE = 0x0231;
    int WM_EXITSIZEMOVE = 0x0232;
    int WM_DROPFILES = 0x0233;
    int WM_MDIREFRESHMENU = 0x0234;

    // #if(WINVER >= 0x0601)
    int WM_TOUCH = 0x0240;
// #endif /* WINVER >= 0x0601 */


    // #if(WINVER >= 0x0400)
    int WM_IME_SETCONTEXT = 0x0281;
    int WM_IME_NOTIFY = 0x0282;
    int WM_IME_CONTROL = 0x0283;
    int WM_IME_COMPOSITIONFULL = 0x0284;
    int WM_IME_SELECT = 0x0285;
    int WM_IME_CHAR = 0x0286;
    // #endif /* WINVER >= 0x0400 */
// #if(WINVER >= 0x0500)
    int WM_IME_REQUEST = 0x0288;
    // #endif /* WINVER >= 0x0500 */
// #if(WINVER >= 0x0400)
    int WM_IME_KEYDOWN = 0x0290;
    int WM_IME_KEYUP = 0x0291;
// #endif /* WINVER >= 0x0400 */

    // #if((_WIN32_WINNT >= 0x0400) || (WINVER >= 0x0500))
    int WM_MOUSEHOVER = 0x02A1;
    int WM_MOUSELEAVE = 0x02A3;
    // #endif
// #if(WINVER >= 0x0500)
    int WM_NCMOUSEHOVER = 0x02A0;
    int WM_NCMOUSELEAVE = 0x02A2;
// #endif /* WINVER >= 0x0500 */

    // #if(_WIN32_WINNT >= 0x0501)
    int WM_WTSSESSION_CHANGE = 0x02B1;

    int WM_TABLET_FIRST = 0x02c0;
    int WM_TABLET_LAST = 0x02df;
// #endif /* _WIN32_WINNT >= 0x0501 */

    int WM_CUT = 0x0300;
    int WM_COPY = 0x0301;
    int WM_PASTE = 0x0302;
    int WM_CLEAR = 0x0303;
    int WM_UNDO = 0x0304;
    int WM_RENDERFORMAT = 0x0305;
    int WM_RENDERALLFORMATS = 0x0306;
    int WM_DESTROYCLIPBOARD = 0x0307;
    int WM_DRAWCLIPBOARD = 0x0308;
    int WM_PAINTCLIPBOARD = 0x0309;
    int WM_VSCROLLCLIPBOARD = 0x030A;
    int WM_SIZECLIPBOARD = 0x030B;
    int WM_ASKCBFORMATNAME = 0x030C;
    int WM_CHANGECBCHAIN = 0x030D;
    int WM_HSCROLLCLIPBOARD = 0x030E;
    int WM_QUERYNEWPALETTE = 0x030F;
    int WM_PALETTEISCHANGING = 0x0310;
    int WM_PALETTECHANGED = 0x0311;
    int WM_HOTKEY = 0x0312;

    // #if(WINVER >= 0x0400)
    int WM_PRINT = 0x0317;
    int WM_PRINTCLIENT = 0x0318;
// #endif /* WINVER >= 0x0400 */

    // #if(_WIN32_WINNT >= 0x0500)
    int WM_APPCOMMAND = 0x0319;
// #endif /* _WIN32_WINNT >= 0x0500 */

    // #if(_WIN32_WINNT >= 0x0501)
    int WM_THEMECHANGED = 0x031A;
// #endif /* _WIN32_WINNT >= 0x0501 */


    // #if(_WIN32_WINNT >= 0x0501)
    int WM_CLIPBOARDUPDATE = 0x031D;
// #endif /* _WIN32_WINNT >= 0x0501 */

    // #if(_WIN32_WINNT >= 0x0600)
    int WM_DWMCOMPOSITIONCHANGED = 0x031E;
    int WM_DWMNCRENDERINGCHANGED = 0x031F;
    int WM_DWMCOLORIZATIONCOLORCHANGED = 0x0320;
    int WM_DWMWINDOWMAXIMIZEDCHANGE = 0x0321;
// #endif /* _WIN32_WINNT >= 0x0600 */

    // #if(_WIN32_WINNT >= 0x0601)
    int WM_DWMSENDICONICTHUMBNAIL = 0x0323;
    int WM_DWMSENDICONICLIVEPREVIEWBITMAP = 0x0326;
// #endif /* _WIN32_WINNT >= 0x0601 */


    // #if(WINVER >= 0x0600)
    int WM_GETTITLEBARINFOEX = 0x033F;
// #endif /* WINVER >= 0x0600 */

// #if(WINVER >= 0x0400)

    int WM_HANDHELDFIRST = 0x0358;
    int WM_HANDHELDLAST = 0x035F;

    int WM_AFXFIRST = 0x0360;
    int WM_AFXLAST = 0x037F;
// #endif /* WINVER >= 0x0400 */

    int WM_PENWINFIRST = 0x0380;
    int WM_PENWINLAST = 0x038F;


    // #if(WINVER >= 0x0400)
    int WM_APP = 0x8000;
// #endif /* WINVER >= 0x0400 */


    /*
     * NOTE: All Message Numbers below 0x0400 are RESERVED.
     *
     * Private Window Messages Start Here:
     */
    int WM_USER = 0x0400;

// #if(WINVER >= 0x0400)

    /*  wParam for WM_SIZING message  */
    int WMSZ_LEFT = 1;
    int WMSZ_RIGHT = 2;
    int WMSZ_TOP = 3;
    int WMSZ_TOPLEFT = 4;
    int WMSZ_TOPRIGHT = 5;
    int WMSZ_BOTTOM = 6;
    int WMSZ_BOTTOMLEFT = 7;
    int WMSZ_BOTTOMRIGHT = 8;
// #endif /* WINVER >= 0x0400 */

}
