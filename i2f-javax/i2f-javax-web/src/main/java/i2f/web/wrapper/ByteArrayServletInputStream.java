package i2f.web.wrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/6/29 14:16
 * @desc
 */
public class ByteArrayServletInputStream extends ServletInputStream {
    protected ByteArrayInputStream bis ;
    public ByteArrayServletInputStream(ByteArrayInputStream bis) {
        this.bis = bis;
    }


    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

    @Override
    public int read() throws IOException {
        return bis.read();
    }
}
