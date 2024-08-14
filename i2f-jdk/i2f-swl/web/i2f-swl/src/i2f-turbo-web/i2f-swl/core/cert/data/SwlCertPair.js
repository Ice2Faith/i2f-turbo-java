/**
 * @param server {SwlCert}
 * @param client {SwlCert}
 * @return {SwlCertPair}
 * @constructor
 */
function SwlCertPair(server=null,
                     client=null) {
    /**
     * 服务端证书
     * @type {SwlCert}
     */
    this.server = server;
    /**
     * 客户端证书
     * @type {SwlCert}
     */
    this.client = client;
}



export default SwlCertPair
