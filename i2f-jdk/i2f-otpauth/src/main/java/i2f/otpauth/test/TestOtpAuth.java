package i2f.otpauth.test;

import i2f.otpauth.impl.Base32;
import i2f.otpauth.impl.TotpAuthenticator;

/**
 * @author Ice2Faith
 * @date 2024/6/13 9:14
 * @desc
 */
public class TestOtpAuth {
    public static void main(String[] args) throws Exception {
        testTotp();
    }

    public static void testTotp() throws Exception {
        TotpAuthenticator authenticator = new TotpAuthenticator(Base32.decode(Base32.encodeOriginal("123456".getBytes())));
        String url = authenticator.makeQrUrl("test", "auth");
        System.out.println(url);
        System.in.read();
        for (int i = 0; i < 100; i++) {
            System.out.println("---------------------");
            String code = authenticator.generate();
            System.out.println(i + "\t : " + code);
            int alive = authenticator.aliveSeconds();
            System.out.println("alive:" + alive);
            Thread.sleep(3000);
            boolean ok = authenticator.verify(code);
            System.out.println(ok);
        }
    }
}
