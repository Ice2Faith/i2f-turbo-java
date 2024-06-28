package i2f.tools.encrypt;


import i2f.tools.encrypt.menus.HelpMenuHandler;
import i2f.tools.encrypt.menus.bc.digest.*;
import i2f.tools.encrypt.menus.bc.digest.hmac.BcHmacSm3MenuHandler;
import i2f.tools.encrypt.menus.bc.digest.hmac.BcHmacTigerMenuHandler;
import i2f.tools.encrypt.menus.bc.digest.hmac.BcHmacWhirlpoolMenuHandler;
import i2f.tools.encrypt.menus.codec.*;
import i2f.tools.encrypt.menus.digest.*;
import i2f.tools.encrypt.menus.digest.hmac.*;
import i2f.tools.encrypt.menus.github.jasypt.*;
import i2f.tools.encrypt.menus.jce.*;
import i2f.tools.encrypt.menus.sm.antherd.*;
import i2f.tools.encrypt.menus.spring.security.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CryptMain {
    public static final IMenuHandler helpHandler = new HelpMenuHandler();
    public static Map<String, IMenuHandler> menuHandlerMap = new ConcurrentHashMap<>();

    public static void addMenuHandler(IMenuHandler handler) {
        if (handler == null) {
            return;
        }
        menuHandlerMap.put(handler.name(), handler);
    }

    static {
        addMenuHandler(helpHandler);
        addMenuHandler(new Base16EncoderMenuHandler());
        addMenuHandler(new Base16DecoderMenuHandler());
        addMenuHandler(new Base32EncoderMenuHandler());
        addMenuHandler(new Base32DecoderMenuHandler());
        addMenuHandler(new Base64EncoderMenuHandler());
        addMenuHandler(new Base64DecoderMenuHandler());
        addMenuHandler(new Base64UrlEncoderMenuHandler());
        addMenuHandler(new Base64UrlDecoderMenuHandler());
        addMenuHandler(new HtmlCerEncoderMenuHandler());
        addMenuHandler(new HtmlCerDecoderMenuHandler());
        addMenuHandler(new HtmlNcrEncoderMenuHandler());
        addMenuHandler(new HtmlNcrDecoderMenuHandler());
        addMenuHandler(new UCodeEncoderMenuHandler());
        addMenuHandler(new UCodeDecoderMenuHandler());
        addMenuHandler(new UrlEncoderMenuHandler());
        addMenuHandler(new UrlDecoderMenuHandler());
        addMenuHandler(new XCodeEncoderMenuHandler());
        addMenuHandler(new XCodeDecoderMenuHandler());
        addMenuHandler(new Md2MenuHandler());
        addMenuHandler(new Md5MenuHandler());
        addMenuHandler(new Sha1MenuHandler());
        addMenuHandler(new Sha224MenuHandler());
        addMenuHandler(new Sha256MenuHandler());
        addMenuHandler(new Sha384MenuHandler());
        addMenuHandler(new Sha512MenuHandler());
        addMenuHandler(new HmacMd2MenuHandler());
        addMenuHandler(new HmacMd5MenuHandler());
        addMenuHandler(new HmacSha1MenuHandler());
        addMenuHandler(new HmacSha224MenuHandler());
        addMenuHandler(new HmacSha256MenuHandler());
        addMenuHandler(new HmacSha384MenuHandler());
        addMenuHandler(new HmacSha512MenuHandler());

        addMenuHandler(new Argon2PasswordEncoderMenuHandler());
        addMenuHandler(new BCryptPasswordEncoderMenuHandler());
        addMenuHandler(new LdapShaPasswordEncoderMenuHandler());
        addMenuHandler(new MD2PasswordEncoderMenuHandler());
        addMenuHandler(new MD4PasswordEncoderMenuHandler());
        addMenuHandler(new MD5PasswordEncoderMenuHandler());
        addMenuHandler(new NoOpPasswordEncoderMenuHandler());
        addMenuHandler(new Pbkdf2PasswordEncoderMenuHandler());
        addMenuHandler(new Pbkdf2SecPasswordEncoderMenuHandler());
        addMenuHandler(new SCryptPasswordEncoderMenuHandler());
        addMenuHandler(new SHA1PasswordEncoderMenuHandler());
        addMenuHandler(new SHA224PasswordEncoderMenuHandler());
        addMenuHandler(new SHA256PasswordEncoderMenuHandler());
        addMenuHandler(new SHA384PasswordEncoderMenuHandler());
        addMenuHandler(new SHA512PasswordEncoderMenuHandler());

        addMenuHandler(new JasyptBasicTextEncoderMenuHandler());
        addMenuHandler(new JasyptBasicTextDecoderMenuHandler());
        addMenuHandler(new JasyptStandardPBEEncoderMenuHandler());
        addMenuHandler(new JasyptStandardPBEDecoderMenuHandler());
        addMenuHandler(new JasyptStandardPBEAlgoEncoderMenuHandler());
        addMenuHandler(new JasyptStandardPBEAlgoDecoderMenuHandler());

        addMenuHandler(new AesB64EncoderMenuHandler());
        addMenuHandler(new AesB64DecoderMenuHandler());
        addMenuHandler(new AesEncoderMenuHandler());
        addMenuHandler(new AesDecoderMenuHandler());
        addMenuHandler(new AesKeygenB64EncoderMenuHandler());
        addMenuHandler(new AesKeygenB64DecoderMenuHandler());
        addMenuHandler(new AesKeygenEncoderMenuHandler());
        addMenuHandler(new AesKeygenDecoderMenuHandler());

        addMenuHandler(new AntherdSm2KeyGenMenuHandler());
        addMenuHandler(new AntherdSm2EncryptMenuHandler());
        addMenuHandler(new AntherdSm2DecryptMenuHandler());
        addMenuHandler(new AntherdSm2SignMenuHandler());
        addMenuHandler(new AntherdSm2VerifyMenuHandler());

        addMenuHandler(new AntherdSm3MenuHandler());

        addMenuHandler(new AntherdSm4GenKeyMenuHandler());
        addMenuHandler(new AntherdSm4EncoderMenuHandler());
        addMenuHandler(new AntherdSm4DecoderMenuHandler());

        addMenuHandler(new BcSha3A224MenuHandler());
        addMenuHandler(new BcSha3A256MenuHandler());
        addMenuHandler(new BcSha3A384MenuHandler());
        addMenuHandler(new BcSha3A512MenuHandler());
        addMenuHandler(new BcSha512A224MenuHandler());
        addMenuHandler(new BcSha512A256MenuHandler());
        addMenuHandler(new BcShake128MenuHandler());
        addMenuHandler(new BcShake256MenuHandler());
        addMenuHandler(new BcSm3MenuHandler());
        addMenuHandler(new BcTigerMenuHandler());
        addMenuHandler(new BcWhirlpoolMenuHandler());

        addMenuHandler(new BcHmacSm3MenuHandler());
        addMenuHandler(new BcHmacTigerMenuHandler());
        addMenuHandler(new BcHmacWhirlpoolMenuHandler());
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            args = new String[]{helpHandler.name()};
        }

        String option = args[0].toLowerCase();

        try {
            IMenuHandler handler = menuHandlerMap.get(option);
            if (handler == null) {
                handler = helpHandler;
            }
            String[] handlerArgs = new String[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                handlerArgs[i - 1] = args[i];
            }
            handler.execute(handlerArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
