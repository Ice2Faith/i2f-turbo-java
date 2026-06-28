package i2f.network.http.proxy.rest.annotations;


import i2f.net.http.impl.HttpUrlConnectProcessor;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.network.http.proxy.rest.HttpProcessorSupplier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ice2Faith
 * @date 2022/5/18 9:14
 * @desc
 */
@Target({
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestClient {
    String url() default "";

    String path() default "";

    Class<? extends HttpProcessorSupplier> httpSupplier() default HttpProcessorSupplier.class;

    Class<? extends IHttpProcessor> http() default HttpUrlConnectProcessor.class;

}
