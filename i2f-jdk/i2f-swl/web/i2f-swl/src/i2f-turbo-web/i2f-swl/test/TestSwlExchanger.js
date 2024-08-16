import SwlTransfer from "../core/core/SwlTransfer";
import SwlRsaAsymmetricEncryptorSupplier from "../core/impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlAesSymmetricEncryptorSupplier from "../core/impl/supplier/SwlAesSymmetricEncryptorSupplier";
import SwlSha256MessageDigester from "../core/impl/SwlSha256MessageDigester";
import SwlBase64Obfuscator from "../core/impl/SwlBase64Obfuscator";
import SwlExchanger from "@/i2f-turbo-web/i2f-swl/core/core/exchanger/SwlExchanger";

/**
 * @return TestSwlExchanger
 * @constructor
 */
function TestSwlExchanger() {

}

TestSwlExchanger.main=function(){
    // TestSwlExchanger.testRaw();

    TestSwlExchanger.testCert();
}

TestSwlExchanger.testCert = function () {

    let clientTransfer = new SwlExchanger();
    clientTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    clientTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    clientTransfer.messageDigester = (new SwlSha256MessageDigester());
    clientTransfer.obfuscator = (new SwlBase64Obfuscator());

    let serverTransfer = new SwlExchanger();
    serverTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    serverTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    serverTransfer.messageDigester = (new SwlSha256MessageDigester());
    serverTransfer.obfuscator = (new SwlBase64Obfuscator());

    let certPair = clientTransfer.generateCertPair("test");
    let serverCert = certPair.server;
    let clientCert = certPair.client;

    let clientSendData = clientTransfer.sendByCert(clientCert,
        ["body:123456", "query:user=admin"]);


    let clientId = "127.0.0.1";
    let serverReceiveData = serverTransfer.receiveByCert(
        clientSendData,
        serverCert,
        clientId);

    let serverResponseData = serverTransfer.sendByCert(serverCert,
        ["echo:ok", "data:ok"]);

    let serverId = "server";
    let clientReceiveData = clientTransfer.receiveByCert(
        serverResponseData,
        clientCert
    );

    console.log("ok");
}

TestSwlExchanger.testRaw=function(){
    let clientTransfer = new SwlExchanger();
    clientTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    clientTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    clientTransfer.messageDigester = (new SwlSha256MessageDigester());
    clientTransfer.obfuscator = (new SwlBase64Obfuscator());

    let serverTransfer = new SwlExchanger();
    serverTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    serverTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    serverTransfer.messageDigester = (new SwlSha256MessageDigester());
    serverTransfer.obfuscator = (new SwlBase64Obfuscator());


    let serverKeyPair = clientTransfer.generateKeyPair();
    let serverAsymSign = "server";

    let clientKeyPair = clientTransfer.generateKeyPair();
    let clientAsymSign = "client";


    let clientSendData = clientTransfer.sendByRaw(serverKeyPair.getPublicKey(), serverAsymSign,
        clientKeyPair.getPrivateKey(), clientAsymSign,
        ["body:123456", "query:user=admin"]);


    let clientId = "127.0.0.1";
    let serverReceiveData = serverTransfer.receiveByRaw(clientId,
        clientSendData,
        clientKeyPair.getPublicKey(),
        serverKeyPair.getPrivateKey());

    let serverResponseData = serverTransfer.sendByRaw(clientKeyPair.getPublicKey(), clientAsymSign,
        serverKeyPair.getPrivateKey(), serverAsymSign
        , ["echo:ok", "data:ok"]);

    let serverId = "server";
    let clientReceiveData = clientTransfer.receiveByRaw(serverId,
        serverResponseData,
        serverKeyPair.getPublicKey(),
        clientKeyPair.getPrivateKey());

    console.log("ok");
}

export default TestSwlExchanger
