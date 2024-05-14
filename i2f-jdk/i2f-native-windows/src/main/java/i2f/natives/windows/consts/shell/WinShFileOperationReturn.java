package i2f.natives.windows.consts.shell;

/**
 * @author Ice2Faith
 * @date 2024/5/12 19:27
 * @desc
 */
public interface WinShFileOperationReturn {
    int SUCCESS = 0;
    int DE_SAMEFILE = 0x71; // 	源文件和目标文件是同一个文件。
    int DE_MANYSRC1DEST = 0x72; // 	源缓冲区中指定了多个文件路径，但只有一个目标文件路径。
    int DE_DIFFDIR = 0x73; // 	指定了重命名操作，但目标路径是不同的目录。 请改用移动操作。
    int DE_ROOTDIR = 0x74; // 	源是根目录，无法移动或重命名。
    int DE_OPCANCELLED = 0x75; // 	操作已被用户取消，或者，如果向 SHFileOperation 提供了相应的标志，则以无提示方式取消操作。
    int DE_DESTSUBTREE = 0x76; // 	目标是源的子树。
    int DE_ACCESSDENIEDSRC = 0x78; // 	安全设置拒绝访问源。
    int DE_PATHTOODEEP = 0x79; // 	源或目标路径超出或将超过 MAX_PATH。
    int DE_MANYDEST = 0x7A; // 	该操作涉及多个目标路径，在移动操作中可能会失败。
    int DE_INVALIDFILES = 0x7C; // 	源或目标中的路径或两者都无效。
    int DE_DESTSAMETREE = 0x7D; // 	源和目标具有相同的父文件夹。
    int DE_FLDDESTISFILE = 0x7E; // 	目标路径是现有文件。
    int DE_FILEDESTISFLD = 0x80; // 	目标路径是现有文件夹。
    int DE_FILENAMETOOLONG = 0x81; // 	文件名超过 MAX_PATH。
    int DE_DEST_IS_CDROM = 0x82; // 	目标是只读 CD-ROM，可能未格式化。
    int DE_DEST_IS_DVD = 0x83; // 	目标是只读 DVD，可能未格式化。
    int DE_DEST_IS_CDRECORD = 0x84; // 	目标为可写 CD-ROM，可能未格式化。
    int DE_FILE_TOO_LARGE = 0x85; // 	操作中涉及的文件对于目标媒体或文件系统来说太大。
    int DE_SRC_IS_CDROM = 0x86; // 	源是只读 CD-ROM，可能未格式化。
    int DE_SRC_IS_DVD = 0x87; // 	源是只读 DVD，可能未格式化。
    int DE_SRC_IS_CDRECORD = 0x88; // 	源是可写 CD-ROM，可能未格式化。
    int DE_ERROR_MAX = 0xB7; // 	操作期间已超出MAX_PATH。
    int UNKNOWN = 0x402; // 	出现未知错误。 这通常是由于源或目标中的路径无效。 此错误不会在 Windows Vista 及更高版本上发生。
    int ERRORONDEST = 0x10000; // 	目标上发生未指定的错误。
    int DE_ROOTDIR_ERRORONDEST = 0x10074; // 	Destination 是根目录，无法重命名。
}
