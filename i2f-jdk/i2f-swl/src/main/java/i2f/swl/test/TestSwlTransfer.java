package i2f.swl.test;

import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.SwlTransfer;
import i2f.swl.data.SwlData;
import i2f.swl.impl.SwlAesSymmetricEncryptor;
import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.impl.SwlRsaAsymmetricEncryptor;
import i2f.swl.impl.SwlSha256MessageDigester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2024/7/10 10:12
 * @desc
 */
public class TestSwlTransfer {
    public static void main(String[] args) {
        testMock();

//        test();
    }

    public static void testMock() {
        SwlTransfer clientTransfer = new SwlTransfer();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        clientTransfer.setMessageDigesterSupplier(() -> new SwlSha256MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlTransfer serverTransfer = new SwlTransfer();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        serverTransfer.setMessageDigesterSupplier(() -> new SwlSha256MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());

        // *********************硬编码的交换秘钥**********************************
        AsymKeyPair swapKeyPair = serverTransfer.generateKeyPair();

        // *********************客户端发送握手请求**********************************
        AsymKeyPair clientKeyPair = clientTransfer.generateKeyPair();

        String payload = UUID.randomUUID().toString().replace("-", "");
        SwlData reqHandleShake = clientTransfer.sendByRaw("swap",
                swapKeyPair.getPublicKey(),
                clientKeyPair.getPrivateKey(),
                new ArrayList<>(Collections.singletonList(payload)),
                new ArrayList<>(Collections.singletonList(clientTransfer.obfuscateEncode(clientKeyPair.getPublicKey())))
        );
        reqHandleShake.setContext(null);

        // ************************服务端接收握手并响应*******************************
        String obfuscateClientPublicKey = reqHandleShake.getAttaches().get(0);
        String clientPublicKey = serverTransfer.obfuscateDecode(obfuscateClientPublicKey);

        SwlData recvReqHandleShake = serverTransfer.receiveByRaw("swap", reqHandleShake,
                clientPublicKey,
                swapKeyPair.getPrivateKey());

        String serverCertId = serverTransfer.acceptOtherSwapKey(obfuscateClientPublicKey);
        String selfPublicKey = serverTransfer.getSelfPublicKey(serverCertId);

        SwlData respHandleShake = serverTransfer.sendByRaw(serverCertId,
                clientPublicKey,
                swapKeyPair.getPrivateKey(),
                new ArrayList<>(Collections.singletonList(selfPublicKey))
        );
        respHandleShake.setContext(null);

        // ************************客户端接收握手响应*******************************
        SwlData recvRespHandleShake = clientTransfer.receiveByRaw("swap", respHandleShake, swapKeyPair.getPublicKey(), clientKeyPair.getPrivateKey());

        String serverPublicKey = recvRespHandleShake.getParts().get(0);

        String clientCertId = clientTransfer.acceptOtherPublicKeyRaw(recvRespHandleShake.getHeader().getCertId(), clientKeyPair, serverPublicKey);

        // ************************客户端发起业务请求*******************************
        SwlData reqBiz = clientTransfer.send(clientCertId, Arrays.asList("hello"));

        // ************************服务端接收业务请求并响应*******************************
        String clientId = "127.0.0.1";
        SwlData recvReqBiz = serverTransfer.receive(clientId, reqBiz);

        String bizBody = recvReqBiz.getParts().get(0);

        SwlData respBiz = serverTransfer.response(recvReqBiz.getHeader().getCertId(), Arrays.asList("echo:" + bizBody));

        // ************************客户端接收业务响应*******************************
        String serverId = "server";
        SwlData recvRespBiz = clientTransfer.receive(serverId, respBiz);

        System.out.println("ok");
    }

    public static void test() {
        SwlTransfer clientTransfer = new SwlTransfer();
        clientTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        clientTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        clientTransfer.setMessageDigesterSupplier(() -> new SwlSha256MessageDigester());
        clientTransfer.setObfuscator(new SwlBase64Obfuscator());

        SwlTransfer serverTransfer = new SwlTransfer();
        serverTransfer.setAsymmetricEncryptorSupplier(() -> new SwlRsaAsymmetricEncryptor());
        serverTransfer.setSymmetricEncryptorSupplier(() -> new SwlAesSymmetricEncryptor());
        serverTransfer.setMessageDigesterSupplier(() -> new SwlSha256MessageDigester());
        serverTransfer.setObfuscator(new SwlBase64Obfuscator());

        AsymKeyPair serverKeyPair = serverTransfer.generateKeyPair();
        AsymKeyPair clientKeyPair = clientTransfer.generateKeyPair();

        String certId = UUID.randomUUID().toString().replace("-", "");

        serverTransfer.acceptOtherPublicKeyRaw(certId, serverKeyPair, clientKeyPair.getPublicKey());
        clientTransfer.acceptOtherPublicKeyRaw(certId, clientKeyPair, serverKeyPair.getPublicKey());

        SwlData clientSendData = clientTransfer.send(certId, Arrays.asList("body:123456", "query:user=admin"));


        String clientId = "127.0.0.1";
        SwlData serverReceiveData = serverTransfer.receive(clientId, clientSendData);

        SwlData serverResponseData = serverTransfer.response(certId, Arrays.asList("echo:ok", "data:ok"));

        String serverId = "server";
        SwlData clientReceiveData = clientTransfer.receive(serverId, serverResponseData);

        System.out.println("ok");
    }
}
