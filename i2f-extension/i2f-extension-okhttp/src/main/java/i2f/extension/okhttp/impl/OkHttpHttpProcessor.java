package i2f.extension.okhttp.impl;

import i2f.net.http.HttpUtil;
import i2f.net.http.consts.ContentTypeConstants;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.net.http.interfaces.IHttpRequestBodyHandler;
import i2f.net.http.interfaces.IHttpResponseExtractor;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.std.str.xml.IXmlSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.serialize.str.xml.impl.Xml2Serializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/6/26 9:32
 * @desc
 */
@Data
@NoArgsConstructor
public class OkHttpHttpProcessor implements IHttpProcessor {
    private OkHttpClient client = createClient();
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected IXmlSerializer xmlSerializer = new Xml2Serializer();

    // 构造函数：接收一个 OkHttpClient 实例（推荐在外部单例配置，以复用连接池）
    public OkHttpHttpProcessor(OkHttpClient client) {
        this.client = client;
    }

    public static OkHttpClient createClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        return okHttpClient;
    }

    @Override
    public <T> T http(HttpRequest request, IHttpResponseExtractor<T> extractor) throws IOException {
        OkHttpClient client = getClient();
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(request.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(request.getReadTimeout(), TimeUnit.MILLISECONDS)
                    .build();
        }
        IHttpRequestBodyHandler<Request.Builder> handler = new OkHttpFormRequestBodyHandler();

        String contentType = request.getHeader().getFirstHeader(HttpHeaderConstants.ContentType);
        if (contentType.contains(ContentTypeConstants.Json)) {
            handler = new OkHttpJsonRequestBodyHandler(jsonSerializer);
        } else if (contentType.contains(ContentTypeConstants.Xml)) {
            handler = new OkHttpXmlRequestBodyHandler(xmlSerializer);
        } else if (contentType.contains(ContentTypeConstants.Form)) {
            handler = new OkHttpFormRequestBodyHandler();
        } else if (contentType.contains(ContentTypeConstants.Multipart)) {
            handler = new OkHttpMultipartFormRequestBodyHandler();
        }

        String reqUrl = HttpUtil.generateUrl(request);

        Request.Builder builder = new Request.Builder()
                .url(reqUrl);

        if (request.getHeader() != null) {
            for (Map.Entry<String, ArrayList<String>> entry : request.getHeader().entrySet()) {
                ArrayList<String> value = entry.getValue();
                if (value == null) {
                    value = new ArrayList<>();
                    value.add(null);
                }
                for (String item : value) {
                    if (item == null) {
                        item = "";
                    }
                    builder.addHeader(entry.getKey(), item);
                }
            }
        }

        Object data = request.getData();
        if (data != null) {
            if (data instanceof byte[]) {
                handler = new OkHttpRawBytesRequestBodyHandler();
            } else if (data instanceof InputStream) {
                handler = new OkHttpRawInputStreamRequestBodyHandler();
            }
        }

        String method = request.getMethod().toUpperCase();
        if (HttpMethodConstants.GET.equals(method)) {
            builder.get();
        } else if (HttpMethodConstants.POST.equals(method)) {
            handler.writeBody(data, request, builder);
        } else if (HttpMethodConstants.PUT.equals(method)) {
            handler.writeBody(data, request, builder);
        } else if (HttpMethodConstants.DELETE.equals(method)) {
            builder.delete();
        }

        Response execute = null;
        HttpResponse response = new HttpResponse();
        boolean autoCloseResource = true;
        try {
            execute = client.newCall(builder.build()).execute();

            response.setStatusCode(execute.code());

            if (execute.headers() != null) {
                HttpHeaders headers = new HttpHeaders();
                Headers rawHeaders = execute.headers();
                Map<String, List<String>> multimap = rawHeaders.toMultimap();
                headers.addAll(multimap);
                response.setHeader(headers);
            }

            ResponseBody body = execute.body();
            InputStream is = body == null ? null : body.byteStream();
            response.setInputStream(is);

            T ret = extractor.extract(response);
            if (ret instanceof HttpResponse) {
                HttpResponse retResp = (HttpResponse) ret;
                retResp.setCloser(new OkHttpCloser(execute));
                autoCloseResource = false;
            }
            return ret;
        } finally {
            if (autoCloseResource) {
                if (execute != null) {
                    execute.close();
                }
            }
        }
    }

    private static class OkHttpCloser implements Closeable {
        private Response execute;

        public OkHttpCloser(Response execute) {
            this.execute = execute;
        }

        @Override
        public void close() throws IOException {
            if (execute != null) {
                execute.close();
                execute = null;
            }
        }
    }
}
