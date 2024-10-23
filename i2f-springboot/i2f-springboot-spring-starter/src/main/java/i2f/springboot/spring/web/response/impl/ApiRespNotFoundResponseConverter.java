package i2f.springboot.spring.web.response.impl;

import i2f.resp.ApiCode;
import i2f.resp.ApiResp;
import i2f.springboot.spring.web.response.StandardApiNotFoundResponseConvertor;

import java.util.LinkedHashMap;

/**
 * @author Ice2Faith
 * @date 2024/10/23 20:06
 * @desc
 */
public class ApiRespNotFoundResponseConverter implements StandardApiNotFoundResponseConvertor<ApiResp<?>> {

    @Override
    public ApiResp<?> convert(LinkedHashMap<String, Object> response) {
        return ApiResp.error(ApiCode.NOT_FOUND, "not found!");
    }
}
