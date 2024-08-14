package i2f.swl.test;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.cert.data.SwlCert;
import i2f.swl.cert.data.SwlCertPair;
import i2f.swl.core.exchanger.SwlExchanger;
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
public class TestSwlExchanger {
    public static void main(String[] args) throws Exception {
//        testRaw();

        testCert();
    }

    public static void testCert() throws Exception {
        SwlExchanger clientTransfer = new SwlExchanger();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        clientTransfer.setMessageDigester(new SwlSha256MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlExchanger serverTransfer = new SwlExchanger();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        serverTransfer.setMessageDigester(new SwlSha256MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());


        SwlCertPair certPair = clientTransfer.generateCertPair("test");
        SwlCert serverCert = certPair.getServer();
        SwlCert clientCert = certPair.getClient();


        SwlData clientSendData = clientTransfer.sendByCert(clientCert,
                Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.receiveByCert(clientId,
                clientSendData,
                serverCert);

        SwlData serverResponseData = serverTransfer.sendByCert(serverCert,
                Arrays.asList("echo:ok", "data:ok"));

        String serverId = "server";
        SwlData clientReceiveData = clientTransfer.receiveByCert(
                serverResponseData,
                clientCert
        );

        System.out.println("ok");
    }

    public static void testRaw() throws Exception {

        SwlExchanger clientTransfer = new SwlExchanger();

        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        clientTransfer.setMessageDigester(new SwlSha256MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlExchanger serverTransfer = new SwlExchanger();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        serverTransfer.setMessageDigester(new SwlSha256MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());


        AsymKeyPair serverKeyPair = clientTransfer.generateKeyPair();
        String serverAsymSign = "server";

        AsymKeyPair clientKeyPair = clientTransfer.generateKeyPair();
        String clientAsymSign = "client";


        SwlData clientSendData = clientTransfer.sendByRaw(serverKeyPair.getPublicKey(), serverAsymSign,
                clientKeyPair.getPrivateKey(), clientAsymSign,
                Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.receiveByRaw(clientId,
                clientSendData,
                clientKeyPair.getPublicKey(),
                serverKeyPair.getPrivateKey());

        SwlData serverResponseData = serverTransfer.sendByRaw(clientKeyPair.getPublicKey(), clientAsymSign,
                serverKeyPair.getPrivateKey(), serverAsymSign
                , Arrays.asList("echo:ok", "data:ok"));

        String serverId = "server";
        SwlData clientReceiveData = clientTransfer.receiveByRaw(serverId,
                serverResponseData,
                serverKeyPair.getPublicKey(),
                clientKeyPair.getPrivateKey());

        System.out.println("ok");
    }
}
