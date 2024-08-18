import SwlRsaAsymmetricEncryptorSupplier from "../core/impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlAesSymmetricEncryptorSupplier from "../core/impl/supplier/SwlAesSymmetricEncryptorSupplier";
import SwlSha256MessageDigester from "../core/impl/SwlSha256MessageDigester";
import SwlBase64Obfuscator from "../core/impl/SwlBase64Obfuscator";
import SwlKeyExchanger from "../core/core/key/SwlKeyExchanger";
import SwlMemKeyManager from "../core/core/key/impl/SwlMemKeyManager";

/**
 * @return TestSwlTransfer
 * @constructor
 */
function TestSwlTransfer() {

}

TestSwlTransfer.main = function () {

    let clientTransfer = new SwlKeyExchanger();
    clientTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    clientTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    clientTransfer.messageDigester = (new SwlSha256MessageDigester());
    clientTransfer.obfuscator = (new SwlBase64Obfuscator());
    clientTransfer.keyManager=new SwlMemKeyManager()

    let serverTransfer = new SwlKeyExchanger();
    serverTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    serverTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    serverTransfer.messageDigester = (new SwlSha256MessageDigester());
    serverTransfer.obfuscator = (new SwlBase64Obfuscator());
    serverTransfer.keyManager=new SwlMemKeyManager()

    let serverKeyPair = serverTransfer.resetSelfKeyPair();
    let clientKeyPair = clientTransfer.resetSelfKeyPair();

    clientTransfer.acceptOtherPublicKey(serverKeyPair.getPublicKey());
    serverTransfer.acceptOtherPublicKey(clientKeyPair.getPublicKey());

    let clientSendData = clientTransfer.sendByKey(serverKeyPair.getPublicKey(), ["body:123456", "query:user=admin"]);


    let clientId = "127.0.0.1";
    let serverReceiveData = serverTransfer.receiveByKey(clientId, clientSendData);

    let serverResponseData = serverTransfer.responseByKey(serverReceiveData.header.remoteAsymSign, ["echo:ok", "data:ok"]);

    let serverId = "server";
    let clientReceiveData = clientTransfer.receiveByKey(serverId, serverResponseData);

    console.log("ok");
}

export default TestSwlTransfer
