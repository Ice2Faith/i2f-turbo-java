package i2f.swl.test;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.SwlTransfer;
import i2f.swl.core.worker.SwlCert;
import i2f.swl.core.worker.SwlCertPair;
import i2f.swl.core.worker.SwlWorker;
import i2f.swl.data.SwlData;
import i2f.swl.impl.SwlAesSymmetricEncryptor;
import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.impl.SwlRsaAsymmetricEncryptor;
import i2f.swl.impl.SwlSha256MessageDigester;
import i2f.swl.std.ISwlAsymmetricEncryptor;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2024/7/10 10:12
 * @desc
 */
public class TestSwlWorker {
    public static void main(String[] args) throws Exception {
//        testRaw();

        testCert();
    }

    public static void testCert() throws Exception {
        SwlWorker clientTransfer = new SwlWorker();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        clientTransfer.setMessageDigester(new SwlSha256MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlWorker serverTransfer = new SwlWorker();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        serverTransfer.setMessageDigester(new SwlSha256MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());


        SwlCertPair certPair = clientTransfer.generateCertPair("test");
        SwlCert serverCert = certPair.getServer();
        SwlCert clientCert = certPair.getClient();


        SwlData clientSendData = clientTransfer.send(clientCert,
                Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.receive(clientId,
                clientSendData,
                serverCert);

        SwlData serverResponseData = serverTransfer.send(serverCert,
                Arrays.asList("echo:ok", "data:ok"));

        String serverId = "server";
        SwlData clientReceiveData = clientTransfer.receive(
                serverResponseData,
                clientCert
        );

        System.out.println("ok");
    }

    public static void testRaw() throws Exception {

        SwlWorker clientTransfer = new SwlWorker();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        clientTransfer.setMessageDigester(new SwlSha256MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlWorker serverTransfer = new SwlWorker();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        serverTransfer.setMessageDigester(new SwlSha256MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());


        AsymKeyPair serverKeyPair = clientTransfer.generateKeyPair();
        String serverAsymSign = "server";

        AsymKeyPair clientKeyPair = clientTransfer.generateKeyPair();
        String clientAsymSign = "client";


        SwlData clientSendData = clientTransfer.send(serverKeyPair.getPublicKey(), serverAsymSign,
                clientKeyPair.getPrivateKey(), clientAsymSign,
                Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.receive(clientId,
                clientSendData,
                clientKeyPair.getPublicKey(),
                serverKeyPair.getPrivateKey());

        SwlData serverResponseData = serverTransfer.send(clientKeyPair.getPublicKey(), clientAsymSign,
                serverKeyPair.getPrivateKey(), serverAsymSign
                , Arrays.asList("echo:ok", "data:ok"));

        String serverId = "server";
        SwlData clientReceiveData = clientTransfer.receive(serverId,
                serverResponseData,
                serverKeyPair.getPublicKey(),
                clientKeyPair.getPrivateKey());

        System.out.println("ok");
    }
}
