package i2f.swl.test;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.SwlTransferRefactor;
import i2f.swl.core.key.SwlKeyExchanger;
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
public class TestSwlTransfer {
    public static void main(String[] args) {

        SwlTransferRefactor clientTransfer = new SwlTransferRefactor();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        clientTransfer.setMessageDigester(new SwlSha256MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlKeyExchanger serverTransfer = new SwlKeyExchanger();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        serverTransfer.setMessageDigester(new SwlSha256MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());

        AsymKeyPair serverKeyPair = serverTransfer.resetSelfKeyPair();
        AsymKeyPair clientKeyPair = clientTransfer.resetSelfKeyPair();

        clientTransfer.acceptOtherPublicKey(serverKeyPair.getPublicKey());
        serverTransfer.acceptOtherPublicKey(clientKeyPair.getPublicKey());

        SwlData clientSendData = clientTransfer.send(serverKeyPair.getPublicKey(), Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.receiveByKey(clientId, clientSendData);

        SwlData serverResponseData = serverTransfer.responseByKey(serverReceiveData.getHeader().getRemoteAsymSign(), Arrays.asList("echo:ok", "data:ok"));

        String serverId = "server";
        SwlData clientReceiveData = clientTransfer.receive(serverId, serverResponseData);

        System.out.println("ok");
    }
}
