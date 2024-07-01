package i2f.extension.filesystem.sftp.proxy;


import i2f.io.filesystem.IFileSystem;
import i2f.io.filesystem.abs.AbsFile;

public class ProxySftpFile extends AbsFile {
    private IFileSystem fs;
    private String path;

    public ProxySftpFile(IFileSystem fs, String path) {
        this.fs = fs;
        this.path = path;
    }

    @Override
    public void setFileSystem(IFileSystem fs) {
        this.fs = fs;
    }

    @Override
    public IFileSystem getFileSystem() {
        return this.fs;
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
