package i2f.spring.web.mapping;

import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.server.RequestPath;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ServletRequestPathUtils;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2022/7/4 9:13
 * @desc
 */
@Data
public class MappingUtil {

    protected RequestMappingHandlerMapping requestMappingHandlerMapping;

    private Map<String, Map<RequestMappingInfo, HandlerMethod>> fastMapping = new ConcurrentHashMap<>();

    public MappingUtil(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        initFastMapping();
    }

    public void initFastMapping() {
        Map<String, Map<RequestMappingInfo, HandlerMethod>> ret = new ConcurrentHashMap<>();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> item : handlerMethods.entrySet()) {
            RequestMappingInfo key = item.getKey();
            HandlerMethod value = item.getValue();
            PathPatternsRequestCondition patternsCondition = key.getPathPatternsCondition();
            if(patternsCondition==null){
                continue;
            }
            Set<PathPattern> pathPatterns = patternsCondition.getPatterns();
            if(pathPatterns==null){
                continue;
            }
            Set<String> patterns = pathPatterns.stream().map(e->e.getPatternString()).collect(Collectors.toSet());
            for (String patten : patterns) {
                if (!ret.containsKey(patten)) {
                    ret.put(patten, new ConcurrentHashMap<>());
                }
                ret.get(patten).put(key, value);
            }
        }

        fastMapping = new ConcurrentHashMap<>(ret.size());
        for (Map.Entry<String, Map<RequestMappingInfo, HandlerMethod>> item : ret.entrySet()) {
            String key = item.getKey();
            Map<RequestMappingInfo, HandlerMethod> value = item.getValue();
            Map<RequestMappingInfo, HandlerMethod> umap = Collections.unmodifiableMap(value);
            fastMapping.put(key, umap);
        }

    }

    public Map.Entry<RequestMappingInfo, HandlerMethod> getRequestMapping(HttpServletRequest request) {
        RequestPath requestPath = ServletRequestPathUtils.parseAndCache(request);
        String path = requestPath.value();
        Map<RequestMappingInfo, HandlerMethod> fastMap = fastMapping.get(path);
        if(fastMap!=null){
            Map.Entry<RequestMappingInfo, HandlerMethod> ret = matchRequestMapping(request, fastMap);
            if(ret!=null){
                return ret;
            }
        }
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
       return matchRequestMapping(request,handlerMethods);
    }

    public Map.Entry<RequestMappingInfo, HandlerMethod> matchRequestMapping(HttpServletRequest request,Map<RequestMappingInfo, HandlerMethod> handlerMethods){
        for (Map.Entry<RequestMappingInfo, HandlerMethod> item : handlerMethods.entrySet()) {
            RequestMappingInfo key = item.getKey();
            HandlerMethod value = item.getValue();
            try {
                if(!ServletRequestPathUtils.hasParsedRequestPath(request)){
                    ServletRequestPathUtils.parseAndCache(request);
                }
                RequestMappingInfo cond = key.getMatchingCondition(request);
                if (cond != null) {
                    HandlerMethod handler = handlerMethods.get(cond);
                    if (handler != null) {
                        return new AbstractMap.SimpleEntry<>(cond, handler);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public HandlerMethod getRequestMappingHandlerMethod(HttpServletRequest request) {
        Map.Entry<RequestMappingInfo, HandlerMethod> pair = getRequestMapping(request);
        if (pair != null) {
            return pair.getValue();
        }
        return null;
    }

    public Method getRequestMappingMethod(HttpServletRequest request) {
        HandlerMethod handlerMethod = getRequestMappingHandlerMethod(request);
        if (handlerMethod != null) {
            return handlerMethod.getMethod();
        }
        return null;
    }

}
