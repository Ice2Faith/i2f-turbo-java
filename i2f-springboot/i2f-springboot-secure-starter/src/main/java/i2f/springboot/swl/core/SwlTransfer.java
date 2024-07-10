package i2f.springboot.swl.core;

import i2f.cache.expire.IExpireCache;
import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.clock.SystemClock;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.springboot.swl.consts.SwlCode;
import i2f.springboot.swl.data.SwlData;
import i2f.springboot.swl.data.SwlHeader;
import i2f.springboot.swl.exception.SwlException;
import i2f.springboot.swl.std.ISwlAsymmetricEncryptor;
import i2f.springboot.swl.std.ISwlMessageDigester;
import i2f.springboot.swl.std.ISwlObfuscator;
import i2f.springboot.swl.std.ISwlSymmetricEncryptor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/7/9 20:22
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlTransfer {
    public static final String SELF_KEY_PAIR_CURRENT_KEY = "swl:key:self:current";
    public static final String SELF_KEY_PAIR_HISTORY_KEY_PREFIX = "swl:key:self:history:";
    public static final String OTHER_KEY_PUBLIC_KEY_PREFIX = "swl:key:other:";
    public static final String NONCE_PREFIX = "swl:nonce:";

    private Supplier<ISwlAsymmetricEncryptor> asymmetricEncryptorSupplier;
    private Supplier<ISwlSymmetricEncryptor> symmetricEncryptorSupplier;
    private ISwlMessageDigester messageDigester;
    private IExpireCache<String, Object> cache = new ObjectExpireCacheWrapper<>(new MapCache<>(new ConcurrentHashMap<>()));
    private ISwlObfuscator obfuscator;
    private long nonceWindowSeconds =30;
    private long nonceTimeoutSeconds = 30;
    private long selfKeyExpireSeconds = TimeUnit.HOURS.toSeconds(24);
    private int selfKeyMaxCount = 3;
    private long otherKeyExpireSeconds = TimeUnit.HOURS.toSeconds(24);
    private SecureRandom random=new SecureRandom();

    public AsymKeyPair getSelfKeyPair() {
        Object obj = cache.get(SELF_KEY_PAIR_CURRENT_KEY);
        if (obj == null) {
            return resetSelfKeyPair();
        }
        String key = SELF_KEY_PAIR_HISTORY_KEY_PREFIX + obj;
        obj = cache.get(key);
        if (obj == null) {
            return resetSelfKeyPair();
        }

        AsymKeyPair ret = (AsymKeyPair) obj;
        ret.setPublicKey(obfuscateDecode(ret.getPublicKey()));
        ret.setPrivateKey(obfuscateDecode(ret.getPrivateKey()));
        return ret;
    }

    public void setSelfKeyPair(String selfAsymSign, AsymKeyPair selfKeyPair) {
        String key = SELF_KEY_PAIR_HISTORY_KEY_PREFIX + selfAsymSign;
        AsymKeyPair keyPair = new AsymKeyPair(
                obfuscateEncode(selfKeyPair.getPublicKey()),
                obfuscateEncode(selfKeyPair.getPrivateKey())
        );
        cache.set(key, keyPair, selfKeyMaxCount * selfKeyExpireSeconds, TimeUnit.SECONDS);
        cache.set(SELF_KEY_PAIR_CURRENT_KEY, selfAsymSign);
    }

    public String getSelfPrivateKey(String selfAsymSign) {
        String key = SELF_KEY_PAIR_HISTORY_KEY_PREFIX + selfAsymSign;
        Object obj = cache.get(key);
        if (obj == null) {
            return null;
        }
        AsymKeyPair asymKeyPair = (AsymKeyPair) obj;
        return obfuscateDecode(asymKeyPair.getPrivateKey());
    }

    public String getOtherPublicKey(String otherAsymSign) {
        String key = OTHER_KEY_PUBLIC_KEY_PREFIX + otherAsymSign;
        Object obj = cache.get(key);
        if (obj == null) {
            return null;
        }
        return obfuscateDecode((String) obj);
    }

    public void setOtherPublicKey(String otherAsymSign, String publicKey) {
        String key = OTHER_KEY_PUBLIC_KEY_PREFIX + otherAsymSign;
        cache.set(key, obfuscateEncode(publicKey), otherKeyExpireSeconds, TimeUnit.SECONDS);
    }

    public void refreshOtherPublicKeyExpire(String otherAsymSign) {
        String key = OTHER_KEY_PUBLIC_KEY_PREFIX + otherAsymSign;
        cache.expire(key, otherKeyExpireSeconds, TimeUnit.SECONDS);
    }

    public boolean containsNonce(String nonce) {
        String key = NONCE_PREFIX + nonce;
        return cache.exists(key);
    }

    public void setNonce(String nonce, long timeoutSeconds) {
        String key = NONCE_PREFIX + nonce;
        cache.set(key, SystemClock.currentTimeMillis(), timeoutSeconds, TimeUnit.SECONDS);
    }

    public void acceptOtherPublicKey(String otherPublicKey) {
        String otherAsymSign = messageDigester.digest(otherPublicKey);
        setOtherPublicKey(otherAsymSign, otherPublicKey);
    }

    public AsymKeyPair resetSelfKeyPair() {
        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorSupplier.get();
        AsymKeyPair asymKeyPair = asymmetricEncryptor.generateKeyPair();
        String selfAsymSign = messageDigester.digest(asymKeyPair.getPublicKey());
        setSelfKeyPair(selfAsymSign, asymKeyPair);
        return asymKeyPair;
    }

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

    public SwlData clientSend(String serverPublicKey, List<String> parts) {
        SwlData ret=new SwlData();
        ret.setParts(new ArrayList<>());
        ret.setHeader(new SwlHeader());

        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorSupplier.get();
        ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorSupplier.get();

        AsymKeyPair selfKeyPair = getSelfKeyPair();

        String clientAsymSign=messageDigester.digest(selfKeyPair.getPublicKey());
        ret.getHeader().setClientAsymSign(clientAsymSign);

        String serverAsymSign=messageDigester.digest(serverPublicKey);
        ret.getHeader().setServerAsymSign(serverAsymSign);

        long timestamp = SystemClock.currentTimeMillis() / 1000;
        long seq=random.nextInt(0x7fff);
        String nonce=Long.toString(timestamp,16)+"-"+Long.toString(seq,16);
        ret.getHeader().setNonce(nonce);

        String key=symmetricEncryptor.generateKey();

        asymmetricEncryptor.setPublicKey(serverPublicKey);
        String randomKey=asymmetricEncryptor.encrypt(key);
        ret.getHeader().setRandomKey(randomKey);

        symmetricEncryptor.setKey(key);
        StringBuilder builder=new StringBuilder();
        for (String part : parts) {
            String data=null;
            if(part!=null){
                data = symmetricEncryptor.encrypt(part);
            }
            if(data!=null){
                builder.append(data);
            }
            ret.getParts().add(data);
        }

        String data=builder.toString();

        String sign=messageDigester.digest(data+randomKey+nonce+clientAsymSign+serverAsymSign);
        ret.getHeader().setSign(sign);

        asymmetricEncryptor.setPrivateKey(selfKeyPair.getPrivateKey());
        String digital=asymmetricEncryptor.sign(sign);
        ret.getHeader().setDigital(digital);

        ret.getHeader().setClientAsymSign(obfuscateEncode(ret.getHeader().getClientAsymSign()));
        ret.getHeader().setServerAsymSign(obfuscateEncode(ret.getHeader().getServerAsymSign()));
        ret.getHeader().setRandomKey(obfuscateEncode(ret.getHeader().getRandomKey()));
        ret.getHeader().setNonce(obfuscateEncode(ret.getHeader().getNonce()));
        ret.getHeader().setSign(obfuscateEncode(ret.getHeader().getSign()));
        ret.getHeader().setDigital(obfuscateEncode(ret.getHeader().getDigital()));
        return ret;
    }

    public SwlData serverReceive(String clientId,SwlData request){
        SwlData ret=new SwlData();
        ret.setParts(new ArrayList<>());
        ret.setHeader(SwlHeader.copy(request.getHeader()));

        ret.getHeader().setClientAsymSign(obfuscateDecode(ret.getHeader().getClientAsymSign()));
        ret.getHeader().setServerAsymSign(obfuscateDecode(ret.getHeader().getServerAsymSign()));
        ret.getHeader().setRandomKey(obfuscateDecode(ret.getHeader().getRandomKey()));
        ret.getHeader().setNonce(obfuscateDecode(ret.getHeader().getNonce()));
        ret.getHeader().setSign(obfuscateDecode(ret.getHeader().getSign()));
        ret.getHeader().setDigital(obfuscateDecode(ret.getHeader().getDigital()));

        long currentTimestamp=SystemClock.currentTimeMillis()/1000;

        String nonce = ret.getHeader().getNonce();
        if(nonce==null || nonce.isEmpty()){
            throw new SwlException(SwlCode.NONCE_MISSING_EXCEPTION.code(), "nonce cannot is empty!");
        }

        String[] nonceArr = nonce.split("-", 2);
        if(nonceArr.length!=2){
            throw new SwlException(SwlCode.NONCE_INVALID_EXCEPTION.code(), "nonce is invalid!");
        }

        long timestamp=Long.parseLong(nonceArr[0],16);
        String seq=nonceArr[1];

        long window= nonceWindowSeconds;

        if(Math.abs(currentTimestamp-timestamp)>window){
            throw new SwlException(SwlCode.NONCE_TIMESTAMP_EXCEED_EXCEPTION.code(), "timestamp is exceed allow window seconds!");
        }

        String nonceKey=clientId+nonce;
        if (containsNonce(nonceKey)) {
            throw new SwlException(SwlCode.NONCE_ALREADY_EXISTS_EXCEPTION.code(), "nonce key already exists!");
        }

        setNonce(nonceKey, nonceTimeoutSeconds);

        String sign = ret.getHeader().getSign();
        if(sign==null || sign.isEmpty()){
            throw new SwlException(SwlCode.SIGN_MISSING_EXCEPTION.code(), "sign cannot be empty!");
        }

        StringBuilder builder=new StringBuilder();
        List<String> parts = request.getParts();
        if(parts!=null){
            for(String part : parts){
                if(part!=null){
                    builder.append(part);
                }
            }
        }

        String data = builder.toString();

        String randomKey = ret.getHeader().getRandomKey();
        if(randomKey==null || randomKey.isEmpty()){
            throw new SwlException(SwlCode.RANDOM_KEY_MISSING_EXCEPTION.code(), "random key cannot be empty!");
        }

        String clientAsymSign = ret.getHeader().getClientAsymSign();
        if(clientAsymSign==null || clientAsymSign.isEmpty()){
            throw new SwlException(SwlCode.CLIENT_ASYM_KEY_SIGN_MISSING_EXCEPTION.code(), "client asym sign cannot be empty!");
        }

        String serverAsymSign = ret.getHeader().getServerAsymSign();
        if(serverAsymSign==null || serverAsymSign.isEmpty()){
            throw new SwlException(SwlCode.SERVER_ASYM_KEY_SIGN_MISSING_EXCEPTION.code(), "server asym sign cannot be empty!");
        }

        boolean signOk=messageDigester.verify(sign,data+randomKey+nonce+clientAsymSign+serverAsymSign);
        if(!signOk){
            throw new SwlException(SwlCode.SIGN_VERIFY_FAILURE_EXCEPTION.code(), "verify sign failure!");
        }

        String digital = ret.getHeader().getDigital();
        if(digital==null || digital.isEmpty()){
            throw new SwlException(SwlCode.DIGITAL_MISSING_EXCEPTION.code(), "digital cannot be empty!");
        }

        String clientPublicKey = getOtherPublicKey(clientAsymSign);
        if(clientPublicKey==null || clientPublicKey.isEmpty()){
            throw new SwlException(SwlCode.CLIENT_ASYM_KEY_NOT_FOUND_EXCEPTION.code(), "client key not found!");
        }

        refreshOtherPublicKeyExpire(clientAsymSign);

        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorSupplier.get();
        asymmetricEncryptor.setPublicKey(clientPublicKey);

        boolean digitalOk=asymmetricEncryptor.verify(digital,sign);
        if(!digitalOk){
            throw new SwlException(SwlCode.DIGITAL_VERIFY_FAILURE_EXCEPTION.code(), "verify digital failure!");
        }

        String serverPrivateKey = getSelfPrivateKey(serverAsymSign);
        if(serverPrivateKey==null || serverPrivateKey.isEmpty()){
            throw new SwlException(SwlCode.SERVER_ASYM_KEY_NOT_FOUND_EXCEPTION.code(), "server key not found!");
        }

        asymmetricEncryptor.setPrivateKey(serverPrivateKey);
        String key=asymmetricEncryptor.decrypt(randomKey);
        if(key==null || key.isEmpty()){
            throw new SwlException(SwlCode.RANDOM_KEY_INVALID_EXCEPTION.code(), "random key is invalid!");
        }

        ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorSupplier.get();
        symmetricEncryptor.setKey(key);

        if(parts!=null){
            for (String part : parts) {
                String item=null;
                if(part!=null){
                    item=symmetricEncryptor.decrypt(part);
                }
                ret.getParts().add(item);
            }
        }

        return ret;
    }


}
