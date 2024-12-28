package i2f.swl.core.key;

import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.exchanger.SwlExchanger;
import i2f.swl.core.key.impl.SwlLocalFileKeyManager;
import i2f.swl.data.SwlData;
import i2f.swl.std.ISwlAsymmetricEncryptor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/9 20:22
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlKeyExchanger extends SwlExchanger {

    protected SwlKeyManager keyManager = new SwlLocalFileKeyManager();

    public AsymKeyPair getSelfKeyPair() {
        return keyManager.getDefaultSelfKeyPair();
    }

    public String getSelfPublicKey() {
        AsymKeyPair keyPair = getSelfKeyPair();
        return keyPair.getPublicKey();
    }

    public String calcKeySign(String publicKey) {
        return messageDigester.digest(publicKey);
    }

    public void setSelfKeyPair(String selfAsymSign, AsymKeyPair selfKeyPair) {
        keyManager.setSelfKeyPair(selfAsymSign, selfKeyPair);
        keyManager.setDefaultSelfAsymSign(selfAsymSign);
    }

    public String getSelfPrivateKey(String selfAsymSign) {
        AsymKeyPair keyPair = keyManager.getSelfKeyPair(selfAsymSign);
        if (keyPair == null) {
            return null;
        }
        return keyPair.getPrivateKey();
    }

    public String getOtherPublicKey(String otherAsymSign) {
        return keyManager.getOtherPublicKey(otherAsymSign);
    }

    public String getOtherPublicKeyDefault() {
        return keyManager.getDefaultOtherPublicKey();
    }

    public void setOtherPublicKey(String otherAsymSign, String publicKey) {
        keyManager.setOtherPublicKey(otherAsymSign, publicKey);
        keyManager.setDefaultOtherAsymSign(otherAsymSign);
    }

    public void acceptOtherPublicKey(String otherPublicKey) {
        String otherAsymSign = messageDigester.digest(otherPublicKey);
        setOtherPublicKey(otherAsymSign, otherPublicKey);
    }

    public String getSelfSwapKey() {
        String selfPublicKey = getSelfPublicKey();
        return obfuscateEncode(selfPublicKey);
    }

    public void acceptOtherSwapKey(String otherSwapKey) {
        String otherPublicKey = obfuscateDecode(otherSwapKey);
        acceptOtherPublicKey(otherPublicKey);
    }

    public AsymKeyPair resetSelfKeyPair() {
        ISwlAsymmetricEncryptor asymmetricEncryptor = requireAsymmetricEncryptor();
        AsymKeyPair asymKeyPair = asymmetricEncryptor.generateKeyPair();
        releaseAsymmetricEncryptor(asymmetricEncryptor);
        String selfAsymSign = messageDigester.digest(asymKeyPair.getPublicKey());
        setSelfKeyPair(selfAsymSign, asymKeyPair);
        return asymKeyPair;
    }

    public SwlData sendByKey(String remotePublicKey, List<String> parts) {
        return sendByKey(remotePublicKey, parts, null);
    }

    public SwlData sendByKey(String remotePublicKey, List<String> parts, List<String> attaches) {
        AsymKeyPair keyPair = getSelfKeyPair();
        return sendByRaw(remotePublicKey, calcKeySign(remotePublicKey),
                keyPair.getPrivateKey(), calcKeySign(keyPair.getPublicKey()),
                parts, attaches);
    }

    public SwlData receiveByKey(String clientId, SwlData request) {
        String selfAsymSign = request.getHeader().getRemoteAsymSign();
        String otherAsymSign = request.getHeader().getLocalAsymSign();
        String otherPublicKey = getOtherPublicKey(otherAsymSign);
        String selfPrivateKey = getSelfPrivateKey(selfAsymSign);
        return receiveByRaw(clientId, request, otherPublicKey, selfPrivateKey);
    }

    public SwlData sendDefaultByKey(List<String> parts, List<String> attaches) {
        String otherPublicKey = getOtherPublicKeyDefault();
        return sendByKey(otherPublicKey, parts, attaches);
    }

    public SwlData sendDefaultByKey(List<String> parts) {
        String otherPublicKey = getOtherPublicKeyDefault();
        return sendByKey(otherPublicKey, parts, null);
    }

    public SwlData responseByKey(String remoteAsymSign, List<String> parts, List<String> attaches) {
        String otherPublicKey = getOtherPublicKey(remoteAsymSign);
        return sendByKey(otherPublicKey, parts, attaches);
    }

    public SwlData responseByKey(String remoteAsymSign, List<String> parts) {
        String otherPublicKey = getOtherPublicKey(remoteAsymSign);
        return sendByKey(otherPublicKey, parts, null);
    }
}
