package i2f.swl.core.exchanger;

import i2f.clock.SystemClock;
import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.pool.impl.ObjectPool;
import i2f.swl.cert.SwlCertUtil;
import i2f.swl.cert.data.SwlCert;
import i2f.swl.cert.data.SwlCertPair;
import i2f.swl.consts.SwlCode;
import i2f.swl.core.exchanger.impl.SwlEmptyNonceManager;
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
import lombok.*;

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

    protected boolean enableTimestamp=true;
    protected long timestampExpireWindowSeconds = 30;

    protected boolean enableNonce=false;
    protected long nonceTimeoutSeconds = TimeUnit.MINUTES.toSeconds(30);

    protected boolean enableDigital=true;

    @Setter(AccessLevel.NONE)
    protected Supplier<ISwlAsymmetricEncryptor> asymmetricEncryptorSupplier = new SwlRsaAsymmetricEncryptorSupplier();
    @Setter(AccessLevel.NONE)
    protected Supplier<ISwlSymmetricEncryptor> symmetricEncryptorSupplier = new SwlAesSymmetricEncryptorSupplier();
    protected ISwlMessageDigester messageDigester = new SwlSha256MessageDigester();
    protected ISwlObfuscator obfuscator = new SwlBase64Obfuscator();

    protected SwlNonceManager nonceManager = new SwlEmptyNonceManager();

    protected SecureRandom random = new SecureRandom();

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    protected transient ObjectPool<ISwlAsymmetricEncryptor> asymmetricEncryptorObjectPool = new ObjectPool<>(asymmetricEncryptorSupplier);
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    protected transient ObjectPool<ISwlSymmetricEncryptor> symmetricEncryptorObjectPool = new ObjectPool<>(symmetricEncryptorSupplier);

    public void setAsymmetricEncryptorSupplier(Supplier<ISwlAsymmetricEncryptor> asymmetricEncryptorSupplier) {
        this.asymmetricEncryptorSupplier = asymmetricEncryptorSupplier;
        asymmetricEncryptorObjectPool.setSupplier(this.asymmetricEncryptorSupplier);
    }

    public void setSymmetricEncryptorSupplier(Supplier<ISwlSymmetricEncryptor> symmetricEncryptorSupplier) {
        this.symmetricEncryptorSupplier = symmetricEncryptorSupplier;
        symmetricEncryptorObjectPool.setSupplier(this.symmetricEncryptorSupplier);
    }

    public ISwlAsymmetricEncryptor requireAsymmetricEncryptor() {
        return asymmetricEncryptorObjectPool.require();
    }

    public void releaseAsymmetricEncryptor(ISwlAsymmetricEncryptor encryptor) {
        asymmetricEncryptorObjectPool.release(encryptor);
    }

    public ISwlSymmetricEncryptor requireSymmetricEncryptor() {
        return symmetricEncryptorObjectPool.require();
    }

    public void releaseSymmetricEncryptor(ISwlSymmetricEncryptor encryptor) {
        symmetricEncryptorObjectPool.release(encryptor);
    }

    public AsymKeyPair generateKeyPair() {
        ISwlAsymmetricEncryptor encryptor = asymmetricEncryptorObjectPool.require();
        try {
            return encryptor.generateKeyPair();
        } finally {
            asymmetricEncryptorObjectPool.release(encryptor);
        }
    }

    public String generateKey() {
        ISwlSymmetricEncryptor encryptor = symmetricEncryptorObjectPool.require();
        try {
            return encryptor.generateKey();
        } finally {
            symmetricEncryptorObjectPool.release(encryptor);
        }
    }

    public SwlCertPair generateCertPair(String certId) {
        ISwlAsymmetricEncryptor encryptor = asymmetricEncryptorObjectPool.require();
        try {
            AsymKeyPair serverKeyPair = encryptor.generateKeyPair();
            AsymKeyPair clientKeyPair = encryptor.generateKeyPair();
            return SwlCertUtil.make(certId, serverKeyPair, clientKeyPair);
        } finally {
            asymmetricEncryptorObjectPool.release(encryptor);
        }
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

    public SwlData sendByCert(SwlCert cert,
                              List<String> parts) {
        return sendByCert(cert, parts, null);
    }

    public SwlData sendByCert(SwlCert cert,
                              List<String> parts,
                              List<String> attaches) {
        return sendByRaw(cert.getRemotePublicKey(), cert.getCertId(),
                cert.getPrivateKey(), cert.getCertId(),
                parts, attaches);
    }

    public SwlData sendByRaw(String remotePublicKey, String remoteAsymSign,
                             String selfPrivateKey, String selfAsymSign,
                             List<String> parts) {
        return sendByRaw(remotePublicKey, remoteAsymSign,
                selfPrivateKey, selfAsymSign,
                parts, null);
    }

    public SwlData sendByRaw(String remotePublicKey, String remoteAsymSign,
                             String selfPrivateKey, String selfAsymSign,
                             List<String> parts,
                             List<String> attaches) {
        SwlData ret = new SwlData();
        ret.setParts(new ArrayList<>());
        ret.setAttaches(new ArrayList<>());
        ret.setHeader(new SwlHeader());
        ret.setContext(new SwlContext());

        ret.getContext().setRemotePublicKey(remotePublicKey);
        ret.getHeader().setRemoteAsymSign(remoteAsymSign);
        ret.getContext().setRemoteAsymSign(remoteAsymSign);


        ret.getContext().setSelfPrivateKey(selfPrivateKey);
        ret.getHeader().setLocalAsymSign(selfAsymSign);
        ret.getContext().setSelfAsymSign(selfAsymSign);


        String timestamp = String.valueOf(SystemClock.currentTimeMillis() / 1000);
        ret.getHeader().setTimestamp(timestamp);
        ret.getContext().setTimestamp(timestamp);

        String nonce = String.valueOf(random.nextInt(0x7fff)) + String.valueOf(random.nextInt(0x7fff));
        ret.getHeader().setNonce(nonce);
        ret.getContext().setNonce(nonce);

        ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorObjectPool.require();
        String key = symmetricEncryptor.generateKey();
        ret.getContext().setKey(key);

        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorObjectPool.require();
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

        symmetricEncryptorObjectPool.release(symmetricEncryptor);

        String data = builder.toString();
        ret.getContext().setData(data);

        String sign = messageDigester.digest(data + randomKey + timestamp + nonce + selfAsymSign + remoteAsymSign);
        ret.getHeader().setSign(sign);
        ret.getContext().setSign(sign);

        String digital="none";
        if(enableDigital) {
            asymmetricEncryptor.setPrivateKey(selfPrivateKey);
            digital = asymmetricEncryptor.sign(sign);
        }
        ret.getHeader().setDigital(digital);
        ret.getContext().setDigital(digital);

        asymmetricEncryptorObjectPool.release(asymmetricEncryptor);

        encode(ret.getHeader());

        return ret;
    }

    public SwlHeader encode(SwlHeader header) {
        header.setRandomKey(obfuscateEncode(header.getRandomKey()));
        header.setNonce(obfuscateEncode(header.getNonce()));
        header.setSign(obfuscateEncode(header.getSign()));
        header.setDigital(obfuscateEncode(header.getDigital()));
        return header;
    }

    public SwlHeader decode(SwlHeader header) {
        header.setRandomKey(obfuscateDecode(header.getRandomKey()));
        header.setNonce(obfuscateDecode(header.getNonce()));
        header.setSign(obfuscateDecode(header.getSign()));
        header.setDigital(obfuscateDecode(header.getDigital()));
        return header;
    }

    public SwlHeader swap(SwlHeader header) {
        String str = header.getLocalAsymSign();
        header.setLocalAsymSign(header.getRemoteAsymSign());
        header.setRemoteAsymSign(str);
        return header;
    }

    public SwlData receiveByCert(SwlData request,
                                 SwlCert cert) {
        return receiveByRaw(cert.getCertId(), request,
                cert.getRemotePublicKey(), cert.getPrivateKey());
    }

    public SwlData receiveByCert(SwlData request,
                                 SwlCert cert,
                                 String clientId) {
        return receiveByRaw(clientId, request,
                cert.getRemotePublicKey(), cert.getPrivateKey());
    }

    public SwlData receiveByRaw(String clientId,
                                SwlData request,
                                String remotePublicKey,
                                String selfPrivateKey) {
        SwlData ret = new SwlData();
        ret.setParts(new ArrayList<>());
        ret.setAttaches(new ArrayList<>());
        ret.setHeader(SwlHeader.copy(request.getHeader(), new SwlHeader()));
        ret.setContext(new SwlContext());

        decode(ret.getHeader());

        swap(ret.getHeader());

        ret.getContext().setClientId(clientId);

        long currentTimestamp = SystemClock.currentTimeMillis() / 1000;
        ret.getContext().setCurrentTimestamp(String.valueOf(currentTimestamp));

        String timestamp = ret.getHeader().getTimestamp();
        long ts = Long.parseLong(timestamp);
        ret.getContext().setTimestamp(timestamp);

        long window = timestampExpireWindowSeconds;
        ret.getContext().setWindow(String.valueOf(window));

        if(enableTimestamp) {
            if (Math.abs(currentTimestamp - ts) > window) {
                throw new SwlException(SwlCode.NONCE_TIMESTAMP_EXCEED_EXCEPTION.code(), "timestamp is exceed allow window seconds!");
            }
        }

        String nonce = ret.getHeader().getNonce();
        ret.getContext().setNonce(nonce);
        if (nonce == null || nonce.isEmpty()) {
            throw new SwlException(SwlCode.NONCE_MISSING_EXCEPTION.code(), "nonce cannot is empty!");
        }

        if(enableNonce) {
            if (nonceManager != null) {
                String nonceKey = timestamp + "-" + nonce;
                if (clientId != null && !clientId.isEmpty()) {
                    nonceKey = clientId + "-" + nonce;
                }
                ret.getContext().setNonceKey(nonceKey);
                if (nonceManager.contains(nonceKey)) {
                    throw new SwlException(SwlCode.NONCE_ALREADY_EXISTS_EXCEPTION.code(), "nonce key already exists!");
                }

                nonceManager.set(nonceKey, nonceTimeoutSeconds);
            }
        }

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
        ret.getContext().setSelfAsymSign(localAsymSign);
        if (localAsymSign == null || localAsymSign.isEmpty()) {
            throw new SwlException(SwlCode.SERVER_ASYM_KEY_SIGN_MISSING_EXCEPTION.code(), "local asym sign cannot be empty!");
        }

        boolean signOk = messageDigester.verify(sign, data + randomKey + timestamp + nonce + remoteAsymSign + localAsymSign);
        ret.getContext().setSignOk(signOk);
        if (!signOk) {
            throw new SwlException(SwlCode.SIGN_VERIFY_FAILURE_EXCEPTION.code(), "verify sign failure!");
        }

        String digital = ret.getHeader().getDigital();
        ret.getContext().setDigital(digital);
        if (digital == null || digital.isEmpty()) {
            throw new SwlException(SwlCode.DIGITAL_MISSING_EXCEPTION.code(), "digital cannot be empty!");
        }

        ret.getContext().setRemotePublicKey(remotePublicKey);
        if (remotePublicKey == null || remotePublicKey.isEmpty()) {
            throw new SwlException(SwlCode.CLIENT_ASYM_KEY_NOT_FOUND_EXCEPTION.code(), "remote key not found!");
        }

        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorObjectPool.require();
        asymmetricEncryptor.setPublicKey(remotePublicKey);

        if(enableDigital){
            boolean digitalOk = asymmetricEncryptor.verify(digital, sign);
            ret.getContext().setDigitalOk(digitalOk);
            if (!digitalOk) {
                throw new SwlException(SwlCode.DIGITAL_VERIFY_FAILURE_EXCEPTION.code(), "verify digital failure!");
            }
        }

        ret.getContext().setSelfPrivateKey(selfPrivateKey);
        if (selfPrivateKey == null || selfPrivateKey.isEmpty()) {
            throw new SwlException(SwlCode.SERVER_ASYM_KEY_NOT_FOUND_EXCEPTION.code(), "server key not found!");
        }

        asymmetricEncryptor.setPrivateKey(selfPrivateKey);
        String key = asymmetricEncryptor.decrypt(randomKey);
        ret.getContext().setKey(key);
        if (key == null || key.isEmpty()) {
            throw new SwlException(SwlCode.RANDOM_KEY_INVALID_EXCEPTION.code(), "random key is invalid!");
        }

        asymmetricEncryptorObjectPool.release(asymmetricEncryptor);

        ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorObjectPool.require();
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

        symmetricEncryptorObjectPool.release(symmetricEncryptor);

        return ret;
    }


}
