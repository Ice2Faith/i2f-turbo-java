package i2f.extension.document.formats.word;


import com.aspose.words.SaveFormat;

/**
 * @author Ice2Faith
 * @date 2024/11/30 12:13
 */
public enum DocFormat {
    UNKNOWN(0),
    DOC(10),
    DOT(11),
    DOCX(20),
    DOCM(21),
    DOTX(22),
    DOTM(23),
    FLAT_OPC(24),
    FLAT_OPC_MACRO_ENABLED(25),
    FLAT_OPC_TEMPLATE(26),
    FLAT_OPC_TEMPLATE_MACRO_ENABLED(27),
    RTF(30),
    WORD_ML(31),
    PDF(40),
    XPS(41),
    XAML_FIXED(42),
    SWF(43),
    SVG(44),
    HTML_FIXED(45),
    OPEN_XPS(46),
    PS(47),
    HTML(50),
    MHTML(51),
    EPUB(52),
    ODT(60),
    OTT(61),
    TEXT(70),
    XAML_FLOW(71),
    XAML_FLOW_PACK(72),
    TIFF(100),
    PNG(101),
    BMP(102),
    JPEG(104),
    ;

    public static Class<?> REFER_CLASS = SaveFormat.class;

    private int code;

    private DocFormat(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
