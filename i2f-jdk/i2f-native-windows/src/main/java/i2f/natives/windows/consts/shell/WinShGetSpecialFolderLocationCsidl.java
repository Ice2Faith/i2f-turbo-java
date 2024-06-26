package i2f.natives.windows.consts.shell;

/**
 * @author Ice2Faith
 * @date 2024/5/12 15:11
 * @desc
 */
public interface WinShGetSpecialFolderLocationCsidl {

    int CSIDL_DESKTOP = 0x0000;        // <desktop>
    int CSIDL_INTERNET = 0x0001;        // Internet Explorer (icon on desktop)
    int CSIDL_PROGRAMS = 0x0002;        // Start Menu\Programs
    int CSIDL_CONTROLS = 0x0003;        // My Computer\Control Panel
    int CSIDL_PRINTERS = 0x0004;        // My Computer\Printers
    int CSIDL_PERSONAL = 0x0005;        // My Documents
    int CSIDL_FAVORITES = 0x0006;        // <user name>\Favorites
    int CSIDL_STARTUP = 0x0007;        // Start Menu\Programs\Startup
    int CSIDL_RECENT = 0x0008;        // <user name>\Recent
    int CSIDL_SENDTO = 0x0009;        // <user name>\SendTo
    int CSIDL_BITBUCKET = 0x000a;        // <desktop>\Recycle Bin
    int CSIDL_STARTMENU = 0x000b;        // <user name>\Start Menu
    int CSIDL_MYDOCUMENTS = CSIDL_PERSONAL; //  Personal was just a silly name for My Documents
    int CSIDL_MYMUSIC = 0x000d;        // "My Music" folder
    int CSIDL_MYVIDEO = 0x000e;        // "My Videos" folder
    int CSIDL_DESKTOPDIRECTORY = 0x0010;        // <user name>\Desktop
    int CSIDL_DRIVES = 0x0011;        // My Computer
    int CSIDL_NETWORK = 0x0012;        // Network Neighborhood (My Network Places)
    int CSIDL_NETHOOD = 0x0013;        // <user name>\nethood
    int CSIDL_FONTS = 0x0014;        // windows\fonts
    int CSIDL_TEMPLATES = 0x0015;
    int CSIDL_COMMON_STARTMENU = 0x0016;        // All Users\Start Menu
    int CSIDL_COMMON_PROGRAMS = 0X0017;        // All Users\Start Menu\Programs
    int CSIDL_COMMON_STARTUP = 0x0018;        // All Users\Startup
    int CSIDL_COMMON_DESKTOPDIRECTORY = 0x0019;        // All Users\Desktop
    int CSIDL_APPDATA = 0x001a;        // <user name>\Application Data
    int CSIDL_PRINTHOOD = 0x001b;        // <user name>\PrintHood

    // #ifndef CSIDL_LOCAL_APPDATA
    int CSIDL_LOCAL_APPDATA = 0x001c;        // <user name>\Local Settings\Applicaiton Data (non roaming)
// #endif // CSIDL_LOCAL_APPDATA

    int CSIDL_ALTSTARTUP = 0x001d;        // non localized startup
    int CSIDL_COMMON_ALTSTARTUP = 0x001e;        // non localized common startup
    int CSIDL_COMMON_FAVORITES = 0x001f;

    // #ifndef _SHFOLDER_H_
    int CSIDL_INTERNET_CACHE = 0x0020;
    int CSIDL_COOKIES = 0x0021;
    int CSIDL_HISTORY = 0x0022;
    int CSIDL_COMMON_APPDATA = 0x0023;        // All Users\Application Data
    int CSIDL_WINDOWS = 0x0024;        // GetWindowsDirectory()
    int CSIDL_SYSTEM = 0x0025;        // GetSystemDirectory()
    int CSIDL_PROGRAM_FILES = 0x0026;        // C:\Program Files
    int CSIDL_MYPICTURES = 0x0027;        // C:\Program Files\My Pictures
// #endif // _SHFOLDER_H_

    int CSIDL_PROFILE = 0x0028;        // USERPROFILE
    int CSIDL_SYSTEMX86 = 0x0029;        // x86 system directory on RISC
    int CSIDL_PROGRAM_FILESX86 = 0x002a;        // x86 C:\Program Files on RISC

    // #ifndef _SHFOLDER_H_
    int CSIDL_PROGRAM_FILES_COMMON = 0x002b;        // C:\Program Files\Common
// #endif // _SHFOLDER_H_

    int CSIDL_PROGRAM_FILES_COMMONX86 = 0x002c;        // x86 Program Files\Common on RISC
    int CSIDL_COMMON_TEMPLATES = 0x002d;        // All Users\Templates

    // #ifndef _SHFOLDER_H_
    int CSIDL_COMMON_DOCUMENTS = 0x002e;        // All Users\Documents
    int CSIDL_COMMON_ADMINTOOLS = 0x002f;        // All Users\Start Menu\Programs\Administrative Tools
    int CSIDL_ADMINTOOLS = 0x0030;        // <user name>\Start Menu\Programs\Administrative Tools
// #endif // _SHFOLDER_H_

    int CSIDL_CONNECTIONS = 0x0031;        // Network and Dial-up Connections
    int CSIDL_COMMON_MUSIC = 0x0035;        // All Users\My Music
    int CSIDL_COMMON_PICTURES = 0x0036;        // All Users\My Pictures
    int CSIDL_COMMON_VIDEO = 0x0037;        // All Users\My Video
    int CSIDL_RESOURCES = 0x0038;        // Resource Direcotry

    // #ifndef _SHFOLDER_H_
    int CSIDL_RESOURCES_LOCALIZED = 0x0039;        // Localized Resource Direcotry
// #endif // _SHFOLDER_H_

    int CSIDL_COMMON_OEM_LINKS = 0x003a;        // Links to All Users OEM specific apps
    int CSIDL_CDBURN_AREA = 0x003b;        // USERPROFILE\Local Settings\Application Data\Microsoft\CD Burning
    // unused                               0x003c
    int CSIDL_COMPUTERSNEARME = 0x003d;        // Computers Near Me (computered from Workgroup membership)

    // #ifndef _SHFOLDER_H_
    int CSIDL_FLAG_CREATE = 0x8000;        // combine with CSIDL_ value to force folder creation in SHGetFolderPath()
// #endif // _SHFOLDER_H_

    int CSIDL_FLAG_DONT_VERIFY = 0x4000;        // combine with CSIDL_ value to return an unverified folder path
    int CSIDL_FLAG_DONT_UNEXPAND = 0x2000;        // combine with CSIDL_ value to avoid unexpanding environment variables
    // #if (NTDDI_VERSION >= NTDDI_WINXP)
    int CSIDL_FLAG_NO_ALIAS = 0x1000;        // combine with CSIDL_ value to insure non-alias versions of the pidl
    int CSIDL_FLAG_PER_USER_INIT = 0x0800;        // combine with CSIDL_ value to indicate per-user init (eg. upgrade)
    // #endif  // NTDDI_WINXP
    int CSIDL_FLAG_MASK = 0xFF00;        // mask for all possible flag values


    String IE_QUICK_LUNCH_SUB_PATH = "\\Microsoft\\Internet Explorer\\Quick Launch";
    int CSIDL_IE_QUICK_LUNCH = CSIDL_APPDATA;
}
