/**
 * @param publicKey {string}
 * @param privateKey {string}
 * @type {OpsSecureKeyPair}
 * @constructor {OpsSecureKeyPair}
 */
function OpsSecureKeyPair(publicKey,privateKey){
    /**
     *
     * @type {string}
     */
    this.publicKey=publicKey;
    /**
     *
     * @type {string}
     */
    this.privateKey=privateKey;

}