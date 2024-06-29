package i2f.compress.test;

import i2f.compress.ICompressor;
import i2f.compress.impl.jdk.ZipJdkCompressor;

import java.io.File;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2024/6/28 17:39
 * @desc
 */
public class TestCompress {
    public static void main(String[] args) throws IOException {
        ICompressor compressor = new ZipJdkCompressor();

        File output = new File("./output/src.zip");
        compressor.compress(output,
                new File("./i2f-jdk/i2f-compress"));

        compressor.release(output, new File("./output/release"));

    }
}
