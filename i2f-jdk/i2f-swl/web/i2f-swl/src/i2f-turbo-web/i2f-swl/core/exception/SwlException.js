import Exception from "../../../i2f-core/excception/Exception";

/**
 * 定义异常
 * @param code {int}
 * @param msg {String}
 * @param cause {Error}
 * @return {SwlException}
 * @extends {Exception}
 */
function SwlException(code, msg = null, cause = null) {
    Exception.call(this, msg, cause);
    this._code = code
}

// 继承
SwlException.prototype = Object.create(Exception.prototype)
SwlException.prototype.constructor = SwlException

SwlException.prototype.code = function () {
    return this._code
}

export default SwlException
