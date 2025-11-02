
/**
 * @type {OpsSecureHelper}
 * @constructor {OpsSecureHelper}
 */
function OpsSecureHelper(){

}

/**
 *
 * @param cert {string}
 * @return {OpsSecureKeyPair}
 */
OpsSecureHelper.deserializeKeyPair=function(cert){
    let json=Base64.decode(cert);
    let ret=new OpsSecureKeyPair();
    let obj = JSON.parse(json);
    ret.publicKey=obj.publicKey;
    ret.privateKey=obj.privateKey;
    return ret;
}

/**
 *
 * @param keyPair {OpsSecureKeyPair}
 * @return {string}
 */
OpsSecureHelper.serializeKeyPair=function(keyPair){
    let json=JSON.stringify(keyPair);
    return Base64.encode(json);
}

OpsSecureHelper.generateCertPair=function(){
    let serverPair = Sm2.generateKeyPairHex();
    let clientPair = Sm2.generateKeyPairHex();
    let serverKeyPair = new OpsSecureKeyPair(clientPair.publicKey, serverPair.privateKey);
    let clientKeyPair = new OpsSecureKeyPair(serverPair.publicKey, clientPair.privateKey);
    let serverCert = OpsSecureHelper.serializeKeyPair(serverKeyPair);
    let clientCert = OpsSecureHelper.serializeKeyPair(clientKeyPair);
    return new OpsSecureCertPair(serverCert, clientCert);
}