package i2f.codec.bytes.basex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/10/12 10:59
 * @desc
 */
public class Base32Test {
    public static void main(String[] args) throws IOException {
        String raw = "";
        for (int i = 0; i < 10; i++) {
            raw += (char) ('0' + i);
            System.out.println("-------------");
            System.out.println(raw);
            String b64 = Base32.encode(raw.getBytes());
            System.out.println(b64);
            b64 = b64.replaceAll("=", "");
            String dec = new String(Base32.decode(b64));
            System.out.println(dec);
            System.out.println(raw.equals(dec));

            ByteArrayInputStream bis = new ByteArrayInputStream(raw.getBytes());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Base32.encode(bis, bos);
            bis.close();
            bos.close();
            System.out.println(new String(bos.toByteArray()));
            bis = new ByteArrayInputStream(bos.toByteArray());
            bos = new ByteArrayOutputStream();
            Base32.decode(bis, bos);
            bis.close();
            bos.close();
            dec = new String(bos.toByteArray());
            System.out.println(raw.equals(dec));
        }
    }
}
