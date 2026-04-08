import SwlRsaAsymmetricEncryptorSupplier from "../core/impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlAesSymmetricEncryptorSupplier from "../core/impl/supplier/SwlAesSymmetricEncryptorSupplier";
import SwlSha256MessageDigesterSupplier from "../core/impl/supplier/SwlSha256MessageDigesterSupplier";
import SwlBase64Obfuscator from "../core/impl/SwlBase64Obfuscator";
import SwlCertExchanger from "../core/cert/SwlCertExchanger";
import SwlMemCertManager from "../core/cert/impl/SwlMemCertManager";

/**
 * @return TestSwlCertExchanger
 * @constructor
 */
function TestSwlCertExchanger() {

}

TestSwlCertExchanger.main=function(){
    TestSwlCertExchanger.testCert()
}

TestSwlCertExchanger.testCert = function () {

    let clientTransfer = new SwlCertExchanger();
    clientTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    clientTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    clientTransfer.messageDigesterSupplier = (new SwlSha256MessageDigesterSupplier());
    clientTransfer.obfuscator = (new SwlBase64Obfuscator());
    clientTransfer.certManager=(new SwlMemCertManager())

    let serverTransfer = new SwlCertExchanger();
    serverTransfer.asymmetricEncryptorSupplier = (new SwlRsaAsymmetricEncryptorSupplier());
    serverTransfer.symmetricEncryptorSupplier = (new SwlAesSymmetricEncryptorSupplier());
    serverTransfer.messageDigesterSupplier = (new SwlSha256MessageDigesterSupplier());
    serverTransfer.obfuscator = (new SwlBase64Obfuscator());
    serverTransfer.certManager=(new SwlMemCertManager())


    let certId = "test";
    serverTransfer.createCertPair(certId);
    let clientCert = serverTransfer.certManager.loadClient(certId);
    clientTransfer.certManager.storeClient(clientCert);


    let clientSendData = clientTransfer.sendByCertId(certId,
        ["body:123456", "query:user=admin"]);


    let clientId = "127.0.0.1";
    let serverReceiveData = serverTransfer.acceptByCertId(
        clientSendData,
        certId,
        clientId);

    let serverResponseData = serverTransfer.responseByCertId(certId,
        ["echo:ok", "data:ok"]);

    let serverId = "server";
    let clientReceiveData = clientTransfer.receiveByCert(
        serverResponseData,
        clientCert
    );

    console.log("ok");
}

export default TestSwlCertExchanger
