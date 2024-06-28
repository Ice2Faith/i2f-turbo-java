package i2f.compress;

import i2f.compress.data.CompressBindData;
import i2f.compress.data.CompressBindFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * @author ltb
 * @date 2022/3/31 15:31
 * @desc
 */
public interface ICompressor {

    void compressBindData(File output, Collection<CompressBindData> inputs) throws IOException;

    void compressBindFile(File output, Collection<CompressBindFile> inputs) throws IOException;

    void compressFile(File output, Collection<File> inputs, Predicate<File> filter) throws IOException;

    default void compressFile(File output, Collection<File> inputs) throws IOException {
        compressFile(output, inputs, null);
    }

    default void compress(File output, CompressBindData... inputs) throws IOException {
        compressBindData(output, Arrays.asList(inputs));
    }

    default void compress(File output, CompressBindFile... inputs) throws IOException {
        compressBindFile(output, Arrays.asList(inputs));
    }

    default void compress(File output, File... inputs) throws IOException {
        compressFile(output, Arrays.asList(inputs), null);
    }


    void release(File input, File output) throws IOException;

    void release(File input, File output, BiConsumer<CompressBindData, File> consumer) throws IOException;

}
