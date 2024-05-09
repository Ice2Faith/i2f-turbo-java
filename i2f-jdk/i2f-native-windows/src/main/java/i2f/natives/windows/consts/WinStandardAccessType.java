package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/9 11:16
 * @desc
 */
public interface WinStandardAccessType {

    int DELETE = 0x00010000;
    int READ_CONTROL = 0x00020000;
    int WRITE_DAC = 0x00040000;
    int WRITE_OWNER = 0x00080000;
    int SYNCHRONIZE = 0x00100000;

    int STANDARD_RIGHTS_REQUIRED = 0x000F0000;

    int STANDARD_RIGHTS_READ = READ_CONTROL;
    int STANDARD_RIGHTS_WRITE = READ_CONTROL;
    int STANDARD_RIGHTS_EXECUTE = READ_CONTROL;

    int STANDARD_RIGHTS_ALL = 0x001F0000;

    int SPECIFIC_RIGHTS_ALL = 0x0000FFFF;

//
// AccessSystemAcl access type
//

    int ACCESS_SYSTEM_SECURITY = 0x01000000;

//
// MaximumAllowed access type
//

    int MAXIMUM_ALLOWED = 0x02000000;

//
//  These are the generic rights.
//

    int GENERIC_READ = 0x80000000;
    int GENERIC_WRITE = 0x40000000;
    int GENERIC_EXECUTE = 0x20000000;
    int GENERIC_ALL = 0x10000000;

}
