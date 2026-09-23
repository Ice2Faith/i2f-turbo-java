package i2f.net.http.data;

import i2f.mutator.BaseMutator;
import i2f.mutator.Mutator;
import i2f.net.http.consts.ContentTypeConstants;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.impl.HttpUrlConnectProcessor;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.net.http.interfaces.IHttpResponseExtractor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2022/3/24 8:33
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpRequest implements BaseMutator<HttpRequest> {

    protected String url;
    protected String method = HttpMethodConstants.GET;
    protected Object params;
    protected Object data;
    protected HttpHeaders header = new HttpHeaders();
    protected List<MultipartFile> files;

    protected int connectTimeout = (int) TimeUnit.SECONDS.toMillis(30);
    protected int readTimeout = (int) TimeUnit.MINUTES.toMillis(5);
    protected String charset = "UTF-8";
    protected boolean allowRedirect = true;
    protected boolean cloudAcceptByteArray = false;

    public static Mutator<HttpRequest> doGet() {
        return new HttpRequest().toMutator()
                .set(u -> u::setMethod, HttpMethodConstants.GET);
    }

    public static Mutator<HttpRequest> doGet(String url) {
        return doGet()
                .set(u -> u::setUrl, url);
    }

    public static Mutator<HttpRequest> doGet(String url, Object params) {
        return doGet(url)
                .set(u -> u::setParams, params);
    }

    public static Mutator<HttpRequest> doPost() {
        return new HttpRequest().toMutator()
                .set(u -> u::setMethod, HttpMethodConstants.POST);
    }

    public static Mutator<HttpRequest> doPost(String url) {
        return doPost()
                .set(u -> u::setUrl, url);
    }

    public static Mutator<HttpRequest> doPut() {
        return new HttpRequest().toMutator()
                .set(u -> u::setMethod, HttpMethodConstants.PUT);
    }

    public static Mutator<HttpRequest> doPut(String url) {
        return doPut()
                .set(u -> u::setUrl, url);
    }

    public static Mutator<HttpRequest> doDelete() {
        return new HttpRequest().toMutator()
                .set(u -> u::setMethod, HttpMethodConstants.DELETE);
    }

    public static Mutator<HttpRequest> doDelete(String url) {
        return doDelete()
                .set(u -> u::setUrl, url);
    }

    public HttpRequest form() {
        this.addHeader(HttpHeaderConstants.ContentType, ContentTypeConstants.Form);
        return this;
    }

    public HttpRequest json() {
        this.addHeader(HttpHeaderConstants.ContentType, ContentTypeConstants.Json);
        return this;
    }

    public HttpRequest multipart() {
        this.addHeader(HttpHeaderConstants.ContentType, ContentTypeConstants.Multipart);
        return this;
    }

    public HttpRequest xml() {
        this.addHeader(HttpHeaderConstants.ContentType, ContentTypeConstants.Xml);
        return this;
    }

    public HttpResponse send() throws IOException {
        return send(new HttpUrlConnectProcessor());
    }

    public <T> T send(IHttpResponseExtractor<T> extractor) throws IOException {
        return send(new HttpUrlConnectProcessor(), extractor);
    }

    public HttpResponse send(IHttpProcessor processor) throws IOException {
        return processor.http(this);
    }

    public <T> T send(IHttpProcessor processor, IHttpResponseExtractor<T> extractor) throws IOException {
        return processor.http(this, extractor);
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

    public HttpRequest setHeader(Map<String, ?> header) {
        this.header = new HttpHeaders();
        this.header.addAll(header);
        return this;
    }


}
