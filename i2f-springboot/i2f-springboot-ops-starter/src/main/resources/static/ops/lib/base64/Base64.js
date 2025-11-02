/**
 * @type {Base64}
 * @constructor {Base64}
 */
function Base64(){

}

/**
 *
 * @param str {string}
 * @return {string}
 */
Base64.encode=function(str){
    return btoa(str);
}

/**
 *
 * @param str {string}
 * @return {string}
 */
Base64.decode=function(str){
    return atob(str);
}