package i2f.natives.windows.consts.shell;

/**
 * @author Ice2Faith
 * @date 2024/5/12 19:25
 * @desc
 */
public interface WinShFileOperationFlag {

    int FOF_MULTIDESTFILES = 0x0001;
    int FOF_CONFIRMMOUSE = 0x0002;
    int FOF_SILENT = 0x0004;  // don't display progress UI (confirm prompts may be displayed still)
    int FOF_RENAMEONCOLLISION = 0x0008;  // automatically rename the source files to avoid the collisions
    int FOF_NOCONFIRMATION = 0x0010;  // don't display confirmation UI, assume "yes" for cases that can be bypassed, "no" for those that can not
    int FOF_WANTMAPPINGHANDLE = 0x0020;  // Fill in SHFILEOPSTRUCT.hNameMappings
    // Must be freed using SHFreeNameMappings
    int FOF_ALLOWUNDO = 0x0040;  // enable undo including Recycle behavior for IFileOperation::Delete()
    int FOF_FILESONLY = 0x0080;  // only operate on the files (non folders), both files and folders are assumed without this
    int FOF_SIMPLEPROGRESS = 0x0100;  // means don't show names of files
    int FOF_NOCONFIRMMKDIR = 0x0200;  // don't dispplay confirmatino UI before making any needed directories, assume "Yes" in these cases
    int FOF_NOERRORUI = 0x0400;  // don't put up error UI, other UI may be displayed, progress, confirmations
    // #if (_WIN32_IE >= 0x0500)
    int FOF_NOCOPYSECURITYATTRIBS = 0x0800;  // dont copy file security attributes (ACLs)
    int FOF_NORECURSION = 0x1000;  // don't recurse into directories for operations that would recurse
    int FOF_NO_CONNECTED_ELEMENTS = 0x2000;  // don't operate on connected elements ("xxx_files" folders that go with .htm files)
    int FOF_WANTNUKEWARNING = 0x4000;  // during delete operation, warn if nuking instead of recycling (partially overrides FOF_NOCONFIRMATION)
    // #endif // (_WIN32_IE >= 0x500)
// #if (_WIN32_WINNT >= 0x0501)
    int FOF_NORECURSEREPARSE = 0x8000;  // deprecated; the operations engine always does the right thing on FolderLink objects (symlinks, reparse points, folder shortcuts)
    // #endif // (_WIN32_WINNT >= 0x501)
    int FOF_NO_UI = (FOF_SILENT | FOF_NOCONFIRMATION | FOF_NOERRORUI | FOF_NOCONFIRMMKDIR); // don't display any UI at all

}
