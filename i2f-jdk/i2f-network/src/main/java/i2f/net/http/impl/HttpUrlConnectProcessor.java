package i2f.net.http.impl;


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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:05
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class HttpUrlConnectProcessor implements IHttpProcessor {
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected IXmlSerializer xmlSerializer = new Xml2Serializer();

    @Override
    public <T> T http(HttpRequest request, IHttpResponseExtractor<T> extractor) throws IOException {
        IHttpRequestBodyHandler<OutputStream> handler = new HttpFormUrlEncodedRequestBodyHandler();

        String contentType = request.getHeader().getFirstHeader(HttpHeaderConstants.ContentType);
        if (contentType.contains(ContentTypeConstants.Json)) {
            handler = new HttpJsonRequestBodyHandler(jsonSerializer);
        } else if (contentType.contains(ContentTypeConstants.Xml)) {
            handler = new HttpXmlRequestBodyHandler(xmlSerializer);
        } else if (contentType.contains(ContentTypeConstants.Form)) {
            handler = new HttpFormUrlEncodedRequestBodyHandler();
        } else if (contentType.contains(ContentTypeConstants.Multipart)) {
            handler = new HttpMultipartFormDataRequestBodyHandler();
        }

        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        HttpResponse response = new HttpResponse();
        try {
            String urlString = HttpUtil.generateUrl(request);
            URL url = new URL(urlString);

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(request.getConnectTimeout());
            conn.setReadTimeout(request.getReadTimeout());
            conn.setInstanceFollowRedirects(request.isAllowRedirect());

            HttpUtil.setHttpHeaders(conn, request);

            String method = request.getMethod();
            conn.setRequestMethod(method);
            method = method.trim().toUpperCase();
            conn.setDoInput(true);

            Object data = request.getData();
            if (data != null) {
                if (HttpMethodConstants.POST.equals(method)
                        || HttpMethodConstants.PUT.equals(method)) {
                    conn.setDoOutput(true);

                    os = conn.getOutputStream();
                    if (data instanceof byte[]) {
                        handler = new HttpRawBytesRequestBodyHandler();
                    } else if (data instanceof InputStream) {
                        handler = new HttpRawInputStreamRequestBodyHandler();
                    }
                    handler.writeBody(request.getData(), request, os, conn);
                }
            }

            conn.connect();

            int code = conn.getResponseCode();
            response.setStatusCode(code);
            response.setStatusMessage(conn.getResponseMessage());

            Map<String, List<String>> headers = conn.getHeaderFields();

            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.addAll(headers);
            response.setHeader(respHeaders);

            long contentLength = conn.getContentLengthLong();
            response.setContentLength(contentLength);

            is = conn.getInputStream();
            response.setInputStream(is);
            if (code != 200) {
                InputStream err = conn.getErrorStream();
                response.setErrorStream(err);
            }

            return extractor.extract(response);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
