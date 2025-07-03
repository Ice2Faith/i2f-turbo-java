package i2f.extension.swl.impl.bc.test;

import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.extension.swl.impl.bc.SwlBcSm2AsymmetricEncryptor;
import i2f.extension.swl.impl.bc.SwlBcSm3MessageDigester;
import i2f.extension.swl.impl.bc.SwlBcSm4SymmetricEncryptor;
import i2f.swl.core.SwlTransfer;
import i2f.swl.data.SwlData;
import i2f.swl.impl.SwlBase64Obfuscator;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2024/7/10 10:12
 * @desc
 */
public class TestBcSmSwlTransfer {
    public static void main(String[] args) {
        SwlTransfer clientTransfer = new SwlTransfer();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlBcSm2AsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlBcSm4SymmetricEncryptor());
        clientTransfer.setMessageDigester(new SwlBcSm3MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlTransfer serverTransfer = new SwlTransfer();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlBcSm2AsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlBcSm4SymmetricEncryptor());
        serverTransfer.setMessageDigester(new SwlBcSm3MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());

        AsymKeyPair serverKeyPair = serverTransfer.resetSelfKeyPair();
        AsymKeyPair clientKeyPair = clientTransfer.resetSelfKeyPair();

        clientTransfer.acceptOtherPublicKey(serverKeyPair.getPublicKey());
        serverTransfer.acceptOtherPublicKey(clientKeyPair.getPublicKey());

        SwlData clientSendData = clientTransfer.send(serverKeyPair.getPublicKey(), Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.receive(clientId, clientSendData);

        SwlData serverResponseData = serverTransfer.response(serverReceiveData.getHeader().getRemoteAsymSign(), Arrays.asList("echo:ok", "data:ok"));

        String serverId = "server";
        SwlData clientReceiveData = clientTransfer.receive(serverId, serverResponseData);

        System.out.println("ok");
    }
}
