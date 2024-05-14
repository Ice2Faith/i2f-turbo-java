package i2f.natives.windows.consts.process;

import i2f.natives.windows.consts.access.WinStandardAccessType;

/**
 * @author Ice2Faith
 * @date 2024/5/11 16:03
 * @desc
 */
public interface WinOpenThreadDesiredAccess {

    int THREAD_TERMINATE = (0x0001);
    int THREAD_SUSPEND_RESUME = (0x0002);
    int THREAD_GET_CONTEXT = (0x0008);
    int THREAD_SET_CONTEXT = (0x0010);
    int THREAD_QUERY_INFORMATION = (0x0040);
    int THREAD_SET_INFORMATION = (0x0020);
    int THREAD_SET_THREAD_TOKEN = (0x0080);
    int THREAD_IMPERSONATE = (0x0100);
    int THREAD_DIRECT_IMPERSONATION = (0x0200);
    // begin_wdm
    int THREAD_SET_LIMITED_INFORMATION = (0x0400);  // winnt
    int THREAD_QUERY_LIMITED_INFORMATION = (0x0800);  // winnt
    // #if (NTDDI_VERSION >= NTDDI_VISTA)
    int THREAD_ALL_ACCESS = (WinStandardAccessType.STANDARD_RIGHTS_REQUIRED | WinStandardAccessType.SYNCHRONIZE |
            0xFFFF);
// #else
//int THREAD_ALL_ACCESS         =(WinStandardAccessType.STANDARD_RIGHTS_REQUIRED | WinStandardAccessType.SYNCHRONIZE |
//0x3FF);
// #endif
}
