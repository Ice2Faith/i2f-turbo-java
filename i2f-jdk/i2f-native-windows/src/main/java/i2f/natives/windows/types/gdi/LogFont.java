package i2f.natives.windows.types.gdi;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/13 11:21
 * @desc
 */
@Data
public class LogFont {
    public int lfHeight;
    public int lfWidth;
    public int lfEscapement;
    public int lfOrientation;
    public int lfWeight;
    public boolean lfItalic;
    public boolean lfUnderline;
    public boolean lfStrikeOut;
    public int lfCharSet;
    public int lfOutPrecision;
    public int lfClipPrecision;
    public int lfQuality;
    public int lfPitchAndFamily;
    public String lfFaceName;
}
