package i2f.springboot.swl.spring;

import i2f.reflect.ReflectResolver;
import i2f.serialize.str.json.IJsonSerializer;
import i2f.spring.matcher.MatcherUtil;
import i2f.spring.web.mapping.MappingUtil;
import i2f.swl.annotation.SecureParams;
import i2f.swl.core.SwlTransfer;
import i2f.web.swl.filter.SwlWebConfig;
import i2f.web.swl.filter.SwlWebConsts;
import i2f.web.swl.filter.SwlWebCtrl;
import i2f.web.swl.filter.SwlWebFilter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/10 17:11
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
public class SwlSpringWebFilter extends SwlWebFilter {
    protected MappingUtil mappingUtil;

    public SwlSpringWebFilter(SwlTransfer transfer, SwlWebConfig config, IJsonSerializer jsonSerializer, MappingUtil mappingUtil) {
        super(transfer, config, jsonSerializer);
        this.mappingUtil = mappingUtil;
    }

    @Override
    public boolean onException(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        e.printStackTrace();
        return false;
    }

    @Override
    public SwlWebCtrl parseCtrl(HttpServletRequest request, HttpServletResponse response) {
        Method method = mappingUtil.getRequestMappingMethod(request);
        // 特殊标记返回值为String的方法
        Class<?> returnType = method.getReturnType();
        if (String.class.isAssignableFrom(returnType)) {
            request.setAttribute(SwlWebConsts.SWL_STRING_RESPONSE,true);
            log.debug("mapping return string type.");
        }
        return parseCtrl(request, method, config);
    }

    public static SwlWebCtrl parseCtrl(HttpServletRequest request, Method method, SwlWebConfig config) {
        SwlWebCtrl defaultCtrl = config.getDefaultCtrl();

        if (method != null) {
            SecureParams ann = ReflectResolver.getMemberAnnotation(method, SecureParams.class);
            if (ann != null) {
                return new SwlWebCtrl(ann.in(), ann.out());
            }
        }

        if (request instanceof MultipartHttpServletRequest) {
            return new SwlWebCtrl(false, defaultCtrl.isOut());
        }

        String path = getTrimContextPathRequestUri(request);

        Boolean in = null;
        Boolean out = null;
        List<String> whiteListIn = config.getWhiteListIn();
        if (whiteListIn != null) {
            if (MatcherUtil.antUrlMatchedAny(path, whiteListIn)) {
                in = false;
            }
        }
        List<String> whiteListOut = config.getWhiteListOut();
        if (whiteListOut != null) {
            if (MatcherUtil.antUrlMatchedAny(path, whiteListOut)) {
                out = false;
            }
        }

        return new SwlWebCtrl(in == null ? defaultCtrl.isIn() : in,
                out == null ? defaultCtrl.isOut() : out);
    }


    public static String getTrimContextPathRequestUri(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.isEmpty(contextPath)) {
            contextPath = "/";
        }
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        if (!contextPath.endsWith("/")) {
            contextPath = contextPath + "/";
        }
        if (!requestUrl.startsWith("/")) {
            requestUrl = "/" + requestUrl;
        }
        if (requestUrl.startsWith(contextPath)) {
            requestUrl = requestUrl.substring(contextPath.length());
        } else {
            String tmp = requestUrl + "/";
            if (contextPath.equals(tmp)) {
                requestUrl = "/";
            }
        }
        if (!requestUrl.startsWith("/")) {
            requestUrl = "/" + requestUrl;
        }
        return requestUrl;
    }
}
