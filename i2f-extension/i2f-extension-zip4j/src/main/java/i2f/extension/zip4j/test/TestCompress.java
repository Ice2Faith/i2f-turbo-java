package i2f.extension.zip4j.test;

import i2f.compress.ICompressor;
import i2f.extension.zip4j.ZipZip4jCompressor;

import java.io.File;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2024/6/28 17:39
 * @desc
 */
public class TestCompress {
    public static void main(String[] args) throws IOException {
        compress(new ZipZip4jCompressor("123456"), "pass.zip");
    }

    public static void compress(ICompressor compressor, String suffix) throws IOException {

        File output = new File("./output/src." + suffix);
        compressor.compress(output,
                new File("./i2f-jdk/i2f-compress"));

        compressor.release(output, new File("./output/release/" + suffix));

    }
}
