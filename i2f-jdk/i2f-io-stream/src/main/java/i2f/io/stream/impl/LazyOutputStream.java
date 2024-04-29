package i2f.io.stream.impl;


import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

public class LazyOutputStream extends OutputStream {
    private OutputStream os;
    private Supplier<OutputStream> supplier;
    private volatile boolean isRequire = false;

    public LazyOutputStream(Supplier<OutputStream> supplier) {
        this.supplier = supplier;
    }

    private void requireCheck() {
        if (isRequire) {
            return;
        }
        synchronized (this) {
            if (!isRequire) {
                this.os = supplier.get();
            }
            isRequire = true;
        }
    }

    @Override
    public void write(int b) throws IOException {
        requireCheck();
        os.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        requireCheck();
        os.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        requireCheck();
        os.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        if (os != null) {
            os.flush();
        }
    }

    @Override
    public void close() throws IOException {
        if (os != null) {
            os.close();
        }
    }
}
