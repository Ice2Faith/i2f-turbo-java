package i2f.natives.windows.consts.gdi;

/**
 * @author Ice2Faith
 * @date 2024/5/12 22:40
 * @desc
 */
public interface WinBitBltRop {
    long SRCCOPY = 0x00CC0020; /* dest = source                   */
    long SRCPAINT = 0x00EE0086; /* dest = source OR dest           */
    long SRCAND = 0x008800C6; /* dest = source AND dest          */
    long SRCINVERT = 0x00660046; /* dest = source XOR dest          */
    long SRCERASE = 0x00440328; /* dest = source AND (NOT dest )   */
    long NOTSRCCOPY = 0x00330008; /* dest = (NOT source)             */
    long NOTSRCERASE = 0x001100A6; /* dest = (NOT src) AND (NOT dest) */
    long MERGECOPY = 0x00C000CA; /* dest = (source AND pattern)     */
    long MERGEPAINT = 0x00BB0226; /* dest = (NOT source) OR dest     */
    long PATCOPY = 0x00F00021; /* dest = pattern                  */
    long PATPAINT = 0x00FB0A09; /* dest = DPSnoo                   */
    long PATINVERT = 0x005A0049; /* dest = pattern XOR dest         */
    long DSTINVERT = 0x00550009; /* dest = (NOT dest)               */
    long BLACKNESS = 0x00000042; /* dest = BLACK                    */
    long WHITENESS = 0x00FF0062; /* dest = WHITE                    */
// #if(WINVER >= 0x0500)

    long NOMIRRORBITMAP = 0x80000000L; /* Do not Mirror the bitmap in this call */
    long CAPTUREBLT = 0x40000000; /* Include layered windows */
// #endif /* WINVER >= 0x0500 */

}
