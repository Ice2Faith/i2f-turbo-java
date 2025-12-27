package i2f.web.swl.filter;

import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.swl.core.SwlTransfer;
import i2f.swl.data.SwlData;
import i2f.swl.data.SwlHeader;
import i2f.url.FormUrlEncodedEncoder;
import i2f.web.filter.OncePerHttpServletFilter;
import i2f.web.servlet.ServletContextUtil;
import i2f.web.wrapper.HttpServletRequestProxyWrapper;
import i2f.web.wrapper.HttpServletResponseProxyWrapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/7/10 15:31
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlWebFilter extends OncePerHttpServletFilter {

    protected SwlTransfer transfer;
    protected SwlWebConfig config;
    protected IJsonSerializer jsonSerializer;

    public SwlWebFilter(SwlTransfer transfer, SwlWebConfig config, IJsonSerializer jsonSerializer) {
        this.transfer = transfer;
        this.config = config;
        this.jsonSerializer = jsonSerializer;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 跳过非常规请求
        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        if ("TRACE".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        // 获取控制信息，可子类重写实现白名单等功能
        SwlWebCtrl ctrl = parseCtrl(request, response);

        // 跳过multipart请求
        String contentType = request.getContentType();
        if (contentType == null) {
            contentType = "";
        }
        contentType = contentType.toLowerCase();

        if (contentType.contains("multipart/form-data")) {
            ctrl.setIn(false);
        }

        // 获取安全头，优先从请求头获取，获取不到则从请求参数获取
        String swlh = request.getHeader(config.getHeaderName());
        if (swlh == null || swlh.isEmpty()) {
            swlh = request.getParameter(config.getHeaderName());
        }

        // 如果带有安全头，则强制为输入需要解密
        if (swlh != null && !swlh.isEmpty()) {
            ctrl.setIn(true);
        }

        // 没有输入输出控制，直接跳过
        if (!ctrl.isIn() && !ctrl.isOut()) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest nextRequest = request;
        HttpServletResponse nextResponse = response;

        // 获取客户端IP，用以客户端隔离
        String clientIp = ServletContextUtil.getIp(request);
        if (clientIp != null) {
            clientIp = clientIp.replaceAll(":", "-");
        }

        // 获取原始请求的非对称秘钥签名
        // 用于响应时使用，以配对请求
        String clientAsymSign = request.getHeader(config.getRemoteAsymSignHeaderName());
        String serverAsymSign = null;

        // 判断是否已经被解密过
        Object decrypted = request.getAttribute(SwlWebConsts.SWL_REQUEST_DECRYPT_ATTR_KEY);

        // 如果输入需要解密，则进行输入解密
        if (ctrl.isIn() && !Boolean.TRUE.equals(decrypted)) {
            try {
                HttpServletRequestProxyWrapper wrapper = new HttpServletRequestProxyWrapper(request);
                nextRequest = wrapper;
                // 获取加密后的请求参数
                String swlp = request.getParameter(config.getParameterName());

                // 获取请求体
                byte[] body = wrapper.getBodyBytes();

                // 转换请求体为字符串
                String srcText = null;
                if (body.length > 0) {
                    srcText = new String(body, request.getCharacterEncoding());
                }

                // 如果文本是引号开头，则是被转义为了JSON
                // 需要进行反序列化字符串
                if (srcText != null) {
                    srcText = srcText.trim();
                    if (srcText.startsWith("\"")) {
                        srcText = (String) jsonSerializer.deserialize(srcText, String.class);
                    }
                }

                List<String> attachedHeaders = new ArrayList<>();
                if (this.config.getAttachedHeaderNames() != null) {
                    for (String headerName : this.config.getAttachedHeaderNames()) {
                        String header = request.getHeader(headerName);
                        attachedHeaders.add(header);
                    }
                }

                // 构造请求数据
                SwlData data = new SwlData();
                data.setHeader(deserializeHeader(swlh));
                data.setParts(Arrays.asList(srcText, swlp));
                data.setAttaches(attachedHeaders);

                // 保存原始请求头
                nextRequest.setAttribute(SwlWebConsts.SWL_REQUEST_RAW_HEADER_ATTR_KEY, data.getHeader());

                // 接受数据
                SwlData receiveData = transfer.receive(clientIp, data);

                // 保存请求时的非对称公钥签名
                // 此时还没有进行receive，因此local还是客户端的值，remote是服务端的值
                clientAsymSign = receiveData.getHeader().getRemoteAsymSign();
                serverAsymSign = receiveData.getHeader().getLocalAsymSign();


                // 保存解密后的头
                nextRequest.setAttribute(SwlWebConsts.SWL_REQUEST_HEADER_ATTR_KEY, receiveData.getHeader());


                // 获取解密后的数据
                srcText = receiveData.getParts().get(0);
                swlp = receiveData.getParts().get(1);

                // 重新设置请求参数
                String replaceQueryString = null;
                Map<String, List<String>> replaceParameterMap = null;
                if (swlp != null) {
                    replaceQueryString = swlp;
                    replaceParameterMap = FormUrlEncodedEncoder.toMap(swlp);
                }

                // 重新构造请求
                if (srcText != null) {
                    body = srcText.getBytes(request.getCharacterEncoding());
                    wrapper = new HttpServletRequestProxyWrapper(request, body);
                }

                // 覆盖请求参数
                if (replaceQueryString != null) {
                    wrapper.setQueryString(replaceQueryString);
                }
                if (replaceParameterMap != null) {
                    wrapper.setParameterMap(replaceParameterMap);
                }

                // 覆盖真实请求内容类型
                String realContentType = request.getHeader(config.getRealContentTypeHeaderName());
                if (realContentType != null && !realContentType.isEmpty()) {
                    wrapper.setAttachHeader("Content-Type", realContentType);
                    wrapper.setContentType(realContentType);
                }

                wrapper.setContentLength(body.length);
                wrapper.setAttachHeader("Content-Length", String.valueOf(body.length));

                // 替换请求为包装请求
                nextRequest = wrapper;
            } catch (Throwable e) {
                // 异常直接进行捕获，方便在后续的流程中进行处理
                nextRequest.setAttribute(SwlWebConsts.SWL_REQUEST_DECRYPT_EXCEPTION_ATTR_KEY, e);
                boolean needResponse = onException(request, response, e);
                if (needResponse) {
                    return;
                }
            }

            // 设置为已被解密
            nextRequest.setAttribute(SwlWebConsts.SWL_REQUEST_DECRYPT_ATTR_KEY, true);

        }

        // 如果需要加密输出，则需要对输出进行包装
        if (ctrl.isOut()) {
            nextResponse = new HttpServletResponseProxyWrapper(response);
        }

        // 进行下游处理
        chain.doFilter(nextRequest, nextResponse);

        // 设置暴露响应头，以支持前端获取这些值
        applyExposeHeader(nextResponse);

        if (!ctrl.isOut()) {
            return;
        }

        // 如果已被加密，则不再处理
        Object encrypted = request.getAttribute(SwlWebConsts.SWL_RESPONSE_ENCRYPT_ATTR_KEY);
        if (Boolean.TRUE.equals(encrypted)) {
            return;
        }

        request.setAttribute(SwlWebConsts.SWL_RESPONSE_ENCRYPT_ATTR_KEY, true);

        HttpServletResponseProxyWrapper responseWrapper = (HttpServletResponseProxyWrapper) nextResponse;

        byte[] responseBody = responseWrapper.getBodyBytes();

        String responseCharset = config.getResponseCharset();
        if (responseCharset == null || responseCharset.isEmpty()) {
            responseCharset = responseWrapper.getCharacterEncoding();
        }
        if (responseCharset == null || responseCharset.isEmpty()) {
            responseCharset = "UTF-8";
        }
        responseWrapper.setCharacterEncoding(responseCharset);

        // 处理直接下载型接口，推荐还是使用白名单
        // 小文件可以在这里进行自动处理
        // 如果是大文件下载，这里可是内存包装
        // 使用白名单，避免OOM
        boolean specificResponseBody = false;
        Collection<String> headerNames = responseWrapper.getHeaderNames();
        for (String item : headerNames) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            String str = item.toLowerCase().trim();
            if ("content-disposition".equals(str)) {
                String header = responseWrapper.getHeader(item);
                if (header != null && !header.isEmpty()) {
                    specificResponseBody = true;
                    break;
                }
            }
        }

        if (specificResponseBody) {
            // 响应数据
            response.setContentType(responseWrapper.getContentType());
            response.setCharacterEncoding(responseWrapper.getCharacterEncoding());
            OutputStream os = response.getOutputStream();
            os.write(responseBody);
            os.flush();
            return;
        }


        String responseText = new String(responseBody, responseCharset);

        Object responseString = request.getAttribute(SwlWebConsts.SWL_STRING_RESPONSE);
        if (Boolean.TRUE.equals(responseString)) {
            responseText = jsonSerializer.serialize(responseText);
        }


        List<String> attachedHeaders = new ArrayList<>();
        if (this.config.getAttachedHeaderNames() != null) {
            for (String headerName : this.config.getAttachedHeaderNames()) {
                String header = response.getHeader(headerName);
                attachedHeaders.add(header);
            }
        }

        List<String> parts = new ArrayList<>();
        parts.add(responseText);

        SwlData responseData = transfer.response(clientAsymSign, parts);
        String responseSwlh = serializeHeader(responseData.getHeader());
        response.setHeader(config.getHeaderName(), responseSwlh);
        response.setHeader(config.getRemoteAsymSignHeaderName(), responseData.getContext().getSelfAsymSign());

        responseText = responseData.getParts().get(0);
        responseBody = responseText.getBytes("UTF-8");

        String responseContentType = responseWrapper.getContentType();
        response.setHeader(config.getRealContentTypeHeaderName(), responseContentType);

        String selfPublicKey = transfer.getSelfPublicKey();
        String selfAsymSign = transfer.calcKeySign(selfPublicKey);
        if (serverAsymSign != null) {
            if (!selfAsymSign.equalsIgnoreCase(serverAsymSign)) {
                response.setHeader(config.getCurrentAsymKeyHeaderName(), transfer.obfuscateEncode(selfPublicKey));
            }
        }
        response.setContentType("text/plain");
        response.setContentLengthLong(responseBody.length);

        // 响应数据
        response.setContentType(responseWrapper.getContentType());
        response.setCharacterEncoding(responseWrapper.getCharacterEncoding());
        OutputStream os = response.getOutputStream();
        os.write(responseBody);
        os.flush();
    }

    public boolean onException(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        e.printStackTrace();
        return config.isFilterResponseException();
    }


    public SwlHeader deserializeHeader(String str) {
        str = transfer.obfuscateDecode(str);
        str = CharsetStringByteCodec.UTF8.encode(Base64StringByteCodec.INSTANCE.decode(str));
        SwlHeader ret = FormUrlEncodedEncoder.ofFormBean(str, SwlHeader.class);
        return ret;
    }

    public String serializeHeader(SwlHeader header) {
        String ret = FormUrlEncodedEncoder.toForm(header);
        ret = Base64StringByteCodec.INSTANCE.encode(CharsetStringByteCodec.UTF8.decode(ret));
        ret = transfer.obfuscateEncode(ret);
        return ret;
    }

    public void applyExposeHeader(HttpServletResponse response) {
        // 将随机Asym加密模糊之后的Symm秘钥放入响应头，并设置可访问权限
        Collection<String> oldHeaders = response.getHeaders(SwlWebConsts.ACCESS_CONTROL_EXPOSE_HEADERS);
        Set<String> headers = new LinkedHashSet<>();
        for (String header : oldHeaders) {
            String[] arr = header.split(",");
            for (String item : arr) {
                String str = item.trim();
                if (!str.isEmpty()) {
                    headers.add(str);
                }
            }
        }
        headers.add(config.getHeaderName());
        headers.add(config.getRemoteAsymSignHeaderName());
        headers.add(config.getCurrentAsymKeyHeaderName());
        headers.add(config.getRealContentTypeHeaderName());
        response.setHeader(SwlWebConsts.ACCESS_CONTROL_EXPOSE_HEADERS, toCommaDelimitedString(headers));
    }


    public String toCommaDelimitedString(Collection<String> headerValues) {
        StringJoiner joiner = new StringJoiner(", ");
        Iterator<String> iterator = headerValues.iterator();

        while (iterator.hasNext()) {
            String val = iterator.next();
            if (val != null && !"".equals(val)) {
                joiner.add(val);
            }
        }

        return joiner.toString();
    }


    public SwlWebCtrl parseCtrl(HttpServletRequest request, HttpServletResponse response) {
        return config.getDefaultCtrl();
    }


}
