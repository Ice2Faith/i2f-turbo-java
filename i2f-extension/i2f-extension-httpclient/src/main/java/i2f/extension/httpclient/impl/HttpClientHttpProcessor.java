package i2f.extension.httpclient.impl;

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
import lombok.experimental.SuperBuilder;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:23
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class HttpClientHttpProcessor implements IHttpProcessor {
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected IXmlSerializer xmlSerializer = new Xml2Serializer();

    @Override
    public <T> T http(HttpRequest request, IHttpResponseExtractor<T> extractor) throws IOException {
        IHttpRequestBodyHandler<HttpEntityEnclosingRequestBase> handler = new HttpClientFormRequestBodyHandler();

        String contentType = request.getHeader().getFirstHeader(HttpHeaderConstants.ContentType);
        if (contentType.contains(ContentTypeConstants.Json)) {
            handler = new HttpClientJsonRequestBodyHandler(jsonSerializer);
        } else if (contentType.contains(ContentTypeConstants.Xml)) {
            handler = new HttpClientXmlRequestBodyHandler(xmlSerializer);
        } else if (contentType.contains(ContentTypeConstants.Form)) {
            handler = new HttpClientFormRequestBodyHandler();
        } else if (contentType.contains(ContentTypeConstants.Multipart)) {
            handler = new HttpClientMultipartFormRequestBodyHandler();
        }

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .build();

        String reqUrl = HttpUtil.generateUrl(request);

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(request.getConnectTimeout())
                .setConnectionRequestTimeout(request.getReadTimeout())
                .setSocketTimeout(request.getReadTimeout())
                .setRedirectsEnabled(request.isAllowRedirect())
                .setCircularRedirectsAllowed(request.isAllowRedirect())
                .build();

        HttpUriRequest req = null;

        for (Map.Entry<String, ArrayList<String>> item : request.getHeader().entrySet()) {
            ArrayList<String> value = item.getValue();
            if (value == null) {
                value = new ArrayList<>();
                value.add(null);
            }
            for (String v : value) {
                String val = v == null ? "" : v;
                req.addHeader(item.getKey(), val);
            }
        }

        Object data = request.getData();
        if (data != null) {
            if (data instanceof byte[]) {
                handler = new HttpClientRawBytesRequestBodyHandler();
            } else if (data instanceof InputStream) {
                handler = new HttpClientRawInputStreamRequestBodyHandler();
            }
        }

        String method = request.getMethod();
        if (HttpMethodConstants.GET.equals(method)) {
            HttpGet httpGet = new HttpGet(reqUrl);
            req = httpGet;
            httpGet.setConfig(config);

        } else if (HttpMethodConstants.POST.equals(method)) {
            HttpPost httpPost = new HttpPost(reqUrl);
            req = httpPost;
            httpPost.setConfig(config);

            if (data != null) {
                handler.writeBody(data, request, httpPost, httpClient);
            }
        } else if (HttpMethodConstants.PUT.equals(method)) {
            HttpPut httpPut = new HttpPut(reqUrl);
            req = httpPut;
            httpPut.setConfig(config);

            if (data != null) {
                handler.writeBody(data, request, httpPut, httpClient);
            }
        } else if (HttpMethodConstants.DELETE.equals(method)) {
            HttpDelete httpDelete = new HttpDelete(reqUrl);
            req = httpDelete;
            httpDelete.setConfig(config);
        }

        CloseableHttpResponse resp = null;
        HttpResponse response = new HttpResponse();
        boolean autoCloseResource = true;
        try {
            resp = httpClient.execute(req);

            response.setStatusCode(resp.getStatusLine().getStatusCode());
            response.setStatusMessage(resp.getStatusLine().getReasonPhrase());

            HttpHeaders respHeader = new HttpHeaders();
            Header[] headers = resp.getAllHeaders();
            for (Header item : headers) {
                String name = item.getName();
                String value = item.getValue();
                respHeader.add(name, value);
            }
            response.setHeader(respHeader);

            HttpEntity entity = resp.getEntity();
            long contentLength = entity.getContentLength();
            response.setContentLength(contentLength);

            InputStream is = entity.getContent();
            response.setInputStream(is);

            T ret = extractor.extract(response);
            if (ret instanceof HttpResponse) {
                HttpResponse retResp = (HttpResponse) ret;
                retResp.setCloser(new HttpClientCloser(httpClient));
                autoCloseResource = false;
            }
            return ret;
        } catch (IOException e) {
            throw e;
        } finally {
            if (autoCloseResource) {
                if (httpClient != null) {
                    httpClient.close();
                }
            }
        }
    }

    private static class HttpClientCloser implements Closeable {
        private CloseableHttpClient httpClient;

        public HttpClientCloser(CloseableHttpClient httpClient) {
            this.httpClient = httpClient;
        }

        @Override
        public void close() throws IOException {
            if (httpClient != null) {
                httpClient.close();
                httpClient = null;
            }
        }
    }
}
