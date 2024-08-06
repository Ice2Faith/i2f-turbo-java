package i2f.swl.core.impl;

import i2f.io.file.FileUtil;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.lru.LruMap;
import i2f.swl.core.SwlKeyManager;
import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.std.ISwlObfuscator;

import java.io.File;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2024/8/4 21:50
 * @desc
 */
public class SwlLocalFileKeyManager implements SwlKeyManager {
    public static final String DEFAULT_KEY_ROOT_DIR_NAME = "./key-data";
    public static final String SELF_KEY_DIR_NAME = "self-key";
    public static final String OTHER_KEY_DIR_NAME = "other-key";
    public static final String SELF_KEY_FILE_SUFFIX = ".self.key";
    public static final String OTHER_KEY_FILE_SUFFIX = ".other.key";
    public static final String KEY_PAIR_SEPARATOR = "\n====\n";

    protected LruMap<String, AsymKeyPair> selfCache = new LruMap<>(1024);
    protected LruMap<String, AsymKeyPair> otherCache = new LruMap<>(1024);

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

    public File getSelfKeyFile(String selfAsymSign){
        return new File(getSelfKeyDir(), selfAsymSign+ SELF_KEY_FILE_SUFFIX);
    }

    public File getOtherKeyFile(String otherAsymSign){
        return new File(getOtherKeyDir(), otherAsymSign+ OTHER_KEY_FILE_SUFFIX);
    }

    public String serializeKeyPair(AsymKeyPair keyPair){
        StringBuilder builder=new StringBuilder();
        builder.append(keyPair.getPublicKey()==null?"":obfuscateEncode(keyPair.getPublicKey()));
        builder.append(KEY_PAIR_SEPARATOR);
        builder.append(keyPair.getPrivateKey()==null?"":obfuscateEncode(keyPair.getPrivateKey()));
        return builder.toString();
    }

    public AsymKeyPair deserializeKeyPair(String str){
        String[] arr = str.split(KEY_PAIR_SEPARATOR, 2);
        String publicKey = arr[0];
        if(publicKey.isEmpty()){
            publicKey=null;
        }
        String privateKey = null;
        if(arr.length>1){
            privateKey=arr[1];
        }
        if(privateKey!=null && privateKey.isEmpty()){
            privateKey=null;
        }
        return new AsymKeyPair(
                obfuscateDecode(publicKey),
                obfuscateDecode(privateKey)
        );
    }

    public AsymKeyPair loadKeyPair(File file) throws IOException {
        if(!file.exists() || !file.isFile()){
            return null;
        }
        String str = FileUtil.loadTxtFile(file);
        return deserializeKeyPair(str);
    }

    public void saveKeyPair(File file,AsymKeyPair keyPair) throws IOException {
        String str = serializeKeyPair(keyPair);
        FileUtil.useParentDir(file);
        FileUtil.save(str,file);
    }

    @Override
    public AsymKeyPair getSelfKeyPair(String selfAsymSign) {
        if(selfAsymSign==null){
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
        if(ret!=null){
            selfCache.put(selfAsymSign,ret);
        }
        return ret;
    }

    @Override
    public void setSelfKeyPair(String selfAsymSign, AsymKeyPair keyPair) {
        try {
            saveKeyPair(getSelfKeyFile(selfAsymSign),keyPair);
        } catch (IOException e) {

        }
    }

    @Override
    public AsymKeyPair getOtherKeyPair(String otherAsymSign) {
        if(otherAsymSign==null){
            return null;
        }
        AsymKeyPair keyPair = otherCache.get(otherAsymSign);
        if (keyPair != null) {
            return keyPair;
        }
        AsymKeyPair ret = null;
        try {
            ret = loadKeyPair(getOtherKeyFile(otherAsymSign));
        } catch (IOException e) {

        }
        if(ret!=null){
            selfCache.put(otherAsymSign,ret);
        }
        return ret;
    }

    @Override
    public void setOtherKeyPair(String otherAsymSign, AsymKeyPair keyPair) {
        try {
            saveKeyPair(getOtherKeyFile(otherAsymSign),keyPair);
        } catch (IOException e) {

        }
    }
}
