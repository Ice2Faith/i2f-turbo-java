package i2f.springboot.swl.test;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.springboot.swl.core.SwlTransfer;
import i2f.springboot.swl.data.SwlData;
import i2f.springboot.swl.impl.SwlBase64Obfuscator;
import i2f.springboot.swl.impl.SwlSm2AsymmetricEncryptor;
import i2f.springboot.swl.impl.SwlSm3MessageDigester;
import i2f.springboot.swl.impl.SwlSm4SymmetricEncryptor;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2024/7/10 10:12
 * @desc
 */
public class TestSwlTransfer {
    public static void main(String[] args) {
        SwlTransfer clientTransfer = new SwlTransfer();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlSm2AsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlSm4SymmetricEncryptor());
        clientTransfer.setMessageDigester(new SwlSm3MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlTransfer serverTransfer = new SwlTransfer();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlSm2AsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlSm4SymmetricEncryptor());
        serverTransfer.setMessageDigester(new SwlSm3MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());

        AsymKeyPair serverKeyPair = serverTransfer.resetSelfKeyPair();
        AsymKeyPair clientKeyPair = clientTransfer.resetSelfKeyPair();

        clientTransfer.acceptOtherPublicKey(serverKeyPair.getPublicKey());
        serverTransfer.acceptOtherPublicKey(clientKeyPair.getPublicKey());

        SwlData clientSendData = clientTransfer.clientSend(serverKeyPair.getPublicKey(), Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.serverReceive(clientId, clientSendData);

        System.out.println("ok");
    }
}
