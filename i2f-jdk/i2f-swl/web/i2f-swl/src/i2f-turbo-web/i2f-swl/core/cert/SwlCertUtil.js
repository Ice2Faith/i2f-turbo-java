import SwlCertPair from "./data/SwlCertPair";
import Base64Util from "../../../i2f-core/codec/Base64Util";
import SwlCert from "./data/SwlCert";

/**
 * @return {SwlCertUtil}
 * @interface SwlCertUtil
 */
function SwlCertUtil() {

}

/**
 * @param cert {SwlCert}
 * @return {String}
 */
SwlCertUtil.serialize = function (cert) {
    let builder=''
    builder+="certId="+encodeURIComponent(cert.certId)+'\n'
    builder+="publicKey="+encodeURIComponent(cert.publicKey)+'\n'
    builder+="privateKey="+encodeURIComponent(cert.privateKey)+'\n'
    builder+="remotePublicKey="+encodeURIComponent(cert.remotePublicKey)+'\n'
    return Base64Util.encrypt(builder)
}

/**
 * @param str {String}
 * @return {SwlCert|null}
 */
SwlCertUtil.deserialize = function (str) {
    if(!str || str==''){
        return null
    }
    let ret = new SwlCert();
    let prop=Base64Util.decrypt(str)
    let lines = prop.split('\n');
    for (let i = 0; i < lines.length; i++) {
        let line=lines[i]
        let pair=line.split('=',2)
        if(pair.length<2){
            continue
        }
        let name=pair[0]
        let value=decodeURIComponent(pair[1])
        if("certId"==name){
            ret.certId=value
        }else if("publicKey"==name){
            ret.publicKey=value
        }else if("privateKey"==name){
            ret.privateKey=value
        }else if("remotePublicKey"==name){
            ret.remotePublicKey=value
        }
    }
    return ret
}

/**
 * @param certId {String}
 * @param serverKeyPair {AsymKeyPair}
 * @param clientKeyPair {AsymKeyPair}
 * @return {SwlCertPair}
 */
SwlCertUtil.make = function (certId,serverKeyPair,clientKeyPair) {
    let server=new SwlCert(certId,
        serverKeyPair.getPublicKey(),
        serverKeyPair.getPrivateKey(),
        clientKeyPair.getPublicKey())
    let client=new SwlCert(certId,
        clientKeyPair.getPublicKey(),
        clientKeyPair.getPrivateKey(),
        serverKeyPair.getPublicKey())
    return new SwlCertPair(server,client)
}

export default SwlCertUtil
