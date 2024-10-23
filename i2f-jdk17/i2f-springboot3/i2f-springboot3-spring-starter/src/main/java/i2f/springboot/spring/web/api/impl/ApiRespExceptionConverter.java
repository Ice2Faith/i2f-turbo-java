package i2f.springboot.spring.web.api.impl;


import i2f.resp.ApiResp;
import i2f.springboot.spring.web.api.StandardApiExceptionConverter;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * @author Ice2Faith
 * @date 2024/10/23 19:13
 * @desc
 */
public class ApiRespExceptionConverter implements StandardApiExceptionConverter<ApiResp<?>> {

    @Override
    public ApiResp<?> convert(Throwable throwable) {
        if (throwable instanceof ArithmeticException) {
            return ApiResp.error("arithmetic exception!");
        }
        if (throwable instanceof NumberFormatException) {
            return ApiResp.error("number format exception!");
        }
        if (throwable instanceof ParseException) {
            return ApiResp.error("parse exception!");
        }
        if (throwable instanceof SQLException) {
            return ApiResp.error("data operation exception!");
        }
        if (throwable instanceof ConnectException) {
            return ApiResp.error("connect exception!");
        }
        if (throwable instanceof NullPointerException) {
            return ApiResp.error("null exception!");
        }
        if (throwable instanceof IOException) {
            return ApiResp.error("io operation exception!");
        }
        if (throwable instanceof ReflectiveOperationException) {
            return ApiResp.error("reflective operation exception!");
        }
        if (throwable instanceof ClassCastException) {
            return ApiResp.error("class cast exception!");
        }
        if (throwable instanceof SecurityException) {
            return ApiResp.error("security exception!");
        }
        if (throwable instanceof StackOverflowError) {
            return ApiResp.error("stack overflow error!");
        }
        if (throwable instanceof LinkageError) {
            return ApiResp.error("linkage error!");
        }
        if (throwable instanceof RuntimeException) {
            return ApiResp.error("runtime exception!");
        }
        return ApiResp.error("internal exception!");
    }
}
