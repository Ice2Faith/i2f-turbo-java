import SwlTransfer from "../core/core/SwlTransfer";
import SwlRsaAsymmetricEncryptorSupplier from "../core/impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlAesSymmetricEncryptorSupplier from "../core/impl/supplier/SwlAesSymmetricEncryptorSupplier";
import SwlSha256MessageDigester from "../core/impl/SwlSha256MessageDigester";
import SwlBase64Obfuscator from "../core/impl/SwlBase64Obfuscator";
import SwlTransferRefactor from "@/i2f-turbo-web/i2f-swl/core/core/SwlTransferRefactor";
import SwlKeyExchanger from "@/i2f-turbo-web/i2f-swl/core/core/key/SwlKeyExchanger";

/**
 * @return TestSwlTransferRefactor
 * @constructor
 */
function TestSwlTransferRefactor() {

}

TestSwlTransferRefactor.main = function () {

    let clientTransfer = new SwlTransferRefactor();
    clientTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    clientTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    clientTransfer.messageDigester = (new SwlSha256MessageDigester());
    clientTransfer.obfuscator = (new SwlBase64Obfuscator());

    let serverTransfer = new SwlKeyExchanger();
    serverTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    serverTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    serverTransfer.messageDigester = (new SwlSha256MessageDigester());
    serverTransfer.obfuscator = (new SwlBase64Obfuscator());

    let serverKeyPair = serverTransfer.resetSelfKeyPair();
    let clientKeyPair = clientTransfer.resetSelfKeyPair();

    clientTransfer.acceptOtherPublicKey(serverKeyPair.getPublicKey());
    serverTransfer.acceptOtherPublicKey(clientKeyPair.getPublicKey());

    let clientSendData = clientTransfer.send(serverKeyPair.getPublicKey(), ["body:123456", "query:user=admin"]);


    let clientId = "127.0.0.1";
    let serverReceiveData = serverTransfer.receiveByKey(clientId, clientSendData);

    let serverResponseData = serverTransfer.responseByKey(serverReceiveData.header.remoteAsymSign, ["echo:ok", "data:ok"]);

    let serverId = "server";
    let clientReceiveData = clientTransfer.receive(serverId, serverResponseData);

    console.log("ok");
}

export default TestSwlTransferRefactor
