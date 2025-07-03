package i2f.extension.image.metadata;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;

import java.io.File;

/**
 * @author Ice2Faith
 * @date 2025/7/3 15:04
 */
public class ImageFileMetadata {
    public static Metadata getImageMetadata(File file) throws Exception {
        return ImageMetadataReader.readMetadata(file);
    }
}
