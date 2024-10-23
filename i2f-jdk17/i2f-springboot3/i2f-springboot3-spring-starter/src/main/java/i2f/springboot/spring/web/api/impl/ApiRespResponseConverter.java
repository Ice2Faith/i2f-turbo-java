package i2f.springboot.spring.web.api.impl;


import i2f.resp.ApiResp;
import i2f.springboot.spring.web.api.StandardApiResponseConverter;

/**
 * @author Ice2Faith
 * @date 2024/10/23 19:13
 * @desc
 */
public class ApiRespResponseConverter implements StandardApiResponseConverter<ApiResp<?>> {

    @Override
    public ApiResp<?> convert(Object obj) {
        if (obj instanceof ApiResp) {
            return (ApiResp<?>) obj;
        }
        return ApiResp.success(obj);
    }
}
