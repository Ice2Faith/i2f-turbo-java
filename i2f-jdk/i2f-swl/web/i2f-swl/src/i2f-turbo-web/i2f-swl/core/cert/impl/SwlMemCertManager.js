import SwlCertManager from "../SwlCertManager";

/**
 * @return {SwlMemCertManager}
 * @extends {SwlCertManager}
 */
function SwlMemCertManager() {
    /**
     * 继承父类属性
     */
    SwlCertManager.call(this)
    /**
     *
     * @type {Map|Object}
     */
    this.certMap=new Map()
}

// 继承
SwlMemCertManager.prototype = Object.create(SwlCertManager.prototype)
SwlMemCertManager.prototype.constructor = SwlMemCertManager

/**
 * @return {string}
 */
SwlMemCertManager.SERVER_SUFFIX=function(){
    return ".server"
}

/**
 * @return {string}
 */
SwlMemCertManager.CLIENT_SUFFIX=function(){
    return ".client"
}

/**
 * @param certId {String}
 * @param server {boolean}
 * @return {String}
 */
SwlMemCertManager.prototype.getCertName = function (certId,server) {
    if (server) {
        return certId + SwlMemCertManager.SERVER_SUFFIX();
    }
    return certId + SwlMemCertManager.CLIENT_SUFFIX();
}


/**
 * @param certId {String}
 * @param server {boolean}
 * @return {SwlCert}
 */
SwlMemCertManager.prototype.load = function (certId,server) {
    let certName = this.getCertName(certId, server);
    let ret = this.certMap[certName];
    if (ret != null) {
        return ret.copy();
    }
    return null;
}
/**
 * @param cert {SwlCert}
 * @param server {boolean}
 * @return {void}
 */
SwlMemCertManager.prototype.store = function (cert,server) {
    if (cert == null) {
        return;
    }
    let certId = cert.certId;
    let certName = this.getCertName(certId, server);
    this.certMap[certName]=cert.copy();
}

export default SwlMemCertManager
