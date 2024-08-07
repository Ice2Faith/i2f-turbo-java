import SwlWebCtrl from "./SwlWebCtrl";
import SwlRsaAsymmetricEncryptorSupplier from "../core/impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlSha256MessageDigester from "../core/impl/SwlSha256MessageDigester";
import SwlBase64Obfuscator from "../core/impl/SwlBase64Obfuscator";
import SwlAesSymmetricEncryptorSupplier from "../core/impl/supplier/SwlAesSymmetricEncryptorSupplier";

/**
 * @return SwlWebConfig
 * @constructor
 */
function SwlWebConfig() {
    /**
     * @type {SwlWebCtrl}
     */
    this.defaultCtrl = new SwlWebCtrl(true, true);
    /**
     * @type {String}
     */
    this.headerName = "swlh";
    /**
     * @type {String}
     */
    this.remoteAsymSignHeaderName = "swlras";
    /**
     * @type {String}
     */
    this.currentAsymKeyHeaderName = "swlcak";
    /**
     * @type {String}
     */
    this.realContentTypeHeaderName = "swlct";
    /**
     * @type {String}
     */
    this.parameterName = "swlp";
    /**
     * @type {String}
     */
    this.responseCharset = "UTF-8";
    /**
     * @type {boolean}
     */
    this.filterResponseException = false;
    /**
     *
     * @type {string[]}
     */
    this.whiteListIn = ['/**/swl/**'];
    /**
     *
     * @type {string[]}
     */
    this.whiteListOut = ['/**/swl/**'];

    /**
     *
     * @type {string[]}
     */
    this.attachedHeaderNames = null;
    /**
     * @type {ISwlAsymmetricEncryptorSupplier}
     */
    this.asymAlgoSupplier = new SwlRsaAsymmetricEncryptorSupplier();
    /**
     *
     * @type {ISwlSymmetricEncryptorSupplier}
     */
    this.symmAlgoSupplier = new SwlAesSymmetricEncryptorSupplier();
    /**
     *
     * @type {ISwlMessageDigester}
     */
    this.digestAlgoSupplier = new SwlSha256MessageDigester();
    /**
     *
     * @type {ISwlObfuscator}
     */
    this.obfuscateAlgoSupplier = new SwlBase64Obfuscator();
}

export default SwlWebConfig
