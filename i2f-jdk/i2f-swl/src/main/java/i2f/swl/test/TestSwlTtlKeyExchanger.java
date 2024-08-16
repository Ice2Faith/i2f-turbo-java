package i2f.swl.test;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.key.ttl.SwlTtlKeyExchanger;
import i2f.swl.data.SwlData;
import i2f.swl.impl.SwlAesSymmetricEncryptor;
import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.impl.SwlRsaAsymmetricEncryptor;
import i2f.swl.impl.SwlSha256MessageDigester;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2024/7/10 10:12
 * @desc
 */
public class TestSwlTtlKeyExchanger {
    public static void main(String[] args) {

        SwlTtlKeyExchanger clientTransfer = new SwlTtlKeyExchanger();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        clientTransfer.setMessageDigester(new SwlSha256MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlTtlKeyExchanger serverTransfer = new SwlTtlKeyExchanger();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        serverTransfer.setMessageDigester(new SwlSha256MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());

        AsymKeyPair serverKeyPair = serverTransfer.resetSelfKeyPair();
        AsymKeyPair clientKeyPair = clientTransfer.resetSelfKeyPair();

        clientTransfer.acceptOtherPublicKey(serverKeyPair.getPublicKey());
        serverTransfer.acceptOtherPublicKey(clientKeyPair.getPublicKey());

        SwlData clientSendData = clientTransfer.sendByKey(serverKeyPair.getPublicKey(), Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.receiveByKey(clientId, clientSendData);

        SwlData serverResponseData = serverTransfer.responseByKey(serverReceiveData.getHeader().getRemoteAsymSign(), Arrays.asList("echo:ok", "data:ok"));

        String serverId = "server";
        SwlData clientReceiveData = clientTransfer.receiveByKey(serverId, serverResponseData);

        System.out.println("ok");
    }
}
