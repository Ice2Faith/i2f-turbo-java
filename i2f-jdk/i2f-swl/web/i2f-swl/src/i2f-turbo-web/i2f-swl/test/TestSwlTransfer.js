import SwlTransfer from "../core/core/SwlTransfer";
import SwlRsaAsymmetricEncryptorSupplier from "../core/impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlAesSymmetricEncryptorSupplier from "../core/impl/supplier/SwlAesSymmetricEncryptorSupplier";
import SwlSha256MessageDigesterSupplier from "../core/impl/supplier/SwlSha256MessageDigesterSupplier";
import SwlBase64Obfuscator from "../core/impl/SwlBase64Obfuscator";
import CodeUtil from "../../i2f-core/util/CodeUtil";

/**
 * @return TestSwlTransfer
 * @constructor
 */
function TestSwlTransfer() {

}

TestSwlTransfer.main = function () {

    TestSwlTransfer.testMock();

    // TestSwlTransfer.test();
}

TestSwlTransfer.testMock=function(){
    let clientTransfer = new SwlTransfer();
    clientTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    clientTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    clientTransfer.messageDigesterSupplier = (new SwlSha256MessageDigesterSupplier());
    clientTransfer.obfuscator = (new SwlBase64Obfuscator());

    let serverTransfer = new SwlTransfer();
    serverTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    serverTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    serverTransfer.messageDigesterSupplier = (new SwlSha256MessageDigesterSupplier());
    serverTransfer.obfuscator = (new SwlBase64Obfuscator());

    // *********************硬编码的交换秘钥**********************************
    let swapKeyPair = serverTransfer.generateKeyPair();

    // *********************客户端发送握手请求**********************************
    let clientKeyPair = clientTransfer.generateKeyPair();

    let payload = CodeUtil.makeCheckCode(32,false);
    let reqHandleShake = clientTransfer.sendByRaw("swap",
        swapKeyPair.getPublicKey(),
        clientKeyPair.getPrivateKey(),
        [payload],
        [clientTransfer.obfuscateEncode(clientKeyPair.getPublicKey())]
    );
    reqHandleShake.context=null;

    // ************************服务端接收握手并响应*******************************
    let obfuscateClientPublicKey = reqHandleShake.attaches[0];
    let clientPublicKey = serverTransfer.obfuscateDecode(obfuscateClientPublicKey);

    let recvReqHandleShake = serverTransfer.receiveByRaw("swap", reqHandleShake,
        clientPublicKey,
        swapKeyPair.getPrivateKey());

    let serverCertId = serverTransfer.acceptOtherSwapKey(obfuscateClientPublicKey);
    let selfPublicKey = serverTransfer.getSelfPublicKey(serverCertId);

    let respHandleShake = serverTransfer.sendByRaw(serverCertId,
        clientPublicKey,
        swapKeyPair.getPrivateKey(),
        [selfPublicKey]
    );
    respHandleShake.context=null;

    // ************************客户端接收握手响应*******************************
    let recvRespHandleShake = clientTransfer.receiveByRaw("swap", respHandleShake, swapKeyPair.getPublicKey(), clientKeyPair.getPrivateKey());

    let serverPublicKey = recvRespHandleShake.parts[0];

    let clientCertId = clientTransfer.acceptOtherPublicKeyRaw(recvRespHandleShake.header.certId, clientKeyPair, serverPublicKey);

    // ************************客户端发起业务请求*******************************
    let reqBiz = clientTransfer.send(clientCertId, ["hello"]);

    // ************************服务端接收业务请求并响应*******************************
    let clientId = "127.0.0.1";
    let recvReqBiz = serverTransfer.receive(clientId, reqBiz);

    let bizBody = recvReqBiz.parts[0];

    let respBiz = serverTransfer.response(recvReqBiz.header.certId, ["echo:" + bizBody]);

    // ************************客户端接收业务响应*******************************
    let serverId = "server";
    let recvRespBiz = clientTransfer.receive(serverId, respBiz);

    console.log("ok");
}

TestSwlTransfer.test=function(){
    let clientTransfer = new SwlTransfer();
    clientTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    clientTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    clientTransfer.messageDigesterSupplier = (new SwlSha256MessageDigesterSupplier());
    clientTransfer.obfuscator = (new SwlBase64Obfuscator());

    let serverTransfer = new SwlTransfer();
    serverTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    serverTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    serverTransfer.messageDigesterSupplier = (new SwlSha256MessageDigesterSupplier());
    serverTransfer.obfuscator = (new SwlBase64Obfuscator());

    let serverKeyPair = serverTransfer.generateKeyPair();
    let clientKeyPair = clientTransfer.generateKeyPair();

    let certId = CodeUtil.makeCheckCode(32,false);

    serverTransfer.acceptOtherPublicKeyRaw(certId, serverKeyPair, clientKeyPair.getPublicKey());
    clientTransfer.acceptOtherPublicKeyRaw(certId, clientKeyPair, serverKeyPair.getPublicKey());


    let clientSendData = clientTransfer.send(certId, ["body:123456", "query:user=admin"]);


    let clientId = "127.0.0.1";
    let serverReceiveData = serverTransfer.receive(clientId, clientSendData);

    let serverResponseData = serverTransfer.response(certId, ["echo:ok", "data:ok"]);

    let serverId = "server";
    let clientReceiveData = clientTransfer.receive(serverId, serverResponseData);

    console.log("ok");
}

export default TestSwlTransfer
