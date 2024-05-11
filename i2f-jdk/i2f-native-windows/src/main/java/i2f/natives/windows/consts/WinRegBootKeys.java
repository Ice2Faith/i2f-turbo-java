package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/11 10:18
 * @desc
 */
public interface WinRegBootKeys {
    long[] ROOT_HKEYS = {
            WinRegOpenKeyHkey.HKEY_CURRENT_USER,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,

            WinRegOpenKeyHkey.HKEY_CURRENT_USER,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,

            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,
            WinRegOpenKeyHkey.HKEY_CURRENT_USER,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,
            WinRegOpenKeyHkey.HKEY_CURRENT_USER,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,
            WinRegOpenKeyHkey.HKEY_CURRENT_USER,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,
            WinRegOpenKeyHkey.HKEY_CURRENT_USER,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,
            WinRegOpenKeyHkey.HKEY_CURRENT_USER,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,
            WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE,
    };

    String[] BOOT_PATHS = {
            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\",
            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\",

            "Software\\Microsoft\\Windows NT\\CurrentVersion\\Windows\\Load\\",
            "SYSTEM\\CurrentControlSet\\Control\\Session Manager\\BootExecute\\",
            "SYSTEM\\CurrentControlSet\\Control\\Session Manager\\SetupExecute\\",
            "SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Execute\\",
            "SYSTEM\\CurrentControlSet\\Control\\Session Manager\\S0InitialCommand\\",

            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\RunOnce\\",
            "Software\\Microsoft\\Windows\\CurrentVersion\\RunOnce\\",
            "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Winlogon\\Userinit\\",
            "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\Explorer\\Run\\",
            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Policies\\Explorer\\Run\\",
            "Software\\Microsoft\\Windows\\CurrentVersion\\RunServicesOnce\\",
            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\RunServicesOnce\\",
            "Software\\Microsoft\\Windows\\CurrentVersion\\RunServices\\",
            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\RunServices\\",
            "Software\\Microsoft\\Windows\\CurrentVersion\\RunOnce\\Setup\\",
            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\RunOnce\\Setup\\",
            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\RunOnceEx\\",
    };
}
