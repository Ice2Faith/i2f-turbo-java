package i2f.swl.test;

import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.SwlTransfer;
import i2f.swl.data.SwlData;
import i2f.swl.impl.SwlAesSymmetricEncryptor;
import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.impl.SwlRsaAsymmetricEncryptor;
import i2f.swl.impl.SwlSha256MessageDigester;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2024/7/10 10:12
 * @desc
 */
public class TestSwlTransfer {
    public static void main(String[] args) {

        SwlTransfer clientTransfer = new SwlTransfer();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        clientTransfer.setMessageDigester(new SwlSha256MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlTransfer serverTransfer = new SwlTransfer();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        serverTransfer.setMessageDigester(new SwlSha256MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());

        AsymKeyPair serverKeyPair = serverTransfer.generateKeyPair();
        AsymKeyPair clientKeyPair = clientTransfer.generateKeyPair();

        String certId = UUID.randomUUID().toString().replace("-", "");

        serverTransfer.acceptOtherPublicKey(certId, serverKeyPair, clientKeyPair.getPublicKey());
        clientTransfer.acceptOtherPublicKey(certId, clientKeyPair, serverKeyPair.getPublicKey());

        SwlData clientSendData = clientTransfer.send(certId, Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.receive(clientId, clientSendData);

        SwlData serverResponseData = serverTransfer.response(certId, Arrays.asList("echo:ok", "data:ok"));

        String serverId = "server";
        SwlData clientReceiveData = clientTransfer.receive(serverId, serverResponseData);

        System.out.println("ok");
    }
}
