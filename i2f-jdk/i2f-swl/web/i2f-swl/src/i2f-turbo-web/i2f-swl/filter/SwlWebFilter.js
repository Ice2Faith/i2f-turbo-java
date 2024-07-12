import SwlTransfer from "../core/core/SwlTransfer";
import SwlWebConfig from "./SwlWebConfig";
import SwlWebCtrl from "./SwlWebCtrl";
import MatcherUtil from "../../i2f-core/match/MatcherUtil";
import qs from 'qs'
import Base64Util from "@/i2f-turbo-web/i2f-core/codec/Base64Util";
import SwlData from "@/i2f-turbo-web/i2f-swl/core/data/SwlData";

/**
 *
 * @param transfer {SwlTransfer}
 * @param config {SwlWebConfig}
 * @constructor
 * @return SwlWebFilter
 */
function SwlWebFilter(transfer = new SwlTransfer(), config = new SwlWebConfig()) {
    /**
     * @type {SwlTransfer}
     */
    this.transfer = transfer;
    /**
     * @type {SwlWebConfig}
     */
    this.config = config;

    this.transfer.asymmetricEncryptorSupplier=config.asymAlgoSupplier
    this.transfer.symmetricEncryptorSupplier=config.symmAlgoSupplier
    this.transfer.messageDigester=config.digestAlgoSupplier
    this.transfer.obfuscator=config.obfuscateAlgoSupplier

}


/**
 *
 * @param res {SwlWebRes}
 * @return {SwlWebRes}
 */
SwlWebFilter.prototype.requestFilter = function (res) {
    let selfPublicKey = this.transfer.getSelfPublicKey();
    let localAsymSign=this.transfer.calcKeySign(selfPublicKey)
    res.headers[this.config.remoteAsymSignHeaderName]=localAsymSign
    let ctrl = SwlWebFilter.parseCtrl(res,this.config);
    if(!ctrl.enableOut){
        return res
    }
    let swlSendContext={}
    let body=null
    if(res.data!=undefined){
        body=JSON.stringify(res.data)
        swlSendContext.data=body
    }
    let params=null
    if(res.params!=undefined){
        params=qs.stringify(res.params)
        swlSendContext.params=params
    }

    let attachedHeaders = []
    if (this.config.attachedHeaderNames) {
        for (let i = 0; i < this.config.attachedHeaderNames.length; i++) {
            let headerName = this.config.attachedHeaderNames[i]
            attachedHeaders.push(res.headers[headerName])
        }
    }

    debugger
    let swlSendData = this.transfer.sendDefault([body, params], attachedHeaders)
    swlSendContext.swlSendData=swlSendData
    let swlh=Base64Util.encrypt(qs.stringify(swlSendData.header))
    swlh=this.transfer.obfuscateEncode(swlh)
    res.data=swlSendData.parts[0]
    let swlp=swlSendData.parts[1]
    if(swlp){
        res.params={}
        res.params[this.config.parameterName]=swlp
    }
    res.headers[this.config.headerName]=swlh
    res.headers[this.config.realContentTypeHeaderName]=SwlWebFilter.getRequestContentType(res)
    res.headers['Content-Type']='text/plain'

    res.swlSendContext=swlSendContext
    return res
}
/**
 *
 * @param res {SwlWebRes}
 * @return {SwlWebRes}
 */
SwlWebFilter.prototype.responseFilter = function (res) {
    if(!res){
        return res
    }
    if(!res.config){
        return res
    }
    let swlReceiveContext={}
    res.config.swlReceiveContext=swlReceiveContext

    let ctrl = SwlWebFilter.parseCtrl(res.config,this.config);
    let swlh=res.headers[this.config.headerName]
    let remoteAsymSign=res.headers[this.config.remoteAsymSignHeaderName]
    let realContentType=res.headers[this.config.realContentTypeHeaderName]
    let currentPublicKey=res.headers[this.config.currentAsymKeyHeaderName]
    if(currentPublicKey && currentPublicKey!=""){
        currentPublicKey=this.transfer.obfuscateDecode(currentPublicKey)
        this.transfer.acceptOtherPublicKey(currentPublicKey)
    }
    if(swlh!=null && swlh!=""){
        ctrl.enableIn=true
    }else{
        ctrl.enableIn=false
    }
    if(!ctrl.enableIn){
        return res
    }
    let body=res.data
    swlh=this.transfer.obfuscateDecode(swlh)
    swlh=Base64Util.decrypt(swlh)
    swlReceiveContext.body=body
    swlReceiveContext.swlh=swlh

    let attachedHeaders = []
    if (this.config.attachedHeaderNames) {
        for (let i = 0; i < this.config.attachedHeaderNames.length; i++) {
            let headerName = this.config.attachedHeaderNames[i]
            attachedHeaders.push(res.headers[headerName])
        }
    }

    let recvData=new SwlData()
    recvData.header=qs.parse(swlh)
    recvData.parts = [body]
    recvData.attaches = attachedHeaders
    let swlReceiveData=this.transfer.receive('server',recvData)
    swlReceiveContext.swlReceiveData=swlReceiveData
    res.headers['Content-Type']=realContentType
    let data=swlReceiveData.parts[0]
    if(data!=null){
        data=JSON.parse(data)
    }
    res.data=data

    return res
}
/**
 *
 * @param res {SwlWebRes}
 * @return {String|null}
 */
SwlWebFilter.getRequestContentType = function (res) {
    let contentType = null
    const method = res.method
    if (contentType == null) {
        Object.keys(res.headers).forEach((key) => {
            const lkey = key.toLowerCase()
            if (lkey == 'content-type') {
                contentType = res.headers[key]
            }
        })
    }
    if (contentType == null) {
        const methodHeader = res.headers[method]
        Object.keys(methodHeader).forEach((key) => {
            const lkey = key.toLowerCase()
            if (lkey == 'content-type') {
                contentType = methodHeader[key]
            }
        })
    }
    return contentType
}

/**
 *
 * @param res {SwlWebRes}
 * @return {string}
 */
SwlWebFilter.getTrimContextPathRequestUri = function (res) {
    let url = SwlWebFilter.getFullURL(res);
    let path= url.pathname
    if(!path){
        path="/";
    }
    if(!path.startsWith("/")){
        path='/'+path;
    }
    return path;
}

/**
 *
 * @param prefix {String}
 * @param suffix {String}
 * @param separator {String}
 * @return {String}
 */
SwlWebFilter.combinePath=function(prefix, suffix, separator='/') {
    if (!prefix || prefix==="") {
        return suffix;
    }
    if (!suffix || suffix==="") {
        return prefix;
    }
    if (prefix.endsWith(separator)) {
        if (suffix.startsWith(separator)) {
            return prefix + suffix.substring(separator.length);
        } else {
            return prefix + suffix;
        }
    } else {
        if (suffix.startsWith(separator)) {
            return prefix + suffix;
        } else {
            return prefix + separator + suffix;
        }
    }
}

/**
 *
 * @param res {SwlWebRes}
 * @return {URL}
 */
SwlWebFilter.getFullURL=function(res){
    let baseUrl=res.baseURL
    let url=res.url
    let fullUrl=url
    if(url.indexOf("://")<0){
        let locationUrl=new URL(window.location.href)
        let domainUrl=locationUrl.protocol+"//"+locationUrl.host
        if(!baseUrl || baseUrl==''){
            baseUrl=SwlWebFilter.combinePath(domainUrl,locationUrl.pathname)
        }else if(baseUrl.startsWith("//")){
            baseUrl=SwlWebFilter.combinePath(domainUrl,baseUrl.substring(2))
        }else if(baseUrl.indexOf('://')<0){
            baseUrl=SwlWebFilter.combinePath(domainUrl,baseUrl)
        }
        fullUrl=SwlWebFilter.combinePath(baseUrl,url)
    }
    return new URL(fullUrl)
}

/**
 *
 * @param res {SwlWebRes}
 * @param config {SwlWebConfig}
 * @return {SwlWebCtrl}
 */
SwlWebFilter.parseCtrl = function (res, config) {
    let defaultCtrl = config.defaultCtrl;

    let contentType = SwlWebFilter.getRequestContentType(res)

    if (!contentType) {
        contentType = ''
    }
    contentType = contentType.toLowerCase()
    if (contentType.indexOf('multipart/form-data') >= 0) {
        return new SwlWebCtrl(defaultCtrl.enableIn, false);
    }


    let path = SwlWebFilter.getTrimContextPathRequestUri(res);

    let enableIn = null;
    let enableOut = null;
    let whiteListIn = config.whiteListIn;
    if (whiteListIn != null) {
        if (MatcherUtil.antUrlMatchedAny(path, whiteListIn)) {
            enableIn = false;
        }
    }
    let whiteListOut = config.whiteListOut;
    if (whiteListOut != null) {
        if (MatcherUtil.antUrlMatchedAny(path, whiteListOut)) {
            enableOut = false;
        }
    }

    return new SwlWebCtrl(enableIn == null ? defaultCtrl.enableIn : enableIn,
        enableOut == null ? defaultCtrl.enableOut : enableOut);
}

export default SwlWebFilter
