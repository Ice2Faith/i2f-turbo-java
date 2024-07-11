/**
 * 定义异常
 * @param msg {String}
 * @param cause {Error} cause
 * @return {Exception}
 * @extends {Error}
 */
function Exception(msg, cause = null) {
    Error.call(this, JSON.stringify({
        msg: msg,
        cause: cause
    }, null, '    '))
    this._msg = msg
    this._cause = cause
}

// 继承
Exception.prototype = Object.create(Error.prototype)
Exception.prototype.constructor = Exception

/**
 *
 * @return {String}
 */
Exception.prototype.msg = function () {
    return this._msg
}

/**
 *
 * @return {null|Error}
 */
Exception.prototype.getCause = function () {
    return this._cause
}

export default Exception
