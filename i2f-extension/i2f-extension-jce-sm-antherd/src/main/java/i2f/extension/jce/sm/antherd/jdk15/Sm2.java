package i2f.extension.jce.sm.antherd.jdk15;

import com.antherd.smcrypto.sm2.Keypair;
import com.antherd.smcrypto.sm2.Point;
import com.antherd.smcrypto.sm2.SignatureOptions;
import i2f.extension.jce.sm.antherd.NashornProvider;
import i2f.extension.jce.sm.antherd.SmAntherdProvider;

import javax.script.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/8/7 19:21
 * @desc
 */
public class Sm2 {
    static {
        NashornProvider.printNonNashorn();
        SmAntherdProvider.printNonDependency();
    }
    private static Invocable invocable = null;

    public Sm2() {
    }

    public static Keypair generateKeyPairHex() {
        Bindings bindings = null;

        try {
            bindings = (Bindings)invocable.invokeFunction("generateKeyPairHex", new Object[0]);
            System.out.println("ok");
        } catch (NoSuchMethodException | ScriptException var2) {
            Exception e = var2;
            e.printStackTrace();
        }

        return new Keypair((String)bindings.get("privateKey"), (String)bindings.get("publicKey"));
    }

    public static String doEncrypt(String msg, String publicKey, int cipherMode) {
        if (msg != null && !msg.trim().isEmpty()) {
            String encryptMsg = null;

            try {
                Object[] param = new Object[]{msg, publicKey, cipherMode};
                encryptMsg = (String)invocable.invokeFunction("doEncrypt", param);
            } catch (NoSuchMethodException | ScriptException var5) {
                Exception e = var5;
                e.printStackTrace();
            }

            return encryptMsg;
        } else {
            return "";
        }
    }

    public static String doEncrypt(String msg, String publicKey) {
        return doEncrypt(msg, publicKey, 1);
    }

    public static String doDecrypt(String encryptData, String privateKey, int cipherMode) {
        if (encryptData != null && !encryptData.trim().isEmpty()) {
            String msg = null;

            try {
                Object[] param = new Object[]{encryptData, privateKey, cipherMode};
                msg = (String)invocable.invokeFunction("doDecrypt", param);
            } catch (NoSuchMethodException | ScriptException var5) {
                Exception e = var5;
                e.printStackTrace();
            }

            return msg;
        } else {
            return "";
        }
    }

    public static String doDecrypt(String encryptData, String privateKey) {
        return doDecrypt(encryptData, privateKey, 1);
    }

    public static String doSignature(String msg, String publicKey, SignatureOptions signatureOptions) {
        String signature = null;

        try {
            signature = (String)invocable.invokeFunction("doSignature", new Object[]{msg, publicKey, getOptionsMap(signatureOptions)});
        } catch (NoSuchMethodException | ScriptException var5) {
            Exception e = var5;
            e.printStackTrace();
        }

        return signature;
    }

    public static String doSignature(String msg, String publicKey) {
        return doSignature(msg, publicKey, (SignatureOptions)null);
    }

    public static boolean doVerifySignature(String msg, String signHex, String publicKey, SignatureOptions signatureOptions) {
        boolean result = false;

        try {
            result = (Boolean)invocable.invokeFunction("doVerifySignature", new Object[]{msg, signHex, publicKey, getOptionsMap(signatureOptions)});
        } catch (NoSuchMethodException | ScriptException var6) {
            Exception e = var6;
            e.printStackTrace();
        }

        return result;
    }

    public static boolean doVerifySignature(String msg, String signHex, String publicKey) {
        return doVerifySignature(msg, signHex, publicKey, (SignatureOptions)null);
    }

    private static Map<String, Object> getOptionsMap(SignatureOptions signatureOptions) {
        Map<String, Object> options = new HashMap();
        if (signatureOptions == null) {
            return options;
        } else {
            if (signatureOptions.getPointPool() != null && signatureOptions.getPointPool().size() == 4) {
                options.put("pointPool", signatureOptions.getPointPool());
            }

            if (signatureOptions.isDer()) {
                options.put("der", signatureOptions.isDer());
            }

            if (signatureOptions.isHash()) {
                options.put("hash", signatureOptions.isHash());
            }

            String publicKey = signatureOptions.getPublicKey();
            if (publicKey != null && !publicKey.trim().equals("")) {
                options.put("publicKey", publicKey);
            }

            String userId = signatureOptions.getUserId();
            if (userId != null && !userId.trim().equals("")) {
                options.put("userId", userId);
            }

            return options;
        }
    }

    public static Point getPoint() {
        Point point = null;

        try {
            Bindings bindings = (Bindings)invocable.invokeFunction("getPoint", new Object[0]);
            point = new Point((String)bindings.get("privateKey"), (String)bindings.get("publicKey"), (Map)bindings.get("k"), (Map)bindings.get("x1"));
        } catch (NoSuchMethodException | ScriptException var2) {
            Exception e = var2;
            e.printStackTrace();
        }

        return point;
    }

    static {
        try {
            InputStream inputStream = com.antherd.smcrypto.sm2.Sm2.class.getClassLoader().getResourceAsStream("sm2.js");
            ScriptEngine engine = (new ScriptEngineManager()).getEngineByName("nashorn");
            engine.eval(new BufferedReader(new InputStreamReader(inputStream)));
            invocable = (Invocable)engine;
        } catch (ScriptException var2) {
            ScriptException e = var2;
            e.printStackTrace();
        }

    }
}
