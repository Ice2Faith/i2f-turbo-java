package i2f.swl.core;

import i2f.clock.SystemClock;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.consts.SwlCode;
import i2f.swl.core.impl.SwlEmptyNonceManager;
import i2f.swl.core.impl.SwlLocalFileKeyManager;
import i2f.swl.data.SwlContext;
import i2f.swl.data.SwlData;
import i2f.swl.data.SwlHeader;
import i2f.swl.exception.SwlException;
import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.impl.SwlSha256MessageDigester;
import i2f.swl.impl.supplier.SwlAesSymmetricEncryptorSupplier;
import i2f.swl.impl.supplier.SwlRsaAsymmetricEncryptorSupplier;
import i2f.swl.std.ISwlAsymmetricEncryptor;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.ISwlObfuscator;
import i2f.swl.std.ISwlSymmetricEncryptor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/7/9 20:22
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlExchanger {

    protected Supplier<ISwlAsymmetricEncryptor> asymmetricEncryptorSupplier = new SwlRsaAsymmetricEncryptorSupplier();
    protected Supplier<ISwlSymmetricEncryptor> symmetricEncryptorSupplier = new SwlAesSymmetricEncryptorSupplier();
    protected ISwlMessageDigester messageDigester = new SwlSha256MessageDigester();
    protected ISwlObfuscator obfuscator = new SwlBase64Obfuscator();

    private long nonceWindowSeconds = TimeUnit.MINUTES.toSeconds(30);
    private long nonceTimeoutSeconds = TimeUnit.MINUTES.toSeconds(30);
    protected SecureRandom random = new SecureRandom();

    protected SwlKeyManager keyManager=new SwlLocalFileKeyManager();

    protected SwlNonceManager nonceManager=new SwlEmptyNonceManager();


    public String getSelfPublicKey(String localAsymSign) {
        AsymKeyPair pair = keyManager.getSelfKeyPair(localAsymSign);
        return pair==null?null:pair.getPublicKey();
    }

    public String getSelfPrivateKey(String localAsymSign) {
        AsymKeyPair pair = keyManager.getSelfKeyPair(localAsymSign);
        return pair==null?null:pair.getPrivateKey();

    }

    public String getOtherPublicKey(String remoteAsymSign) {
        AsymKeyPair pair = keyManager.getOtherKeyPair(remoteAsymSign);
        return pair==null?null:pair.getPublicKey();
    }

    public String getOtherPrivateKey(String remoteAsymSign) {
        AsymKeyPair pair = keyManager.getOtherKeyPair(remoteAsymSign);
        return pair==null?null:pair.getPrivateKey();
    }


    public boolean containsNonce(String nonce) {
        return nonceManager.contains(nonce);
    }

    public void setNonce(String nonce, long timeoutSeconds) {
        nonceManager.set(nonce,timeoutSeconds);
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


    public SwlData send(String remoteAsymSign,
                        String localAsymSign,
                        List<String> parts,
                        List<String> attaches) {
        SwlData ret = new SwlData();
        ret.setParts(new ArrayList<>());
        ret.setAttaches(new ArrayList<>());
        ret.setHeader(new SwlHeader());
        ret.setContext(new SwlContext());

        String remotePublicKey=getOtherPublicKey(remoteAsymSign);

        ret.getContext().setRemotePublicKey(remotePublicKey);

        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorSupplier.get();
        ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorSupplier.get();

        String selfPrivateKey = getSelfPrivateKey(localAsymSign);
        AsymKeyPair selfKeyPair=new AsymKeyPair(null,selfPrivateKey);
        ret.getContext().setSelfKeyPair(selfKeyPair);

        ret.getHeader().setLocalAsymSign(localAsymSign);
        ret.getContext().setLocalAsymSign(localAsymSign);

        ret.getHeader().setRemoteAsymSign(remoteAsymSign);
        ret.getContext().setRemoteAsymSign(remoteAsymSign);

        long timestamp = SystemClock.currentTimeMillis() / 1000;
        long seq = random.nextInt(0x7fff);
        String nonce = Long.toString(timestamp, 16) + "-" + Long.toString(seq, 16);
        ret.getHeader().setNonce(nonce);
        ret.getContext().setTimestamp(String.valueOf(timestamp));
        ret.getContext().setSeq(String.valueOf(seq));
        ret.getContext().setNonce(nonce);

        String key = symmetricEncryptor.generateKey();
        ret.getContext().setKey(key);

        asymmetricEncryptor.setPublicKey(remotePublicKey);
        String randomKey = asymmetricEncryptor.encrypt(key);
        ret.getHeader().setRandomKey(randomKey);
        ret.getContext().setRandomKey(randomKey);

        symmetricEncryptor.setKey(key);
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            String data = null;
            if (part != null) {
                data = symmetricEncryptor.encrypt(part);
            }
            if (data != null) {
                builder.append(data);
            }
            ret.getParts().add(data);
        }
        if (attaches != null) {
            for (String attach : attaches) {
                if (attach != null) {
                    builder.append(attach);
                }
                ret.getAttaches().add(attach);
            }
        }

        String data = builder.toString();
        ret.getContext().setData(data);

        String sign = messageDigester.digest(data + randomKey + nonce + localAsymSign + remoteAsymSign);
        ret.getHeader().setSign(sign);
        ret.getContext().setSign(sign);

        asymmetricEncryptor.setPrivateKey(selfPrivateKey);
        String digital = asymmetricEncryptor.sign(sign);
        ret.getHeader().setDigital(digital);
        ret.getContext().setDigital(digital);

        ret.getHeader().setLocalAsymSign(obfuscateEncode(ret.getHeader().getLocalAsymSign()));
        ret.getHeader().setRemoteAsymSign(obfuscateEncode(ret.getHeader().getRemoteAsymSign()));
        ret.getHeader().setRandomKey(obfuscateEncode(ret.getHeader().getRandomKey()));
        ret.getHeader().setNonce(obfuscateEncode(ret.getHeader().getNonce()));
        ret.getHeader().setSign(obfuscateEncode(ret.getHeader().getSign()));
        ret.getHeader().setDigital(obfuscateEncode(ret.getHeader().getDigital()));
        return ret;
    }

    public SwlData receive(String clientId, SwlData request) {
        SwlData ret = new SwlData();
        ret.setParts(new ArrayList<>());
        ret.setAttaches(new ArrayList<>());
        ret.setHeader(SwlHeader.copy(request.getHeader()));
        ret.setContext(new SwlContext());

        ret.getHeader().setLocalAsymSign(obfuscateDecode(ret.getHeader().getLocalAsymSign()));
        ret.getHeader().setRemoteAsymSign(obfuscateDecode(ret.getHeader().getRemoteAsymSign()));
        ret.getHeader().setRandomKey(obfuscateDecode(ret.getHeader().getRandomKey()));
        ret.getHeader().setNonce(obfuscateDecode(ret.getHeader().getNonce()));
        ret.getHeader().setSign(obfuscateDecode(ret.getHeader().getSign()));
        ret.getHeader().setDigital(obfuscateDecode(ret.getHeader().getDigital()));

        String str = ret.getHeader().getLocalAsymSign();
        ret.getHeader().setLocalAsymSign(ret.getHeader().getRemoteAsymSign());
        ret.getHeader().setRemoteAsymSign(str);

        ret.getContext().setClientId(clientId);

        long currentTimestamp = SystemClock.currentTimeMillis() / 1000;
        ret.getContext().setCurrentTimestamp(String.valueOf(currentTimestamp));

        String nonce = ret.getHeader().getNonce();
        ret.getContext().setNonce(nonce);
        if (nonce == null || nonce.isEmpty()) {
            throw new SwlException(SwlCode.NONCE_MISSING_EXCEPTION.code(), "nonce cannot is empty!");
        }

        String[] nonceArr = nonce.split("-", 2);
        if (nonceArr.length != 2) {
            throw new SwlException(SwlCode.NONCE_INVALID_EXCEPTION.code(), "nonce is invalid!");
        }

        long timestamp = Long.parseLong(nonceArr[0], 16);
        String seq = nonceArr[1];
        ret.getContext().setTimestamp(String.valueOf(timestamp));
        ret.getContext().setSeq(seq);

        long window = nonceWindowSeconds;
        ret.getContext().setWindow(String.valueOf(window));

        if (Math.abs(currentTimestamp - timestamp) > window) {
            throw new SwlException(SwlCode.NONCE_TIMESTAMP_EXCEED_EXCEPTION.code(), "timestamp is exceed allow window seconds!");
        }

        String nonceKey = nonce;
        if (clientId != null && !clientId.isEmpty()) {
            nonceKey = clientId + "-" + nonce;
        }
        ret.getContext().setNonceKey(nonceKey);
        if (containsNonce(nonceKey)) {
            throw new SwlException(SwlCode.NONCE_ALREADY_EXISTS_EXCEPTION.code(), "nonce key already exists!");
        }

        setNonce(nonceKey, nonceTimeoutSeconds);

        String sign = ret.getHeader().getSign();
        ret.getContext().setSign(sign);
        if (sign == null || sign.isEmpty()) {
            throw new SwlException(SwlCode.SIGN_MISSING_EXCEPTION.code(), "sign cannot be empty!");
        }

        StringBuilder builder = new StringBuilder();
        List<String> parts = request.getParts();
        if (parts != null) {
            for (String part : parts) {
                if (part != null) {
                    builder.append(part);
                }
            }
        }

        List<String> attaches = request.getAttaches();
        if (attaches != null) {
            for (String attach : attaches) {
                if (attach != null) {
                    builder.append(attach);
                }
            }
        }

        String data = builder.toString();
        ret.getContext().setData(data);

        String randomKey = ret.getHeader().getRandomKey();
        ret.getContext().setRandomKey(randomKey);
        if (randomKey == null || randomKey.isEmpty()) {
            throw new SwlException(SwlCode.RANDOM_KEY_MISSING_EXCEPTION.code(), "random key cannot be empty!");
        }

        String remoteAsymSign = ret.getHeader().getRemoteAsymSign();
        ret.getContext().setRemoteAsymSign(remoteAsymSign);
        if (remoteAsymSign == null || remoteAsymSign.isEmpty()) {
            throw new SwlException(SwlCode.CLIENT_ASYM_KEY_SIGN_MISSING_EXCEPTION.code(), "remote asym sign cannot be empty!");
        }

        String localAsymSign = ret.getHeader().getLocalAsymSign();
        ret.getContext().setLocalAsymSign(localAsymSign);
        if (localAsymSign == null || localAsymSign.isEmpty()) {
            throw new SwlException(SwlCode.SERVER_ASYM_KEY_SIGN_MISSING_EXCEPTION.code(), "local asym sign cannot be empty!");
        }

        boolean signOk = messageDigester.verify(sign, data + randomKey + nonce + remoteAsymSign + localAsymSign);
        ret.getContext().setSignOk(signOk);
        if (!signOk) {
            throw new SwlException(SwlCode.SIGN_VERIFY_FAILURE_EXCEPTION.code(), "verify sign failure!");
        }

        String digital = ret.getHeader().getDigital();
        ret.getContext().setDigital(digital);
        if (digital == null || digital.isEmpty()) {
            throw new SwlException(SwlCode.DIGITAL_MISSING_EXCEPTION.code(), "digital cannot be empty!");
        }

        String remotePublicKey = getOtherPublicKey(remoteAsymSign);
        ret.getContext().setRemotePublicKey(remotePublicKey);
        if (remotePublicKey == null || remotePublicKey.isEmpty()) {
            throw new SwlException(SwlCode.CLIENT_ASYM_KEY_NOT_FOUND_EXCEPTION.code(), "remote key not found!");
        }

        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorSupplier.get();
        asymmetricEncryptor.setPublicKey(remotePublicKey);

        boolean digitalOk = asymmetricEncryptor.verify(digital, sign);
        ret.getContext().setDigitalOk(digitalOk);
        if (!digitalOk) {
            throw new SwlException(SwlCode.DIGITAL_VERIFY_FAILURE_EXCEPTION.code(), "verify digital failure!");
        }

        String localPrivateKey = getSelfPrivateKey(localAsymSign);
        ret.getContext().setSelfKeyPair(new AsymKeyPair(null, localPrivateKey));
        if (localPrivateKey == null || localPrivateKey.isEmpty()) {
            throw new SwlException(SwlCode.SERVER_ASYM_KEY_NOT_FOUND_EXCEPTION.code(), "server key not found!");
        }

        asymmetricEncryptor.setPrivateKey(localPrivateKey);
        String key = asymmetricEncryptor.decrypt(randomKey);
        ret.getContext().setKey(key);
        if (key == null || key.isEmpty()) {
            throw new SwlException(SwlCode.RANDOM_KEY_INVALID_EXCEPTION.code(), "random key is invalid!");
        }

        ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorSupplier.get();
        symmetricEncryptor.setKey(key);

        if (parts != null) {
            for (String part : parts) {
                String item = null;
                if (part != null) {
                    item = symmetricEncryptor.decrypt(part);
                }
                ret.getParts().add(item);
            }
        }

        return ret;
    }



}