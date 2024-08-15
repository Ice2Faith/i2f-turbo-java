import SwlExchanger from "../core/exchanger/SwlExchanger";
import SwlStorageCertManager from "./impl/SwlStorageCertManager";

/**
 * @param certManager {SwlCertManager}
 * @return {SwlCertExchanger}
 * @extends {SwlExchanger}
 * @constructor {SwlCertExchanger}
 */
function SwlCertExchanger(certManager=new SwlStorageCertManager()) {
    /**
     *
     * @type {SwlCertManager}
     */
    this.certManager=certManager
}


// 继承
SwlCertExchanger.prototype = Object.create(SwlExchanger.prototype)
SwlCertExchanger.prototype.constructor = SwlCertExchanger


/**
 * @param certId {String}
 * @return {void}
 */
SwlCertExchanger.prototype.createCertPair = function (certId) {
    let pair = this.generateCertPair(certId);
    this.certManager.storePair(pair);
}
/**
 * @param certId {String}
 * @param parts {string[]|null}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlCertExchanger.prototype.sendByCertId = function (certId,parts,attaches=[]) {
    let cert = this.certManager.loadClient(certId);
    return this.sendByCert(cert, parts, attaches);
}
/**
 * @param request {SwlData}
 * @param certId {String}
 * @param clientId {String|null}
 * @return {SwlData}
 */
SwlCertExchanger.prototype.receiveByCertId = function (request,certId,clientId=null) {
    let cert = this.certManager.loadClient(certId);
    if(!clientId){
        clientId=cert.certId
    }
    return this.receiveByCert(request,cert,clientId);
}

/**
 * @param certId {String}
 * @param parts {string[]|null}
 * @param attaches {String[]|null}
 * @return {SwlData}
 */
SwlCertExchanger.prototype.responseByCertId = function (certId,parts,attaches=[]) {
    let cert = this.certManager.loadServer(certId);
    return this.sendByCert(cert, parts, attaches);
}

/**
 * @param request {SwlData}
 * @param certId {String}
 * @param clientId {String|null}
 * @return {SwlData}
 */
SwlCertExchanger.prototype.acceptByCertId = function (request,certId,clientId=[]) {
    let cert = this.certManager.loadServer(certId);
    if(!clientId){
        clientId=certId
    }
    return this.receiveByCert(request, cert,clientId);
}

export default SwlCertExchanger
