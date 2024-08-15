/**
 * @return {Base64Codec}
 * @constructor
 */
function Base64Codec(){

}

/**
 *
 * @param str {string}
 * @return {string}
 */
Base64Codec.encode=function(str){
    return btoa(str)
}

/**
 *
 * @param b64 {string} base64
 * @return {string}
 */
Base64Codec.decode=function (b64){
    return atob(b64)
}

/**
 *
 * @param str {string}
 * @return {string}
 */
Base64Codec.prototype.encode=function(str){
    return Base64Codec.encode(str)
}

/**
 *
 * @param b64 {string} base64
 * @return {string}
 */
Base64Codec.prototype.decode=function (b64){
    return Base64Codec.decode(b64)
}


export default Base64Codec