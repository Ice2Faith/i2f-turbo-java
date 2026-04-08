import SwlWebCtrl from "./SwlWebCtrl";
import SwlRsaAsymmetricEncryptorSupplier from "../core/impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlSha256MessageDigesterSupplier from "../core/impl/supplier/SwlSha256MessageDigesterSupplier";
import SwlBase64Obfuscator from "../core/impl/SwlBase64Obfuscator";
import SwlAesSymmetricEncryptorSupplier from "../core/impl/supplier/SwlAesSymmetricEncryptorSupplier";

/**
 * @return SwlWebConfig
 * @constructor
 */
function SwlWebConfig() {
    /**
     * @type {boolean}
     */
    this.enable=true;
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
    this.certIdName = "swlci";
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
    this.urlPatterns = null;
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
     * @type {ISwlMessageDigesterSupplier}
     */
    this.digestAlgoSupplier = new SwlSha256MessageDigesterSupplier();
    /**
     *
     * @type {ISwlObfuscator}
     */
    this.obfuscateAlgoSupplier = new SwlBase64Obfuscator();
}

export default SwlWebConfig
