package i2f.extension.zip4j;

import i2f.compress.std.data.CompressBindData;
import i2f.compress.std.impl.AbsCompressor;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * @author Ice2Faith
 * @date 2024/6/29 16:49
 * @desc
 */
public class ZipZip4jCompressor extends AbsCompressor {
    private ZipParameters zipParameters;
    private String password;
    public ZipZip4jCompressor(){
        this(null);
    }
    public ZipZip4jCompressor(String password){
        this.password=password;
        ZipParameters parameters = new ZipParameters();
        // 压缩级别
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);
        parameters.setCompressionLevel(CompressionLevel.NORMAL);

        if(password!=null && !password.isEmpty()){
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(EncryptionMethod.AES);
            parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_128);
        }

        this.zipParameters=parameters;
    }
    public ZipZip4jCompressor(ZipParameters zipParameters,String password){
        this.zipParameters=zipParameters;
        this.password=password;
    }

    @Override
    public void compressBindData(File output, Collection<CompressBindData> inputs) throws IOException {
        ZipFile zipFile=new ZipFile(output);
        if(password!=null && !"".equals(password)){
            zipFile.setPassword(password.toCharArray());
        }
        for (CompressBindData input : inputs) {
            String path = input.getDirectory() + "/" + input.getFileName();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            InputStream is = input.getInputStream();
            if (is == null) {
                continue;
            }

            ZipParameters parameters = new ZipParameters(zipParameters);
            parameters.setFileNameInZip(path);
            if (input.getSize() >= 0) {
                parameters.setEntrySize(input.getSize());
            }
            zipFile.addStream(is,parameters);

            is.close();

        }

        zipFile.close();
    }

    @Override
    public void release(File input, File output, BiConsumer<CompressBindData, File> consumer) throws IOException {
        ZipFile zipFile=new ZipFile(input);
        if(zipFile.isEncrypted()){
            zipFile.setPassword(password.toCharArray());
        }
        zipFile.extractAll(output.getAbsolutePath());
    }
}
