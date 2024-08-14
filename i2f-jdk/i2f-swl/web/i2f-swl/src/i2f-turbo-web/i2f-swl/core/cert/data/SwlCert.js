/**
 * @return {SwlCert}
 * @constructor
 */
function SwlCert(certId=null,
                 publicKey=null,
                 privateKey=null,
                 remotePublicKey=null) {
    /**
     * 证书ID
     * @type {String}
     */
    this.certId = certId;
    /**
     * 公钥
     * @type {String}
     */
    this.publicKey = publicKey;
    /**
     * 私钥
     * @type {String}
     */
    this.privateKey = privateKey;
    /**
     * 对方公钥
     * @type {String}
     */
    this.remotePublicKey = remotePublicKey;
}

/**
 *
 * @param cert {SwlCert}
 * @return {SwlCert}
 */
SwlCert.copy = function (cert) {
    let ret = new SwlCert();
    ret.certId=cert.certId;
    ret.publicKey = cert.publicKey;
    ret.privateKey = cert.privateKey;
    ret.remotePublicKey = cert.remotePublicKey;
    return ret;
}

/**
 *
 * @return {SwlCert}
 */
SwlCert.prototype.copy=function(){
    return SwlCert.copy(this)
}

export default SwlCert
