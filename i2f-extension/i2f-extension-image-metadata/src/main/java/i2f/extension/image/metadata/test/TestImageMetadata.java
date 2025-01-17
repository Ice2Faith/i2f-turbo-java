package i2f.extension.image.metadata.test;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.util.Collection;

/**
 * @author Ice2Faith
 * @date 2025/1/14 14:56
 */
public class TestImageMetadata {
    public static void main(String[] args) throws Exception {
        File file = new File("./test.jpg");

        Metadata metadata = ImageMetadataReader.readMetadata(file);
        Iterable<Directory> directories = metadata.getDirectories();
        for (Directory directory : directories) {
            String name = directory.getName();
            System.out.println("=======================");
            System.out.println(name);
            Collection<Tag> tags = directory.getTags();
            for (Tag tag : tags) {
                System.out.println("\t" + tag);
                System.out.println("\t\tname:" + tag.getTagName());
                System.out.println("\t\ttype:" + tag.getTagType());
                System.out.println("\t\tdesc:" + tag.getDescription());
            }
        }
    }
}
