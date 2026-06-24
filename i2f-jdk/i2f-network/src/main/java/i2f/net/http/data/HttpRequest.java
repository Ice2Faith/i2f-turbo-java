package i2f.net.http.data;

import i2f.net.http.consts.ContentTypeConstants;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.impl.HttpFormUrlEncodedRequestBodyHandler;
import i2f.net.http.impl.HttpJsonRequestBodyHandler;
import i2f.net.http.impl.HttpUrlConnectProcessor;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.net.http.interfaces.IHttpRequestBodyHandler;
import i2f.serialize.std.str.json.IJsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2022/3/24 8:33
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpRequest {

    private String url;
    private String method = HttpMethodConstants.GET;
    private Object params;
    private Object data;
    private HttpHeaders header;
    private List<MultipartFile> files;

    private int connectTimeout = 30 * 1000;
    private int readTimeout = 30 * 1000;
    private String charset = "UTF-8";
    private boolean allowRedirect = true;
    private boolean cloudAcceptByteArray = false;

    private IHttpRequestBodyHandler requestBodyHandler;

    public static HttpRequest doGet() {
        return new HttpRequest()
                .setMethod(HttpMethodConstants.GET);
    }

    public static HttpRequest doGet(String url) {
        return doGet().setUrl(url);
    }

    public static HttpRequest doGet(String url, Map<String, Object> params) {
        return doGet(url).setParams(params);
    }

    public static HttpRequest doPost() {
        return new HttpRequest()
                .setMethod(HttpMethodConstants.POST);
    }

    public static HttpRequest doPost(String url) {
        return doPost().setUrl(url);
    }

    public static HttpRequest doPut() {
        return new HttpRequest()
                .setMethod(HttpMethodConstants.PUT);
    }

    public static HttpRequest doPut(String url) {
        return doPut().setUrl(url);
    }

    public static HttpRequest doDelete() {
        return new HttpRequest()
                .setMethod(HttpMethodConstants.DELETE);
    }

    public static HttpRequest doDelete(String url) {
        return doDelete().setUrl(url);
    }

    public HttpRequest form() {
        this.addHeader(HttpHeaderConstants.ContentType, ContentTypeConstants.Form);
        this.setRequestBodyHandler(new HttpFormUrlEncodedRequestBodyHandler());
        return this;
    }

    public HttpRequest json() {
        this.addHeader(HttpHeaderConstants.ContentType, ContentTypeConstants.Json);
        this.setRequestBodyHandler(new HttpJsonRequestBodyHandler());
        return this;
    }

    public HttpRequest json(IJsonSerializer processor) {
        this.addHeader(HttpHeaderConstants.ContentType, ContentTypeConstants.Json);
        this.setRequestBodyHandler(new HttpJsonRequestBodyHandler(processor));
        return this;
    }

    public HttpResponse send() throws IOException {
        return send(new HttpUrlConnectProcessor());
    }

    public HttpResponse send(IHttpProcessor processor) throws IOException {
        return processor.doHttp(this);
    }

    public HttpRequest addFile(File file) throws FileNotFoundException {
        if (files == null) {
            files = new ArrayList<>();
        }
        MultipartFile item = new MultipartFile(file);
        files.add(item);
        return this;
    }

    public HttpRequest addFile(String name, File file) throws FileNotFoundException {
        if (files == null) {
            files = new ArrayList<>();
        }
        MultipartFile item = new MultipartFile(name, file);
        files.add(item);
        return this;
    }

    public HttpRequest addFile(MultipartFile file) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(file);
        return this;
    }

    public HttpRequest addHeader(String key, Object value) {
        if (header == null) {
            header = new HttpHeaders();
        }
        header.add(key, value);
        return this;
    }

    public HttpRequest addAllHeader(Map<String, ?> map) {
        if (header == null) {
            header = new HttpHeaders();
        }
        header.addAll(map);
        return this;
    }

    public HttpRequest applyHeader(Consumer<HttpHeaders> consumer) {
        if (header == null) {
            header = new HttpHeaders();
        }
        header.apply(consumer);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public HttpRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    public Object getParams() {
        return params;
    }

    public HttpRequest setParams(Object params) {
        this.params = params;
        return this;
    }

    public Object getData() {
        return data;
    }

    public HttpRequest setData(Object data) {
        this.data = data;
        return this;
    }

    public HttpHeaders getHeader() {
        return header;
    }

    public HttpRequest setHeader(Map<String, ?> header) {
        this.header = new HttpHeaders();
        this.header.addAll(header);
        return this;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public HttpRequest setFiles(List<MultipartFile> files) {
        this.files = files;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public HttpRequest setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public HttpRequest setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public HttpRequest setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public boolean isAllowRedirect() {
        return allowRedirect;
    }

    public HttpRequest setAllowRedirect(boolean allowRedirect) {
        this.allowRedirect = allowRedirect;
        return this;
    }

    public boolean isCloudAcceptByteArray() {
        return cloudAcceptByteArray;
    }

    public HttpRequest setCloudAcceptByteArray(boolean cloudAcceptByteArray) {
        this.cloudAcceptByteArray = cloudAcceptByteArray;
        return this;
    }

    public IHttpRequestBodyHandler getRequestBodyHandler() {
        return requestBodyHandler;
    }

    public HttpRequest setRequestBodyHandler(IHttpRequestBodyHandler requestBodyHandler) {
        this.requestBodyHandler = requestBodyHandler;
        return this;
    }
}
