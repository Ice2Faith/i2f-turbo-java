package i2f.net.http.impl;


import i2f.io.stream.StreamUtil;
import i2f.net.http.HttpUtil;
import i2f.net.http.consts.ContentTypeConstants;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.net.http.interfaces.IHttpRequestBodyHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:05
 * @desc
 */
public class HttpUrlConnectProcessor implements IHttpProcessor {
    @Override
    public HttpResponse doHttp(HttpRequest request) throws IOException {
        IHttpRequestBodyHandler<OutputStream> handler = new HttpFormUrlEncodedRequestBodyHandler();

        String contentType = request.getHeader().getFirstHeader(HttpHeaderConstants.ContentType);
        if (contentType.contains(ContentTypeConstants.Json)) {
            handler = new HttpJsonRequestBodyHandler();
        } else if (contentType.contains(ContentTypeConstants.Xml)) {
            handler = new HttpXmlRequestBodyHandler();
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
            response.setResponseCode(code);
            response.setResponseMessage(conn.getResponseMessage());

            Map<String, List<String>> headers = conn.getHeaderFields();

            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.addAll(headers);
            response.setHeader(respHeaders);

            long contentLength = conn.getContentLengthLong();
            response.setContentLength(contentLength);

            is = conn.getInputStream();
            if (request.isCloudAcceptByteArray() || contentLength > 0 && contentLength < Integer.MAX_VALUE) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream((int) contentLength);
                StreamUtil.streamCopy(is, bos, false);
                bos.close();
                byte[] bts = bos.toByteArray();
                response.setParsedContentBytes(true);
                response.setContentBytes(bts);
                ByteArrayInputStream bis = new ByteArrayInputStream(bts);
                response.setInputStream(bis);
            } else { // unknown length(-1) or gather byte array max length
                is = StreamUtil.localStream(is);
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

            if (code != 200) {
                InputStream err = conn.getErrorStream();
                ByteArrayOutputStream erros = new ByteArrayOutputStream();
                StreamUtil.streamCopy(err, erros, false);
                erros.close();
                byte[] errbts = erros.toByteArray();
                response.setErrorBytes(errbts);
                ByteArrayInputStream erris = new ByteArrayInputStream(errbts);
                response.setErrorStream(erris);
                err.close();
            }

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
        return response;
    }
}
