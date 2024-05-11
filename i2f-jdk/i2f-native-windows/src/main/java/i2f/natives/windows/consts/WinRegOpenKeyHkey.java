package i2f.natives.windows.consts;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/5/10 16:01
 * @desc
 */
public interface WinRegOpenKeyHkey {

    long HKEY_CLASSES_ROOT = 0x80000000L;
    long HKEY_CURRENT_USER = 0x80000001L;
    long HKEY_LOCAL_MACHINE = 0x80000002L;
    long HKEY_USERS = 0x80000003L;
    long HKEY_PERFORMANCE_DATA = 0x80000004L;
    long HKEY_PERFORMANCE_TEXT = 0x80000050L;
    long HKEY_PERFORMANCE_NLSTEXT = 0x80000060L;
    // #if(WINVER >= 0x0400)
    long HKEY_CURRENT_CONFIG = 0x80000005L;
    long HKEY_DYN_DATA = 0x80000006L;
    long HKEY_CURRENT_USER_LOCAL_SETTINGS = 0x80000007L;

    Map<Long, String> NAME_MAP = Collections.unmodifiableMap(new LinkedHashMap<Long, String>() {
        {
            put(HKEY_CLASSES_ROOT, "HKEY_CLASSES_ROOT");
            put(HKEY_CURRENT_USER, "HKEY_CURRENT_USER");
            put(HKEY_LOCAL_MACHINE, "HKEY_LOCAL_MACHINE");
            put(HKEY_USERS, "HKEY_USERS");
            put(HKEY_PERFORMANCE_DATA, "HKEY_PERFORMANCE_DATA");
            put(HKEY_PERFORMANCE_TEXT, "HKEY_PERFORMANCE_TEXT");
            put(HKEY_PERFORMANCE_NLSTEXT, "HKEY_PERFORMANCE_NLSTEXT");
            put(HKEY_CURRENT_CONFIG, "HKEY_CURRENT_CONFIG");
            put(HKEY_DYN_DATA, "HKEY_DYN_DATA");
            put(HKEY_CURRENT_USER_LOCAL_SETTINGS, "HKEY_CURRENT_USER_LOCAL_SETTINGS");
        }
    });

}
