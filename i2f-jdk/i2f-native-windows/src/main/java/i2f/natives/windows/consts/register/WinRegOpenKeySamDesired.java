package i2f.natives.windows.consts.register;

import i2f.natives.windows.consts.access.WinStandardAccessType;

/**
 * @author Ice2Faith
 * @date 2024/5/10 16:06
 * @desc
 */
public interface WinRegOpenKeySamDesired {

    int KEY_QUERY_VALUE = 0x0001;
    int KEY_SET_VALUE = 0x0002;
    int KEY_CREATE_SUB_KEY = 0x0004;
    int KEY_ENUMERATE_SUB_KEYS = 0x0008;
    int KEY_NOTIFY = 0x0010;
    int KEY_CREATE_LINK = 0x0020;
    int KEY_WOW64_32KEY = 0x0200;
    int KEY_WOW64_64KEY = 0x0100;
    int KEY_WOW64_RES = 0x0300;

    int KEY_READ = ((WinStandardAccessType.STANDARD_RIGHTS_READ |
            KEY_QUERY_VALUE |
            KEY_ENUMERATE_SUB_KEYS |
            KEY_NOTIFY)
            &
            (~WinStandardAccessType.SYNCHRONIZE));


    int KEY_WRITE = ((WinStandardAccessType.STANDARD_RIGHTS_WRITE |
            KEY_SET_VALUE |
            KEY_CREATE_SUB_KEY)
            &
            (~WinStandardAccessType.SYNCHRONIZE));

    int KEY_EXECUTE = ((KEY_READ)
            &
            (~WinStandardAccessType.SYNCHRONIZE));

    int KEY_ALL_ACCESS = ((WinStandardAccessType.STANDARD_RIGHTS_ALL |
            KEY_QUERY_VALUE |
            KEY_SET_VALUE |
            KEY_CREATE_SUB_KEY |
            KEY_ENUMERATE_SUB_KEYS |
            KEY_NOTIFY |
            KEY_CREATE_LINK)
            &
            (~WinStandardAccessType.SYNCHRONIZE));

}
