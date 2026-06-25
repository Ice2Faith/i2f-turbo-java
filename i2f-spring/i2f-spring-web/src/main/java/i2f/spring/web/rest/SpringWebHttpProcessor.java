package i2f.spring.web.rest;

import i2f.io.file.FileUtil;
import i2f.io.stream.StreamUtil;
import i2f.net.http.HttpUtil;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.net.http.interfaces.IHttpRequestBodyHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/25 10:11
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringWebHttpProcessor implements IHttpProcessor {
    protected RestTemplate restTemplate;

    public SpringWebHttpProcessor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public HttpResponse doHttp(HttpRequest request) throws IOException {
        IHttpRequestBodyHandler<ClientHttpRequest> handler = new SpringWebAutoHttpRequestBodyHandler(restTemplate);

        String reqUrl = HttpUtil.generateUrl(request);

        return restTemplate.execute(reqUrl,
                HttpMethod.resolve(request.getMethod().toUpperCase()),
                new RequestCallback() {
                    @Override
                    public void doWithRequest(ClientHttpRequest req) throws IOException {
                        for (Map.Entry<String, ArrayList<String>> item : request.getHeader().entrySet()) {
                            ArrayList<String> value = item.getValue();
                            if (value == null) {
                                value = new ArrayList<>();
                                value.add(null);
                            }
                            for (String v : value) {
                                String val = v == null ? "" : v;
                                req.getHeaders().add(item.getKey(), val);
                            }
                        }

                        Object data = request.getData();
                        if (data != null) {
                            handler.writeBody(data, request, req);
                        }
                    }
                }, new ResponseExtractor<HttpResponse>() {
                    @Override
                    public HttpResponse extractData(ClientHttpResponse resp) throws IOException {
                        HttpResponse ret = new HttpResponse();
                        ret.setResponseCode(resp.getRawStatusCode());
                        ret.setResponseMessage(resp.getStatusText());

                        org.springframework.http.HttpHeaders headers = resp.getHeaders();
                        ret.setHeader(HttpHeaders.create().addAll(headers));

                        long contentLength = resp.getHeaders().getContentLength();
                        ret.setContentLength(contentLength);
                        if (request.isCloudAcceptByteArray() || contentLength > 0 && contentLength < Integer.MAX_VALUE) {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) contentLength);
                            InputStream respIs = resp.getBody();
                            StreamUtil.streamCopy(respIs, bos, true);
                            byte[] bts = bos.toByteArray();
                            ret.setParsedContentBytes(true);
                            ret.setContentBytes(bts);
                            ByteArrayInputStream bis = new ByteArrayInputStream(bts);
                            ret.setInputStream(bis);
                        } else { // unknown length(-1) or gather byte array max length
                            File pfile = FileUtil.getTempFile();
                            FileOutputStream pfos = new FileOutputStream(pfile);
                            InputStream respIs = resp.getBody();
                            StreamUtil.streamCopy(respIs, pfos, true);
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
                                ret.setParsedContentBytes(true);
                                ret.setContentBytes(bts);
                                ByteArrayInputStream bis = new ByteArrayInputStream(bts);
                                ret.setInputStream(bis);
                            } else {
                                ret.setParsedContentBytes(false);
                                ret.setInputStream(is);
                            }
                        }

                        return ret;
                    }
                });
    }
}
