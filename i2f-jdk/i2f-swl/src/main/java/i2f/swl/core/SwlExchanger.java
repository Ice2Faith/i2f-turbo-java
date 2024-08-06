package i2f.swl.core;

import i2f.clock.SystemClock;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.consts.SwlCode;
import i2f.swl.core.impl.SwlEmptyNonceManager;
import i2f.swl.core.impl.SwlLocalFileKeyManager;
import i2f.swl.core.worker.SwlWorker;
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
public class SwlExchanger extends SwlWorker {

    protected SwlKeyManager keyManager=new SwlLocalFileKeyManager();

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


    public SwlData send(String remoteAsymSign,
                        String localAsymSign,
                        List<String> parts,
                        List<String> attaches) {
        String remotePublicKey=getOtherPublicKey(remoteAsymSign);
        String selfPrivateKey = getSelfPrivateKey(localAsymSign);
        return send(remotePublicKey,remoteAsymSign,
                selfPrivateKey,localAsymSign,
                parts,attaches);
    }

    public SwlData receive(String clientId, SwlData request) {
        // 此时，还没有进行交换，因此remote是对于发送方来说的
        String selfAsymSign = request.getHeader().getRemoteAsymSign();
        String remoteAsymSign = request.getHeader().getLocalAsymSign();
        String remotePublicKey = getOtherPublicKey(remoteAsymSign);
        String localPrivateKey = getSelfPrivateKey(selfAsymSign);
        return receive(clientId,request,remotePublicKey,localPrivateKey);
    }



}
