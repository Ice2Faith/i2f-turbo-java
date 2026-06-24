package i2f.extension.httpclient.impl;

import i2f.io.file.FileUtil;
import i2f.io.stream.StreamUtil;
import i2f.net.http.HttpUtil;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.net.http.interfaces.IHttpRequestBodyHandler;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:23
 * @desc
 */
public class HttpClientHttpProcessor implements IHttpProcessor {
    @Override
    public HttpResponse doHttp(HttpRequest request) throws IOException {
        IHttpRequestBodyHandler handler = request.getRequestBodyHandler();
        if (handler == null) {
            handler = new HttpClientRequestFormDataHandler();
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

        String method = request.getMethod();
        if (HttpMethodConstants.GET.equals(method)) {
            HttpGet httpGet = new HttpGet(reqUrl);
            req = httpGet;
            httpGet.setConfig(config);

        } else if (HttpMethodConstants.POST.equals(method)) {
            HttpPost httpPost = new HttpPost(reqUrl);
            req = httpPost;
            httpPost.setConfig(config);
            handler.writeBody(request.getData(), request, httpPost, httpClient);
        } else if (HttpMethodConstants.PUT.equals(method)) {
            HttpPut httpPut = new HttpPut(reqUrl);
            req = httpPut;
            httpPut.setConfig(config);
            handler.writeBody(request.getData(), request, httpPut, httpClient);
        } else if (HttpMethodConstants.DELETE.equals(method)) {
            HttpDelete httpDelete = new HttpDelete(reqUrl);
            req = httpDelete;
            httpDelete.setConfig(config);
        }

        CloseableHttpResponse resp = null;
        HttpResponse response = new HttpResponse();

        try {
            resp = httpClient.execute(req);

            response.setResponseCode(resp.getStatusLine().getStatusCode());
            response.setResponseMessage(resp.getStatusLine().getReasonPhrase());

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
            if (request.isCloudAcceptByteArray() || contentLength > 0 && contentLength < Integer.MAX_VALUE) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream((int) contentLength);
                entity.writeTo(bos);
                byte[] bts = bos.toByteArray();
                response.setParsedContentBytes(true);
                response.setContentBytes(bts);
                ByteArrayInputStream bis = new ByteArrayInputStream(bts);
                response.setInputStream(bis);
            } else { // unknown length(-1) or gather byte array max length
                File pfile = FileUtil.getTempFile();
                FileOutputStream pfos = new FileOutputStream(pfile);
                entity.writeTo(pfos);
                pfos.close();
                FileInputStream pfis = new FileInputStream(pfile);
                InputStream is = StreamUtil.localStream(pfis);
                pfis.close();
                pfile.delete();
                if (is instanceof ByteArrayInputStream) {
                    ByteArrayInputStream tbis = (ByteArrayInputStream) is;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    StreamUtil.streamCopy(tbis, bos, false);
                    bos.close();
                    byte[] bts = bos.toByteArray();
                    response.setParsedContentBytes(true);
                    response.setContentBytes(bts);
                    ByteArrayInputStream bis = new ByteArrayInputStream(bts);
                    response.setInputStream(bis);
                } else {
                    response.setParsedContentBytes(false);
                    response.setInputStream(is);
                }
            }

            return response;
        } catch (IOException e) {
            throw e;
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        }
    }
}
