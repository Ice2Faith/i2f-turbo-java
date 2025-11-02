

/**
 * @param keyPair {OpsSecureKeyPair}
 * @type {OpsSecureTransfer}
 * @constructor
 */
function OpsSecureTransfer(keyPair){
    /**
     *
     * @type {OpsSecureKeyPair}
     */
    this.keyPair=keyPair;
}

/**
 *
 * @param obj {Object}
 * @return {OpsSecureDto}
 */
OpsSecureTransfer.prototype.send=function(obj){
    let ret=new OpsSecureDto();
    let timestamp=''+Math.round(new Date().getTime()/1000);
    ret.timestamp=timestamp;
    let nonce=''+(Math.round(Math.random()*0x0fffff));
    ret.nonce=nonce;
    let json = JSON.stringify(obj);
    let randomKey = Sm4.generateHexKey();
    let key = Sm2.doEncrypt(randomKey, this.keyPair.publicKey);
    ret.key=key;
    let payload=Sm4.encrypt(json,randomKey);
    ret.payload=payload;
    let sign= Sm3.sm3(timestamp+nonce+key+payload);
    ret.sign=sign;
    let digital = Sm2.doSignature(sign, this.keyPair.privateKey);
    ret.digital=digital;
    return ret;
}

/**
 *
 * @param dto {OpsSecureDto}
 * @return {string}
 */
OpsSecureTransfer.prototype.recvJson=function(dto){
    if(dto==null){
        throw new Error("missing content!");
    }
    let timestamp = dto.timestamp;
    if(!timestamp || timestamp.length==0){
        throw new Error("missing timestamp!");
    }
    let ts = parseInt(timestamp);
    let cts = Math.round(new Date().getTime()/1000);
    if((cts-ts)>30+60){
        throw new Error("exceed timestamp!");
    }
    let nonce = dto.nonce;
    if(!nonce || nonce.length==0){
        throw new Error("missing nonce!");
    }
    let payload = dto.payload;
    if(!payload || payload.length==0){
        throw new Error("missing payload!");
    }
    let key= dto.key;
    if(!key || key.length==0){
        throw new Error("missing key!");
    }
    let sign = dto.sign;
    if(!sign || sign.length==0){
        throw new Error("missing sign!");
    }
    let calcSign = Sm3.sm3(timestamp+nonce+key+payload);
    if(calcSign!=sign){
        throw new Error("invalid sign!");
    }
    let digital = dto.digital;
    if(!digital || digital.length==0){
        throw new Error("missing sign!");
    }
    let ok = Sm2.doVerifySignature(sign, digital, this.keyPair.publicKey);
    if(!ok){
        throw new Error("verify digital failed!");
    }
    let randomKey = Sm2.doDecrypt(key, this.keyPair.privateKey);
    if(!randomKey || randomKey.length==0){
        throw new Error("invalid key!");
    }
    let json = Sm4.decrypt(payload, randomKey);
    if(!json || json.length==0){
        throw new Error("invalid payload!");
    }
    return json;
}

/**
 *
 * @param dto {OpsSecureDto}
 * @return {Object}
 */
OpsSecureTransfer.prototype.recv=function(dto){
    let json = this.recvJson(dto);
    return JSON.parse(json);
}