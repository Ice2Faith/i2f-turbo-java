package i2f.crypto.std.encrypt.asymmetric.key;

import i2f.codec.str.obfuscate.Base64Obfuscator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author Ice2Faith
 * @date 2023/9/5 22:17
 * @desc
 */
@Data
@NoArgsConstructor
public class AsymKeyPair {
    private String publicKey;
    private String privateKey;

    public AsymKeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public static AsymKeyPair copy(AsymKeyPair pair){
        return new AsymKeyPair(pair.getPublicKey(),pair.getPrivateKey());
    }

    public AsymKeyPair copy(){
        return copy(this);
    }


    public static void saveAsymKey(AsymKeyPair asymKey, OutputStream os) throws IOException {
        String pubKey = asymKey.getPublicKey();
        String priKey = asymKey.getPrivateKey();
        if (pubKey == null) {
            pubKey = "";
        }
        if (priKey == null) {
            priKey = "";
        }
        pubKey = Base64Obfuscator.encode(pubKey, true);
        priKey = Base64Obfuscator.encode(priKey, true);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(pubKey);
        writer.newLine();
        writer.write(priKey);
        writer.newLine();
        writer.flush();
        os.flush();
    }

    public static AsymKeyPair loadAsymKey(InputStream is) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String pubKey = reader.readLine();
        String priKey = reader.readLine();
        pubKey = Base64Obfuscator.decode(pubKey);
        priKey = Base64Obfuscator.decode(priKey);
        if (pubKey == null || pubKey.isEmpty()) {
            pubKey = null;
        }
        if (priKey == null || priKey.isEmpty()) {
            priKey = null;
        }
        return new AsymKeyPair(pubKey, priKey);
    }
}
