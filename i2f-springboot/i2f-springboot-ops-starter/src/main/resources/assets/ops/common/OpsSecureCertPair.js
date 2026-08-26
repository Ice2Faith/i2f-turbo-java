/**
 *
 * @param serverCert {string}
 * @param clientCert {string}
 * @type {OpsSecureCertPair}
 * @constructor {OpsSecureCertPair}
 */
function OpsSecureCertPair(serverCert,clientCert) {
    /**
     * @type {string}
     */
    this.serverCert=serverCert;
    /**
     * @type {string}
     */
    this.clientCert=clientCert;
}