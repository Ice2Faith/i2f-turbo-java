package i2f.swl.core.key.impl;

import i2f.io.file.FileUtil;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.lru.LruMap;
import i2f.swl.core.key.SwlKeyManager;
import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.std.ISwlObfuscator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2024/8/4 21:50
 * @desc
 */
public class SwlLocalFileKeyManager implements SwlKeyManager {
    public static final String DEFAULT_KEY_ROOT_DIR_NAME = "./swl/key-data";
    public static final String SELF_KEY_DIR_NAME = "self-key";
    public static final String OTHER_KEY_DIR_NAME = "other-key";
    public static final String SELF_KEY_FILE_SUFFIX = ".self.key";
    public static final String OTHER_KEY_FILE_SUFFIX = ".other.key";
    public static final String KEY_PAIR_SEPARATOR = "\n====\n";
    public static final String DEFAULT_SELF_KEY_FILE = "default.self.key.txt";
    public static final String DEFAULT_OTHER_KEY_FILE = "default.other.key.txt";

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected LruMap<String, AsymKeyPair> selfCache = new LruMap<>(1024);
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected LruMap<String, String> otherCache = new LruMap<>(1024);
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected AtomicReference<String> selfDefaultCache = new AtomicReference<>();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected AtomicReference<String> otherDefaultCache = new AtomicReference<>();

    protected File rootDir = new File(DEFAULT_KEY_ROOT_DIR_NAME);

    protected ISwlObfuscator obfuscator = new SwlBase64Obfuscator();


    public String obfuscateEncode(String data) {
        if (data == null) {
            return null;
        }
        if (obfuscator == null) {
            return data;
        }
        return obfuscator.encode(data);
    }

    public String obfuscateDecode(String data) {
        if (data == null) {
            return null;
        }
        if (obfuscator == null) {
            return data;
        }
        return obfuscator.decode(data);
    }

    public File getSelfKeyDir() {
        return new File(rootDir, SELF_KEY_DIR_NAME);
    }

    public File getOtherKeyDir() {
        return new File(rootDir, OTHER_KEY_DIR_NAME);
    }

    public File getSelfKeyFile(String selfAsymSign) {
        return new File(getSelfKeyDir(), selfAsymSign + SELF_KEY_FILE_SUFFIX);
    }

    public File getOtherKeyFile(String otherAsymSign) {
        return new File(getOtherKeyDir(), otherAsymSign + OTHER_KEY_FILE_SUFFIX);
    }

    public File getDefaultSelfKeyFile() {
        return new File(getSelfKeyDir(), DEFAULT_SELF_KEY_FILE);
    }

    public File getDefaultOtherKeyFile() {
        return new File(getOtherKeyDir(), DEFAULT_OTHER_KEY_FILE);
    }

    public String serializeKeyPair(AsymKeyPair keyPair) {
        StringBuilder builder = new StringBuilder();
        builder.append(keyPair.getPublicKey() == null ? "" : obfuscateEncode(keyPair.getPublicKey()));
        builder.append(KEY_PAIR_SEPARATOR);
        builder.append(keyPair.getPrivateKey() == null ? "" : obfuscateEncode(keyPair.getPrivateKey()));
        return builder.toString();
    }

    public AsymKeyPair deserializeKeyPair(String str) {
        String[] arr = str.split(KEY_PAIR_SEPARATOR, 2);
        String publicKey = arr[0];
        if (publicKey.isEmpty()) {
            publicKey = null;
        }
        String privateKey = null;
        if (arr.length > 1) {
            privateKey = arr[1];
        }
        if (privateKey != null && privateKey.isEmpty()) {
            privateKey = null;
        }
        return new AsymKeyPair(
                obfuscateDecode(publicKey),
                obfuscateDecode(privateKey)
        );
    }

    public AsymKeyPair loadKeyPair(File file) throws IOException {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        String str = FileUtil.loadTxtFile(file);
        return deserializeKeyPair(str);
    }

    public void saveKeyPair(File file, AsymKeyPair keyPair) throws IOException {
        String str = serializeKeyPair(keyPair);
        FileUtil.useParentDir(file);
        FileUtil.save(str, file);
    }

    @Override
    public AsymKeyPair getDefaultSelfKeyPair() {
        try {
            String selfAsymSign = getDefaultSelfAsymSign();
            return getSelfKeyPair(selfAsymSign);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public String getDefaultSelfAsymSign() {
        String str = selfDefaultCache.get();
        if (str != null) {
            return str;
        }
        try {
            File file = getDefaultSelfKeyFile();
            String selfAsymSign = FileUtil.loadTxtFile(file);
            if (selfAsymSign != null) {
                selfDefaultCache.set(selfAsymSign);
            }
            return selfAsymSign;
        } catch (IOException e) {

        }
        return null;
    }

    @Override
    public void setDefaultSelfAsymSign(String selfAsymSign) {
        selfDefaultCache.set(selfAsymSign);
        try {
            File file = getDefaultSelfKeyFile();
            if (selfAsymSign == null) {
                file.delete();
                return;
            }
            FileUtil.useParentDir(file);
            FileUtil.save(selfAsymSign, file);
        } catch (IOException e) {

        }
    }

    @Override
    public AsymKeyPair getSelfKeyPair(String selfAsymSign) {
        if (selfAsymSign == null) {
            return null;
        }
        AsymKeyPair keyPair = selfCache.get(selfAsymSign);
        if (keyPair != null) {
            return keyPair;
        }
        AsymKeyPair ret = null;
        try {
            ret = loadKeyPair(getSelfKeyFile(selfAsymSign));
        } catch (IOException e) {

        }
        if (ret != null) {
            selfCache.put(selfAsymSign, ret.copy());
        }
        return ret;
    }

    @Override
    public void setSelfKeyPair(String selfAsymSign, AsymKeyPair keyPair) {
        if (selfAsymSign == null || keyPair == null) {
            return;
        }
        selfCache.put(selfAsymSign, keyPair.copy());
        try {
            saveKeyPair(getSelfKeyFile(selfAsymSign), keyPair);
        } catch (IOException e) {

        }
    }

    @Override
    public String getDefaultOtherPublicKey() {
        try {
            String otherAsymSign = getDefaultOtherAsymSign();
            return getOtherPublicKey(otherAsymSign);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public String getDefaultOtherAsymSign() {
        String str = otherDefaultCache.get();
        if (str != null) {
            return str;
        }
        try {
            File file = getDefaultOtherKeyFile();
            String otherAsymSign = FileUtil.loadTxtFile(file);
            if (otherAsymSign != null) {
                otherDefaultCache.set(otherAsymSign);
            }
            return otherAsymSign;
        } catch (IOException e) {

        }
        return null;
    }

    @Override
    public void setDefaultOtherAsymSign(String otherAsymSign) {
        otherDefaultCache.set(otherAsymSign);
        try {
            File file = getDefaultOtherKeyFile();
            if (otherAsymSign == null) {
                file.delete();
                return;
            }
            FileUtil.useParentDir(file);
            FileUtil.save(otherAsymSign, file);
        } catch (IOException e) {

        }
    }

    @Override
    public String getOtherPublicKey(String otherAsymSign) {
        if (otherAsymSign == null) {
            return null;
        }
        String publicKey = otherCache.get(otherAsymSign);
        if (publicKey != null) {
            return publicKey;
        }
        String ret = null;
        try {
            AsymKeyPair keyPair = loadKeyPair(getOtherKeyFile(otherAsymSign));
            if (keyPair != null) {
                ret = keyPair.getPublicKey();
            }
        } catch (IOException e) {

        }
        if (ret != null) {
            otherCache.put(otherAsymSign, ret);
        }
        return ret;
    }

    @Override
    public void setOtherPublicKey(String otherAsymSign, String publicKey) {
        if (otherAsymSign == null || publicKey == null) {
            return;
        }
        otherCache.put(otherAsymSign, publicKey);
        try {
            saveKeyPair(getOtherKeyFile(otherAsymSign), new AsymKeyPair(publicKey, null));
        } catch (IOException e) {

        }
    }
}
