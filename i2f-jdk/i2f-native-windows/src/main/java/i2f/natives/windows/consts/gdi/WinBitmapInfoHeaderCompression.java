package i2f.natives.windows.consts.gdi;

/**
 * @author Ice2Faith
 * @date 2024/5/14 22:56
 * @desc
 */
public interface WinBitmapInfoHeaderCompression {
    /* constants for the biCompression field */
    int BI_RGB = 0;
    int BI_RLE8 = 1;
    int BI_RLE4 = 2;
    int BI_BITFIELDS = 3;
    int BI_JPEG = 4;
    int BI_PNG = 5;
// #if (_WIN32_WINNT >= _WIN32_WINNT_NT4)
// #endif
}
