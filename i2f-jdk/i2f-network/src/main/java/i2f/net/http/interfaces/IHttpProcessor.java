package i2f.net.http.interfaces;


import i2f.net.http.HttpUtil;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:05
 * @desc
 */
public interface IHttpProcessor {
    /**
     * 阻塞，会等待所有数据传输到本地或者临时文件中，此时得到的 response 对象的流都是在本机的
     *
     * @param request
     * @return
     * @throws IOException
     */
    default HttpResponse http2Local(HttpRequest request) throws IOException {
        return http(request, response -> {
            return HttpUtil.localStoredResponse(request, response);
        });
    }

    /**
     * 非阻塞，不等待所有数据传输结束，适用于流式处理场景，此时得到的流都是原始的流
     * 但是根据具体的实现，在某些底层实现中，可能因为底层组件会自动关闭原始流
     * 所以，可能会变为阻塞等待所有内容缓存到本地后返回
     *
     * @param request
     * @return
     * @throws IOException
     */
    default HttpResponse http(HttpRequest request) throws IOException {
        return http(request, response -> {
            return response;
        });
    }

    /**
     * 非阻塞，不等待所有数据传输结束，适用于流式处理场景
     *
     * @param request
     * @param extractor
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T http(HttpRequest request, IHttpResponseExtractor<T> extractor) throws IOException;
}
