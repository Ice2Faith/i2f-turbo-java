package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/9 11:19
 * @desc
 */
public interface WinOpenProcessDesiredAccess {

    int PROCESS_TERMINATE = 0x0001;
    int PROCESS_CREATE_THREAD = 0x0002;
    int PROCESS_SET_SESSIONID = 0x0004;
    int PROCESS_VM_OPERATION = 0x0008;
    int PROCESS_VM_READ = 0x0010;
    int PROCESS_VM_WRITE = 0x0020;
    int PROCESS_DUP_HANDLE = 0x0040;
    int PROCESS_CREATE_PROCESS = 0x0080;
    int PROCESS_SET_QUOTA = 0x0100;
    int PROCESS_SET_INFORMATION = 0x0200;
    int PROCESS_QUERY_INFORMATION = 0x0400;
    int PROCESS_SUSPEND_RESUME = 0x0800;
    int PROCESS_QUERY_LIMITED_INFORMATION = 0x1000;
    //#if (NTDDI_VERSION >= NTDDI_VISTA)
    int PROCESS_ALL_ACCESS = (WinStandardAccessType.STANDARD_RIGHTS_REQUIRED | WinStandardAccessType.SYNCHRONIZE | 0xFFFF);
// #else
// int PROCESS_ALL_ACCESS        (WinStandardAccessType.STANDARD_RIGHTS_REQUIRED | WinStandardAccessType.SYNCHRONIZE | 0xFFF);
// #endif

}
