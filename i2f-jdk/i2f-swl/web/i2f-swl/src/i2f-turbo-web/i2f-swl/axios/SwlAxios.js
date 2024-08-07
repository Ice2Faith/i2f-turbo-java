import axios from 'axios'
import SwlWebFilter from "../filter/SwlWebFilter";
import SwlTransfer from "../core/core/SwlTransfer";
import LocalStorageExpireCache from "../../i2f-core/cache/impl/LocalStorageExpireCache";

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

const SwlAxios = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: 'http://localhost:8082/app/',
    // 超时
    timeout: 30 * 60 * 1000
})

const swlTransfer = new SwlTransfer()
swlTransfer.cache = new LocalStorageExpireCache()
const swlFilter = new SwlWebFilter(swlTransfer)
SwlAxios.$filter=swlFilter

// request拦截器
SwlAxios.interceptors.request.use(res => {
    res.headers['Host'] = window.location.host
    res.headers['Origin'] = window.location.origin
    res.headers['User-Agent'] = navigator.userAgent
    debugger
    res = swlFilter.requestFilter(res)
    return res
}, error => {
    debugger
    return Promise.reject(error)
})

// 响应拦截器
SwlAxios.interceptors.response.use(res => {
        debugger
        res = swlFilter.responseFilter(res)
        return res
    },
    error => {
        debugger
        error.response=swlFilter.responseFilter(error.response)
        return Promise.reject(error)
    }
)

export default SwlAxios
