import SwlWebFilter from '../filter/SwlWebFilter';
import SwlWebConfig from '../filter/SwlWebConfig';
import SwlTransferConfig from '../core/core/SwlTransferConfig';
import SwlTransfer from '../core/core/SwlTransfer';
import LocalStorageExpireCache from '../../i2f-core/cache/impl/LocalStorageExpireCache';
import CodeUtil from '../..//i2f-core/util/CodeUtil';
import Base64Util from '../../i2f-core/codec/Base64Util';
import SwlDto from '../core/data/SwlDto';

const swlTransferConfig = new SwlTransferConfig();
swlTransferConfig.enableEncrypt = true;
const swlTransfer = new SwlTransfer();
swlTransfer.cache = new LocalStorageExpireCache();
swlTransfer.applyConfig(swlTransferConfig);

const swlWebConfig = new SwlWebConfig();
swlWebConfig.enable = true;
swlWebConfig.attachedHeaderNames = [];
const arr = ','.split(',');
for (let i = 0; i < arr.length; i++) {
  /**
   * @type string
   */
  let item = arr[i];
  item = item.trim();
  if (item.length > 0) {
    swlWebConfig.attachedHeaderNames.push(item);
  }
}

export const swlFilter = new SwlWebFilter(swlTransfer, swlWebConfig);

/**
 *
 * @param axiosPromiseCreater {function(AxiosRequestConfig):Promise}
 * @return {Promise<string>}
 */
export function doSwapKey(axiosPromiseCreater) {
  if (swlFilter.config.enable == false) {
    return Promise.resolve(null);
  }
  const clientTransfer = swlFilter.transfer;
  const swapKeyPair = clientTransfer.getSelfSwapKey();

  const clientKeyPair = clientTransfer.generateKeyPair();

  const payload = CodeUtil.makeCheckCode(32, false);
  const reqHandleShake = clientTransfer.sendByRaw(
    'swap',
    swapKeyPair.getPublicKey(),
    clientKeyPair.getPrivateKey(),
    [payload],
    [clientTransfer.obfuscateEncode(clientKeyPair.getPublicKey())]
  );
  reqHandleShake.context = null;

  const reqJson = JSON.stringify(reqHandleShake);
  const reqPayload = Base64Util.encrypt(reqJson);
  const reqData = new SwlDto();
  reqData.payload = reqPayload;
  return axiosPromiseCreater({
    url: 'swl/swapKey',
    method: 'post',
    data: reqData,
  }).then(({ data }) => {
    /**
     * @type {SwlDto}
     */
    const respDto = data;
    const respPayload = respDto.payload;
    const respJson = Base64Util.decrypt(respPayload);
    const respHandleShake = JSON.parse(respJson);

    const recvRespHandleShake = clientTransfer.receiveByRaw(
      'swap',
      respHandleShake,
      swapKeyPair.getPublicKey(),
      clientKeyPair.getPrivateKey()
    );

    const serverPublicKey = recvRespHandleShake.parts[0];

    const clientCertId = clientTransfer.acceptOtherPublicKeyRaw(
      recvRespHandleShake.header.certId,
      clientKeyPair,
      serverPublicKey
    );

    return clientCertId;
  });
}
