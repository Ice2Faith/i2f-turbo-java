/**
 * 定义异常
 * @param type {String}
 * @param msg {String}
 * @return {Error}
 */
function Exception(type, msg) {
    return new Error(JSON.stringify({
        type: type,
        msg: msg
    }, null, '    '))

}

export default Exception
