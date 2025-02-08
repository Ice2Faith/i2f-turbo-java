package i2f.extension.sevenz.callbak;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileOutStream;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @author Ice2Faith
 * @date 2024/6/29 17:39
 * @desc
 */
@Data
@NoArgsConstructor
public class ExtractSevenZCallback implements IArchiveExtractCallback, ICryptoGetTextPassword {
    private String password;
    private IInArchive inArchive;
    private File output;
    private ExtractOperationResult operationResult;
    private long total;
    private long completed;

    public ExtractSevenZCallback(String password, IInArchive inArchive, File output) {
        this.password = password;
        this.inArchive = inArchive;
        this.output = output;
    }

    public ExtractSevenZCallback(IInArchive inArchive, File output) {
        this.inArchive = inArchive;
        this.output = output;
    }

    @Override
    public ISequentialOutStream getStream(int i, ExtractAskMode extractAskMode) throws SevenZipException {
        if (extractAskMode != ExtractAskMode.EXTRACT) {
            return null;
        }
        boolean isFolder = (Boolean) inArchive
                .getProperty(i, PropID.IS_FOLDER);
        String name = (String) inArchive.getProperty(i, PropID.PATH);
        File file = new File(output, name);
        if (isFolder) {
            file.mkdirs();
            return null;
        }

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
            return new RandomAccessFileOutStream(accessFile);
        } catch (Exception e) {
            throw new SevenZipException(e.getMessage(), e);
        }
    }

    @Override
    public void prepareOperation(ExtractAskMode extractAskMode) throws SevenZipException {

    }

    @Override
    public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {
        this.operationResult = extractOperationResult;
    }

    @Override
    public String cryptoGetTextPassword() throws SevenZipException {
        return password;
    }

    @Override
    public void setTotal(long l) throws SevenZipException {
        this.total = l;
    }

    @Override
    public void setCompleted(long l) throws SevenZipException {
        this.completed = l;
    }
}
