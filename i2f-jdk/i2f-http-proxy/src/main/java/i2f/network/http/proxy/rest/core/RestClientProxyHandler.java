package i2f.network.http.proxy.rest.core;


import i2f.annotations.core.naming.Name;
import i2f.convert.obj.ObjectConvertor;
import i2f.environment.std.IEnvironment;
import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.io.stream.StreamUtil;
import i2f.match.regex.RegexUtil;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;
import i2f.net.http.data.MultipartFile;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.net.http.interfaces.IHttpResponseExtractor;
import i2f.network.http.proxy.rest.HttpProcessorSupplier;
import i2f.network.http.proxy.rest.IHttpRequestCustomizer;
import i2f.network.http.proxy.rest.annotations.*;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.serialize.std.str.IStringObjectSerializer;
import i2f.typeof.TypeOf;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/5/18 9:52
 * @desc
 */
public class RestClientProxyHandler implements IProxyInvocationHandler {
    protected IStringObjectSerializer processor;
    protected IEnvironment environment;

    public RestClientProxyHandler(IStringObjectSerializer processor) {
        this.processor = processor;
    }

    public RestClientProxyHandler(IStringObjectSerializer processor, IEnvironment environment) {
        this.processor = processor;
        this.environment = environment;
    }

    public String replaceWithEnv(String str) {
        if (environment != null) {
            str = RegexUtil.regexFindAndReplace(str, "\\$(!)?\\{[^}]+\\}", s -> {
                String prop = s;
                boolean null2empty = false;
                if (s.startsWith("$!")) {
                    null2empty = true;
                    prop = s.substring(2, s.length() - 1);
                } else {
                    prop = s.substring(1, s.length() - 1);
                }
                prop = prop.trim();
                String defaultVal = null;
                int idx = prop.lastIndexOf(":");
                if (idx >= 0) {
                    defaultVal = prop.substring(idx + 1).trim();
                    prop = prop.substring(0, idx).trim();
                }
                String val = environment.getProperty(prop, defaultVal);
                if (val == null) {
                    if (null2empty) {
                        val = "";
                    }
                }
                return String.valueOf(val);
            });
        }
        return str;
    }

    @Override
    public Object invoke(Object ivkObj, IInvokable invokable, Object... args) throws Throwable {
        if (!(invokable instanceof JdkMethod)) {
            throw new IllegalArgumentException("un-support invocable type: " + invokable.getClass());
        }
        JdkMethod jdkMethod = (JdkMethod) invokable;
        Method method = jdkMethod.getMethod();

        HttpRequest request = new HttpRequest();
        Class<?> clazz = method.getDeclaringClass();
        RestClient client = ReflectResolver.getAnnotation(clazz, RestClient.class);
        if (client == null) {
            throw new IllegalStateException("interface not an RestClient interface");
        }
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter item = parameters[i];
            parameterNames[i] = item.getName();
            Name annName = ReflectResolver.getAnnotation(item, Name.class);
            if (annName != null) {
                String value = annName.value();
                if (value != null && !value.isEmpty()) {
                    parameterNames[i] = value;
                }
            }
        }

        IHttpProcessor http = null;
        Class<? extends HttpProcessorSupplier> httpSupplier = client.httpSupplier();
        if (httpSupplier != null && !HttpProcessorSupplier.class.equals(httpSupplier)) {
            try {
                HttpProcessorSupplier supplier = ReflectResolver.getInstance(httpSupplier);
                http = supplier.get();
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        if (http == null) {
            try {
                http = ReflectResolver.getInstance(client.http());
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        String url = joinUrlPath(client.url(), client.path());

        String path = "";
        String urlMethod = HttpMethodConstants.GET;
        RestMapping mapping = ReflectResolver.getAnnotation(method, RestMapping.class);
        if (mapping != null) {
            path = mapping.value();
            urlMethod = mapping.method();
        }
        RestGetMapping getMapping = ReflectResolver.getAnnotation(method, RestGetMapping.class);
        if (getMapping != null) {
            path = getMapping.value();
            urlMethod = HttpMethodConstants.GET;
        }
        RestPostMapping postMapping = ReflectResolver.getAnnotation(method, RestPostMapping.class);
        if (postMapping != null) {
            path = postMapping.value();
            urlMethod = HttpMethodConstants.POST;
        }
        RestPutMapping putMapping = ReflectResolver.getAnnotation(method, RestPutMapping.class);
        if (putMapping != null) {
            path = putMapping.value();
            urlMethod = HttpMethodConstants.PUT;
        }
        RestDeleteMapping deleteMapping = ReflectResolver.getAnnotation(method, RestDeleteMapping.class);
        if (deleteMapping != null) {
            path = deleteMapping.value();
            urlMethod = HttpMethodConstants.DELETE;
        }

        url = replaceWithEnv(url);
        path = replaceWithEnv(path);

        url = joinUrlPath(url, path);

        if (urlMethod == null || urlMethod.isEmpty()) {
            urlMethod = HttpMethodConstants.GET;
        }
        request.setUrl(url);
        request.setMethod(urlMethod);

        HttpHeaders headers = new HttpHeaders();

        Object params = null;

        Object datas = null;

        IHttpRequestCustomizer customizer = null;
        IHttpResponseExtractor<?> extractor = null;

        RestHeaders annHeaders = ReflectResolver.getAnnotation(method, RestHeaders.class);
        if (annHeaders != null) {
            for (RestHeader item : annHeaders.value()) {
                String name = item.name();
                if (name == null || name.isEmpty()) {
                    continue;
                }
                if (item.param() == null || item.param().isEmpty()) {
                    String val = item.value();
                    val = replaceWithEnv(val);
                    headers.add(name, val);
                } else {
                    Object paramObj = null;
                    String param = item.param();
                    if (param.matches("^\\d+$")) {
                        int paramIdx = Integer.parseInt(param);
                        if (paramIdx < args.length) {
                            paramObj = args[paramIdx];
                        }
                    } else {
                        String paramName = param;
                        for (int i = 0; i < parameters.length; i += 1) {
                            String currName = parameterNames[i];

                            if (currName.equals(paramName)) {
                                paramObj = args[i];
                                break;
                            }

                        }
                    }
                    if (item.attr() == null || item.attr().isEmpty()) {
                        headers.add(name, paramObj);
                    } else {
                        Object val = Visitor.visit(item.attr(), paramObj).get();
                        headers.add(name, val);
                    }
                }
            }
        }

        for (int i = 0; i < parameters.length; i++) {
            Parameter item = parameters[i];
            String name = parameterNames[i];
            Object val = args[i];
            if (val instanceof MultipartFile) {
                request.addFile((MultipartFile) val);
                continue;
            }
            if (val instanceof File) {
                try {
                    request.addFile((File) val);
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
                continue;
            }
            if (val instanceof IHttpRequestCustomizer) {
                customizer = (IHttpRequestCustomizer) val;
                continue;
            }
            if (val instanceof IHttpResponseExtractor) {
                extractor = (IHttpResponseExtractor<?>) val;
                continue;
            }
            RestHeader annHeader = ReflectResolver.getAnnotation(item, RestHeader.class);
            if (annHeader != null) {
                String headerName = annHeader.name();
                if (annHeader.name() != null && !annHeader.name().isEmpty()) {
                    name = annHeader.name();
                }
                if (headerName == null || headerName.isEmpty()) {
                    Map<String, Object> map = new HashMap<>();
                    if (val instanceof Map) {
                        Map<?, ?> valMap = (Map<?, ?>) val;
                        for (Map.Entry<?, ?> entry : valMap.entrySet()) {
                            Object key = entry.getKey();
                            map.put(key == null ? null : String.valueOf(key), entry.getValue());
                        }
                    } else {
                        ReflectResolver.bean2map(val, map);
                    }
                    headers.addAll(map);
                } else {
                    headers.add(name, val);
                }
                continue;
            }
            RestBody annBody = ReflectResolver.getAnnotation(item, RestBody.class);
            if (annBody != null) {
                String bodyName = annBody.value();
                if (annBody.value() != null && !annBody.value().isEmpty()) {
                    name = annBody.value();
                }
                if (datas == null) {
                    if (bodyName == null || bodyName.isEmpty()) {
                        datas = val;
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put(name, val);
                        datas = map;
                    }
                } else {
                    if (!(datas instanceof Map)) {
                        Map<String, Object> map = new HashMap<>();
                        ReflectResolver.bean2map(datas, map);
                        datas = map;
                    }
                    Map<String, Object> map = (Map<String, Object>) datas;
                    map.put(name, val);
                }
                continue;
            }
            RestPathVariable annPathVariable = ReflectResolver.getAnnotation(item, RestPathVariable.class);
            if (annPathVariable != null) {
                if (annPathVariable.value() != null && !annPathVariable.value().isEmpty()) {
                    name = annPathVariable.value();
                }
                String requestUrl = request.getUrl();
                requestUrl = requestUrl.replace("{" + name + "}", String.valueOf(val));
                request.setUrl(requestUrl);
                continue;
            }
            RestParam annParam = ReflectResolver.getAnnotation(item, RestParam.class);
            if (annParam != null) {
                if (annParam.value() != null && !annParam.value().isEmpty()) {
                    name = annParam.value();
                }
            }
            if (params == null) {
                if (name == null || name.isEmpty()) {
                    params = val;
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put(name, val);
                    params = map;
                }
            } else {
                if (!(params instanceof Map)) {
                    Map<String, Object> map = new HashMap<>();
                    ReflectResolver.bean2map(params, map);
                    params = map;
                }
                Map<String, Object> map = (Map<String, Object>) params;
                map.put(name, val);
            }
        }


        request.setHeader(headers);

        request.setParams(params);

        request.setData(datas);

        Class<?> retType = method.getReturnType();
        Object ret = null;
        try {

            if (customizer != null) {
                customizer.customizer(request);
            }

            if (extractor == null) {
                ret = http.http(request, response -> {
                    if (TypeOf.typeOf(retType, HttpResponse.class)) {
                        return response;
                    }
                    if (TypeOf.typeOf(retType, byte[].class)) {
                        return response.getContentAsBytes();
                    }
                    if (TypeOf.typeOf(retType, InputStream.class)) {
                        return StreamUtil.localStream(response.getInputStream());
                    }
                    String content = response.getContentAsString("UTF-8");
                    if (TypeOf.typeOf(retType, String.class)) {
                        return content;
                    }

                    Object obj = ObjectConvertor.tryConvertAsType(content, retType);
                    if (!TypeOf.instanceOf(obj, retType)) {
                        obj = processor.deserialize(content, retType);
                    }
                    return obj;
                });
            } else {
                ret = http.http(request, extractor);
                ret = ObjectConvertor.tryConvertAsType(ret, retType);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        return ret;
    }

    public static String joinUrlPath(String basePath, String subPath) {
        if (subPath != null && !subPath.isEmpty()) {
            if (basePath.endsWith("/")) {
                if (subPath.startsWith("/")) {
                    basePath = basePath + subPath.substring(1);
                } else {
                    basePath = basePath + subPath;
                }
            } else {
                if (subPath.startsWith("/")) {
                    basePath = basePath + subPath;
                } else {
                    basePath = basePath + "/" + subPath;
                }
            }
        }

        return basePath;
    }
}
