package i2f.image;

import sun.font.FontManager;
import sun.font.FontManagerFactory;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/8/22 8:57
 * @desc
 */
public class FontUtil {
    public static String[] DEFAULT_FONT_PATHS = {
            "fonts",
            "conf/fonts",
            "resources/fonts"
    };
    public static final String[] TRUE_TYPE_SUFFIXES = {
            ".ttf", ".ttc", ".woff", ".ottf"
    };
    public static final String[] TYPE1_SUFFIXES = {
            ".pfa", ".pfb"
    };
    private static final AtomicBoolean loaded = new AtomicBoolean(false);

    static {
        registryDefaultFonts();
    }

    public static void registryDefaultFonts() {
        if (loaded.getAndSet(true)) {
            return;
        }
        File[] defaultFontFiles = new File[DEFAULT_FONT_PATHS.length];
        for (int i = 0; i < DEFAULT_FONT_PATHS.length; i++) {
            defaultFontFiles[i] = new File(DEFAULT_FONT_PATHS[i]);
        }
        registryFonts(defaultFontFiles);
    }

    public static Font loadFont(File fontFile) {
        if (fontFile == null || !fontFile.exists() || !fontFile.isFile()) {
            return null;
        }
        String name = fontFile.getName();
        String suffix = "";
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            suffix = name.substring(idx);
        }
        suffix = suffix.toLowerCase();
        for (String item : TRUE_TYPE_SUFFIXES) {
            if (item.equals(suffix)) {
                try {
                    return Font.createFont(Font.TRUETYPE_FONT, fontFile);
                } catch (Exception e) {
                }
            }
        }
        for (String item : TYPE1_SUFFIXES) {
            if (item.equals(suffix)) {
                try {
                    return Font.createFont(Font.TYPE1_FONT, fontFile);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            return Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (Exception e) {

        }
        try {
            return Font.createFont(Font.TYPE1_FONT, fontFile);
        } catch (Exception e) {

        }
        return null;
    }

    public static List<Font> loadFonts(File[] fontFiles) {
        List<Font> ret = new ArrayList<>();
        if (fontFiles == null || fontFiles.length == 0) {
            return ret;
        }
        for (File fontFile : fontFiles) {
            if (!fontFile.exists()) {
                continue;
            }
            if (fontFile.isFile()) {
                Font font = loadFont(fontFile);
                if (font != null) {
                    ret.add(font);
                }
            }
            if (fontFile.isDirectory()) {
                File[] files = fontFile.listFiles();
                List<Font> next = loadFonts(files);
                ret.addAll(next);
            }
        }
        return ret;
    }

    public static void registryFonts(File[] fontFiles) {
        List<Font> fonts = loadFonts(fontFiles);
        registryFonts(fonts);
    }

    public static void registryFonts(Collection<Font> fonts) {
        if (fonts == null) {
            return;
        }
        FontManager fm = FontManagerFactory.getInstance();
        for (Font font : fonts) {
            if (font == null) {
                continue;
            }
            fm.registerFont(font);
        }
    }
}
