package i2f.swl.test;

import i2f.swl.cert.SwlCert;
import i2f.swl.cert.impl.SwlResourceCertManager;
import i2f.swl.core.exchanger.cert.SwlCertExchanger;
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
public class TestSwlCertExchanger {
    public static void main(String[] args) throws Exception {
//        testRaw();

        testCert();
    }

    public static void testCert() throws Exception {
        SwlCertExchanger clientTransfer = new SwlCertExchanger();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        clientTransfer.setMessageDigester(new SwlSha256MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());
        clientTransfer.setCertManager(new SwlResourceCertManager());

        SwlCertExchanger serverTransfer = new SwlCertExchanger();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        serverTransfer.setMessageDigester(new SwlSha256MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());
        serverTransfer.setCertManager(new SwlResourceCertManager());

        String certId = "test";
        serverTransfer.createCertPair(certId);
        SwlCert clientCert = serverTransfer.getCertManager().loadClient(certId);
        clientTransfer.getCertManager().storeClient(clientCert);


        SwlData clientSendData = clientTransfer.send(certId,
                Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.accept(clientId,
                clientSendData,
                certId);

        SwlData serverResponseData = serverTransfer.response(certId,
                Arrays.asList("echo:ok", "data:ok"));

        String serverId = "server";
        SwlData clientReceiveData = clientTransfer.receive(
                serverResponseData,
                clientCert
        );

        System.out.println("ok");
    }

}
