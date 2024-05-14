package i2f.natives.windows.consts.com;

/**
 * @author Ice2Faith
 * @date 2024/5/12 15:05
 * @desc
 */
public interface WinCoInitializeExCoInit {
    int COINIT_APARTMENTTHREADED = 0x2;      // Apartment model

    // #if  (_WIN32_WINNT >= 0x0400 ) || defined(_WIN32_DCOM) // DCOM
// These constants are only valid on Windows NT 4.0
    int COINIT_MULTITHREADED = 0x0;      // OLE calls objects on any thread.
    int COINIT_DISABLE_OLE1DDE = 0x4;      // Don't use DDE for Ole1 support.
    int COINIT_SPEED_OVER_MEMORY = 0x8;      // Trade memory for speed.
// #endif // DCOM
}
