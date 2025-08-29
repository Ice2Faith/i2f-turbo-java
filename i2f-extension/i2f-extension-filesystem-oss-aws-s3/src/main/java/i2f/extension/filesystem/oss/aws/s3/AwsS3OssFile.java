package i2f.extension.filesystem.oss.aws.s3;

import i2f.io.filesystem.IFileSystem;
import i2f.io.filesystem.abs.AbsFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2025/6/25 20:49
 * @desc
 */
public class AwsS3OssFile extends AbsFile {
    private IFileSystem fs;
    private String path;

    public AwsS3OssFile(String path) {
        this.path = path;
    }

    public AwsS3OssFile(IFileSystem fs, String path) {
        this.fs = fs;
        this.path = path;
    }

    @Override
    public void setFileSystem(IFileSystem fs) {
        this.fs = fs;
    }

    @Override
    public IFileSystem getFileSystem() {
        return fs;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void writeBytes(byte[] data) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        getFileSystem().store(getPath(), is);
    }

}