package i2f.form.dialog.impl.image;

import i2f.form.dialog.std.IUrlPreviewDialog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/5/10 13:14
 */
public class ImageUrlPreviewDialog implements IUrlPreviewDialog {
    public static final ImageUrlPreviewDialog INSTANCE=new ImageUrlPreviewDialog();
    @Override
    public boolean support(Object obj) {
        URL url = castAsUrl(obj);
        if(url==null){
            return false;
        }
        String suffix = getUrlFileSuffix(url);
        if (Arrays.asList(
                ".jpg", ".png", ".jpeg", ".webp", ".bmp", ".tiff"
        ).contains(suffix)) {
            return true;
        }
        return false;
    }

    @Override
    public void preview(Object obj) {
        try {
            URL url = castAsUrl(obj);
            BufferedImage img = ImageIO.read(url);
            ImageDialogs.previewImage(img);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(),e);
        }
    }


}
