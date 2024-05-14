package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/7 16:16
 * @desc
 */
public interface WinSystemMetrics {

    int SM_CXSCREEN = 0;
    int SM_CYSCREEN = 1;
    int SM_CXVSCROLL = 2;
    int SM_CYHSCROLL = 3;
    int SM_CYCAPTION = 4;
    int SM_CXBORDER = 5;
    int SM_CYBORDER = 6;
    int SM_CXDLGFRAME = 7;
    int SM_CYDLGFRAME = 8;
    int SM_CYVTHUMB = 9;
    int SM_CXHTHUMB = 10;
    int SM_CXICON = 11;
    int SM_CYICON = 12;
    int SM_CXCURSOR = 13;
    int SM_CYCURSOR = 14;
    int SM_CYMENU = 15;
    int SM_CXFULLSCREEN = 16;
    int SM_CYFULLSCREEN = 17;
    int SM_CYKANJIWINDOW = 18;
    int SM_MOUSEPRESENT = 19;
    int SM_CYVSCROLL = 20;
    int SM_CXHSCROLL = 21;
    int SM_DEBUG = 22;
    int SM_SWAPBUTTON = 23;
    int SM_RESERVED1 = 24;
    int SM_RESERVED2 = 25;
    int SM_RESERVED3 = 26;
    int SM_RESERVED4 = 27;
    int SM_CXMIN = 28;
    int SM_CYMIN = 29;
    int SM_CXSIZE = 30;
    int SM_CYSIZE = 31;
    int SM_CXFRAME = 32;
    int SM_CYFRAME = 33;
    int SM_CXMINTRACK = 34;
    int SM_CYMINTRACK = 35;
    int SM_CXDOUBLECLK = 36;
    int SM_CYDOUBLECLK = 37;
    int SM_CXICONSPACING = 38;
    int SM_CYICONSPACING = 39;
    int SM_MENUDROPALIGNMENT = 40;
    int SM_PENWINDOWS = 41;
    int SM_DBCSENABLED = 42;
    int SM_CMOUSEBUTTONS = 43;

    // #if(WINVER >= 0x0400)
    int SM_CXFIXEDFRAME = SM_CXDLGFRAME;  /* ;win40 name change */
    int SM_CYFIXEDFRAME = SM_CYDLGFRAME;  /* ;win40 name change */
    int SM_CXSIZEFRAME = SM_CXFRAME;  /* ;win40 name change */
    int SM_CYSIZEFRAME = SM_CYFRAME;  /* ;win40 name change */

    int SM_SECURE = 44;
    int SM_CXEDGE = 45;
    int SM_CYEDGE = 46;
    int SM_CXMINSPACING = 47;
    int SM_CYMINSPACING = 48;
    int SM_CXSMICON = 49;
    int SM_CYSMICON = 50;
    int SM_CYSMCAPTION = 51;
    int SM_CXSMSIZE = 52;
    int SM_CYSMSIZE = 53;
    int SM_CXMENUSIZE = 54;
    int SM_CYMENUSIZE = 55;
    int SM_ARRANGE = 56;
    int SM_CXMINIMIZED = 57;
    int SM_CYMINIMIZED = 58;
    int SM_CXMAXTRACK = 59;
    int SM_CYMAXTRACK = 60;
    int SM_CXMAXIMIZED = 61;
    int SM_CYMAXIMIZED = 62;
    int SM_NETWORK = 63;
    int SM_CLEANBOOT = 67;
    int SM_CXDRAG = 68;
    int SM_CYDRAG = 69;
// #endif /* WINVER >= 0x0400 */

    int SM_SHOWSOUNDS = 70;

    // #if(WINVER >= 0x0400)
    int SM_CXMENUCHECK = 71;   /* Use instead of GetMenuCheckMarkDimensions()! */
    int SM_CYMENUCHECK = 72;
    int SM_SLOWMACHINE = 73;
    int SM_MIDEASTENABLED = 74;
// #endif /* WINVER >= 0x0400 */

    // #if (WINVER >= 0x0500) || (_WIN32_WINNT >= 0x0400)
    int SM_MOUSEWHEELPRESENT = 75;
// #endif

    // #if(WINVER >= 0x0500)
    int SM_XVIRTUALSCREEN = 76;
    int SM_YVIRTUALSCREEN = 77;
    int SM_CXVIRTUALSCREEN = 78;
    int SM_CYVIRTUALSCREEN = 79;
    int SM_CMONITORS = 80;
    int SM_SAMEDISPLAYFORMAT = 81;
// #endif /* WINVER >= 0x0500 */

    // #if(_WIN32_WINNT >= 0x0500)
    int SM_IMMENABLED = 82;
// #endif /* _WIN32_WINNT >= 0x0500 */

    // #if(_WIN32_WINNT >= 0x0501)
    int SM_CXFOCUSBORDER = 83;
    int SM_CYFOCUSBORDER = 84;
// #endif /* _WIN32_WINNT >= 0x0501 */

    // #if(_WIN32_WINNT >= 0x0501)
    int SM_TABLETPC = 86;
    int SM_MEDIACENTER = 87;
    int SM_STARTER = 88;
    int SM_SERVERR2 = 89;
// #endif /* _WIN32_WINNT >= 0x0501 */

    // #if(_WIN32_WINNT >= 0x0600)
    int SM_MOUSEHORIZONTALWHEELPRESENT = 91;
    int SM_CXPADDEDBORDER = 92;
// #endif /* _WIN32_WINNT >= 0x0600 */

    // #if(WINVER >= 0x0601)
    int SM_DIGITIZER = 94;
    int SM_MAXIMUMTOUCHES = 95;
// #endif /* WINVER >= 0x0601 */

    // #if (WINVER < 0x0500) && (!defined(_WIN32_WINNT) || (_WIN32_WINNT < 0x0400))
    int SM_CMETRICS = 76;
    // #elif WINVER == 0x500
//    int SM_CMETRICS = 83;
    // #elif WINVER == 0x501
//    int SM_CMETRICS = 91;
    // #elif WINVER == 0x600
//    int SM_CMETRICS = 93;
    // #else
//    int SM_CMETRICS = 97;
// #endif

    // #if(WINVER >= 0x0500)
    int SM_REMOTESESSION = 0x1000;


    // #if(_WIN32_WINNT >= 0x0501)
    int SM_SHUTTINGDOWN = 0x2000;
// #endif /* _WIN32_WINNT >= 0x0501 */

    // #if(WINVER >= 0x0501)
    int SM_REMOTECONTROL = 0x2001;
// #endif /* WINVER >= 0x0501 */

    // #if(WINVER >= 0x0501)
    int SM_CARETBLINKINGENABLED = 0x2002;
// #endif /* WINVER >= 0x0501 */

// #endif /* WINVER >= 0x0500 */
}
