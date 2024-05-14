package i2f.natives.windows.consts.device;

/**
 * @author Ice2Faith
 * @date 2024/5/7 16:32
 * @desc
 */
public interface WinDeviceCapsIndex {

    /* Device Parameters for GetDeviceCaps() */
    int DRIVERVERSION = 0;    /* Device driver version                    */
    int TECHNOLOGY = 2;    /* Device classification                    */
    int HORZSIZE = 4;    /* Horizontal size in millimeters           */
    int VERTSIZE = 6;    /* Vertical size in millimeters             */
    int HORZRES = 8;    /* Horizontal width in pixels               */
    int VERTRES = 10;    /* Vertical height in pixels                */
    int BITSPIXEL = 12;    /* Number of bits per pixel                 */
    int PLANES = 14;    /* Number of planes                         */
    int NUMBRUSHES = 16;    /* Number of brushes the device has         */
    int NUMPENS = 18;    /* Number of pens the device has            */
    int NUMMARKERS = 20;    /* Number of markers the device has         */
    int NUMFONTS = 22;    /* Number of fonts the device has           */
    int NUMCOLORS = 24;    /* Number of colors the device supports     */
    int PDEVICESIZE = 26;    /* Size required for device descriptor      */
    int CURVECAPS = 28;    /* Curve capabilities                       */
    int LINECAPS = 30;    /* Line capabilities                        */
    int POLYGONALCAPS = 32;    /* Polygonal capabilities                   */
    int TEXTCAPS = 34;    /* Text capabilities                        */
    int CLIPCAPS = 36;    /* Clipping capabilities                    */
    int RASTERCAPS = 38;    /* Bitblt capabilities                      */
    int ASPECTX = 40;    /* Length of the X leg                      */
    int ASPECTY = 42;    /* Length of the Y leg                      */
    int ASPECTXY = 44;    /* Length of the hypotenuse                 */

    int LOGPIXELSX = 88;    /* Logical pixels/inch in X                 */
    int LOGPIXELSY = 90;    /* Logical pixels/inch in Y                 */

    int SIZEPALETTE = 104;    /* Number of entries in physical palette    */
    int NUMRESERVED = 106;    /* Number of reserved entries in palette    */
    int COLORRES = 108;    /* Actual color resolution                  */

// Printing related DeviceCaps. These replace the appropriate Escapes

    int PHYSICALWIDTH = 110; /* Physical Width in device units           */
    int PHYSICALHEIGHT = 111; /* Physical Height in device units          */
    int PHYSICALOFFSETX = 112; /* Physical Printable Area x margin         */
    int PHYSICALOFFSETY = 113; /* Physical Printable Area y margin         */
    int SCALINGFACTORX = 114; /* Scaling factor x                         */
    int SCALINGFACTORY = 115; /* Scaling factor y                         */

// Display driver specific

    int VREFRESH = 116;  /* Current vertical refresh rate of the    */
    /* display device (for displays only) in Hz*/
    int DESKTOPVERTRES = 117;  /* Horizontal width of entire desktop in   */
    /* pixels                                  */
    int DESKTOPHORZRES = 118;  /* Vertical height of entire desktop in    */
    /* pixels                                  */
    int BLTALIGNMENT = 119;  /* Preferred blt alignment                 */

    // #if(WINVER >= 0x0500)
    int SHADEBLENDCAPS = 120;  /* Shading and blending caps               */
    int COLORMGMTCAPS = 121;  /* Color Management caps                   */
// #endif /* WINVER >= 0x0500 */

}
