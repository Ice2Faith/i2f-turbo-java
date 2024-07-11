import axios from 'axios'
import SwlWebFilter from "../filter/SwlWebFilter";

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

const SwlAxios = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: 'http://localhost:8081/app/',
    // 超时
    timeout: 30 * 60 * 1000
})

const swlFilter = new SwlWebFilter()
SwlAxios.$filter=swlFilter

// request拦截器
SwlAxios.interceptors.request.use(res => {
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