package i2f.otpauth.test;

import i2f.codec.bytes.basex.Base32;
import i2f.otpauth.impl.HotpAuthenticator;
import i2f.otpauth.impl.SteamAuthenticator;
import i2f.otpauth.impl.TotpAuthenticator;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2024/6/13 9:14
 * @desc
 */
public class TestOtpAuth {
    public static void main(String[] args) throws Exception {
//        testTotp();
//        testHotp();
        testSteam();
    }

    public static void testSteam() throws Exception {
        SteamAuthenticator authenticator = new SteamAuthenticator(Base32.decode(Base32.encode("123456".getBytes())));
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

    public static void testHotp() throws Exception {
        long counter = 1234;
        byte[] secretBytes = Base32.decode(Base32.encode("123456".getBytes()));
        HotpAuthenticator sender = new HotpAuthenticator(counter, secretBytes);
        HotpAuthenticator receiver = new HotpAuthenticator(counter, secretBytes);
        String url = receiver.makeQrUrl("test", "auth");
        System.out.println(url);
        System.in.read();
        for (int i = 0; i < 100; i++) {
            System.out.println("---------------------");
            String code = sender.generate();
            System.out.println(i + "\t : " + code);
            boolean ok = receiver.verify(code);
            System.out.println(ok);
        }
    }

    public static void testTotp() throws Exception {
        TotpAuthenticator authenticator = new TotpAuthenticator(Base32.decode(Base32.encode("123456".getBytes())));
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
