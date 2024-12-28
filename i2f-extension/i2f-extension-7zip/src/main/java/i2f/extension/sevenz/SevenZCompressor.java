package i2f.extension.sevenz;

import i2f.compress.impl.AbsCompressor;
import i2f.compress.std.data.CompressBindData;
import i2f.extension.sevenz.callbak.CompressSevenZCallback;
import i2f.extension.sevenz.callbak.ExtractSevenZCallback;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.IOutCreateArchive7z;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.impl.RandomAccessFileOutStream;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * @author Ice2Faith
 * @date 2024/6/29 17:16
 * @desc
 */
@Data
@NoArgsConstructor
public class SevenZCompressor extends AbsCompressor {
    private String password;

    public SevenZCompressor(String password) {
        this.password = password;
    }

    @Override
    public void compressBindData(File output, Collection<CompressBindData> inputs) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(output, "rw");
        IOutCreateArchive7z outArchive = SevenZip.openOutArchive7z();

        if (password != null && !password.isEmpty()) {
            outArchive.setHeaderEncryption(true);
        }

        // Create archive
        outArchive.createArchive(new RandomAccessFileOutStream(raf),
                inputs.size(), new CompressSevenZCallback(password, inputs));

        outArchive.close();
        raf.close();
    }

    @Override
    public void release(File input, File output, BiConsumer<CompressBindData, File> consumer) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(input, "r");
        IInArchive inArchive = SevenZip.openInArchive(null, // autodetect archive type
                new RandomAccessFileInStream(randomAccessFile),password);

        int[] in = new int[inArchive.getNumberOfItems()];
        for (int i = 0; i < in.length; i++) {
            in[i] = i;
        }
        inArchive.extract(in, false, // Non-test mode
                new ExtractSevenZCallback(password,inArchive,output));

        inArchive.close();

        randomAccessFile.close();
    }
}
