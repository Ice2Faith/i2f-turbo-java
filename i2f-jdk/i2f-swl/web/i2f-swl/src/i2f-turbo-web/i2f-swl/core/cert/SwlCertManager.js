import SwlCertPair from "./data/SwlCertPair";

/**
 * @return {SwlCertManager}
 * @constructor
 */
function SwlCertManager() {

}

/**
 * @param certId {String}
 * @param server {boolean}
 * @return {SwlCert}
 */
SwlCertManager.prototype.load = function (certId,server) {

}
/**
 * @param cert {SwlCert}
 * @param server {boolean}
 * @return {void}
 */
SwlCertManager.prototype.store = function (cert,server) {

}
/**
 * @param certId {String}
 * @return {SwlCert}
 */
SwlCertManager.prototype.loadServer = function (certId) {
    return this.load(certId,true)
}
/**
 * @param certId {String}
 * @return {SwlCert}
 */
SwlCertManager.prototype.loadClient = function (certId) {
    return this.load(certId,false)
}
/**
 * @param cert {SwlCert}
 * @return {void}
 */
SwlCertManager.prototype.storeServer = function (cert) {
    this.store(cert,true)
}
/**
 * @param cert {SwlCert}
 * @return {void}
 */
SwlCertManager.prototype.storeClient = function (cert) {
    this.store(cert,false)
}
/**
 *
 * @param certId {String}
 * @return {SwlCertPair}
 */
SwlCertManager.prototype.loadPair = function (certId) {
    let server = this.load(certId, true);
    let client = this.load(certId, false);
    return new SwlCertPair(server, client);
}
/**
 *
 * @param pair {SwlCertPair}
 * @return {void}
 */
SwlCertManager.prototype.storePair = function (pair) {
    this.store(pair.server, true);
    this.store(pair.client, false);
}

export default SwlCertManager
