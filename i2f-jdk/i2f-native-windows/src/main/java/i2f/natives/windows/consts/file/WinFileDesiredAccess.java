package i2f.natives.windows.consts.file;

import i2f.natives.windows.consts.access.WinStandardAccessType;

/**
 * @author Ice2Faith
 * @date 2024/5/11 16:20
 * @desc
 */
public interface WinFileDesiredAccess {

    long FILE_READ_DATA = (0x0001);    // file & pipe
    long FILE_LIST_DIRECTORY = (0x0001);    // directory

    long FILE_WRITE_DATA = (0x0002);    // file & pipe
    long FILE_ADD_FILE = (0x0002);    // directory

    long FILE_APPEND_DATA = (0x0004);    // file
    long FILE_ADD_SUBDIRECTORY = (0x0004);    // directory
    long FILE_CREATE_PIPE_INSTANCE = (0x0004);    // named pipe


    long FILE_READ_EA = (0x0008);    // file & directory

    long FILE_WRITE_EA = (0x0010);    // file & directory

    long FILE_EXECUTE = (0x0020);    // file
    long FILE_TRAVERSE = (0x0020);    // directory

    long FILE_DELETE_CHILD = (0x0040);    // directory

    long FILE_READ_ATTRIBUTES = (0x0080);    // all

    long FILE_WRITE_ATTRIBUTES = (0x0100);    // all

    long FILE_ALL_ACCESS = (WinStandardAccessType.STANDARD_RIGHTS_REQUIRED | WinStandardAccessType.SYNCHRONIZE | 0x1FF);

    long FILE_GENERIC_READ = (WinStandardAccessType.STANDARD_RIGHTS_READ |
            FILE_READ_DATA |
            FILE_READ_ATTRIBUTES |
            FILE_READ_EA |
            WinStandardAccessType.SYNCHRONIZE);


    long FILE_GENERIC_WRITE = (WinStandardAccessType.STANDARD_RIGHTS_WRITE |
            FILE_WRITE_DATA |
            FILE_WRITE_ATTRIBUTES |
            FILE_WRITE_EA |
            FILE_APPEND_DATA |
            WinStandardAccessType.SYNCHRONIZE);


    long FILE_GENERIC_EXECUTE = (WinStandardAccessType.STANDARD_RIGHTS_EXECUTE |
            FILE_READ_ATTRIBUTES |
            FILE_EXECUTE |
            WinStandardAccessType.SYNCHRONIZE);

}
