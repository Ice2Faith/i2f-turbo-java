package i2f.web.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/6/29 14:30
 * @desc
 */
public class ByteArrayServletOutputStream extends ServletOutputStream {
    protected ByteArrayOutputStream bos;

    public ByteArrayServletOutputStream(ByteArrayOutputStream bos) {
        this.bos = bos;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
        bos.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        bos.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        bos.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        bos.flush();
    }

    @Override
    public void close() throws IOException {
        bos.close();
    }
}
