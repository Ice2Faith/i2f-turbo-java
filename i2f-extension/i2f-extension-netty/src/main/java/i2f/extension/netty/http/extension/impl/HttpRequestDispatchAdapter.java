package i2f.extension.netty.http.extension.impl;


import i2f.extension.netty.http.annotation.*;
import i2f.extension.netty.http.extension.AbstractHttpRequestHandler;
import i2f.extension.netty.http.extension.HttpWebRequest;
import i2f.extension.netty.http.extension.HttpWebResponse;
import i2f.reflect.ReflectResolver;
import i2f.resources.ResourcesLoader;
import i2f.serialize.std.str.json.IJsonSerializer;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2021/8/18
 */
public class HttpRequestDispatchAdapter extends AbstractHttpRequestHandler {
    protected IJsonSerializer jsonSerializer;

    public HttpRequestDispatchAdapter(IJsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
    }

    public volatile Map<String, Method> pathMapping = new ConcurrentHashMap<String, Method>();

    public void addMapping(String path, Method method) {
        pathMapping.put(path, method);
    }

    public void addMapping(String path, Class<?> clazz, String methodName) {
        Method method = ReflectResolver.matchMethodByParamTypes(clazz, methodName, new Class[]{HttpWebRequest.class, HttpWebResponse.class});
        addMapping(path, method);
    }

    /**
     * @param path
     * @param fullClassNameThatMethodName 格式：全限定类名:方法名 例如：com.test.Controller:test
     */
    public void addMapping(String path, String fullClassNameThatMethodName) {
        String[] arr = fullClassNameThatMethodName.split(":", 2);
        if (arr.length == 2) {
            String className = arr[0];
            String methodName = arr[1];
            try {
                Class clazz = Class.forName(className);
                addMapping(path, clazz, methodName);
            } catch (ClassNotFoundException e) {

            }
        }
    }

    public void addMappingByScanPackage(String... basePackages) throws IOException {
        Map<String, Class<?>> map = ResourcesLoader.scanClassNamesBasePackages((name, clazz) -> {
            NettyController ann = ReflectResolver.getAnnotation(clazz, NettyController.class);
            if (ann != null) {
                return true;
            }
            return false;
        }, basePackages);
        for (Map.Entry<String, Class<?>> entry : map.entrySet()) {
            addMappingByScanClass(entry.getValue());
        }

    }

    public void addMappingByScanClass(Class clazz) {
        NettyController controller = ReflectResolver.getAnnotation(clazz, NettyController.class);
        Map<Method, Class<?>> map = ReflectResolver.getMethods(clazz, (method) -> {
            NettyRequestMapping mapping = ReflectResolver.getAnnotation(method, NettyRequestMapping.class);
            if (mapping != null) {
                return true;
            }
            return false;
        });
        for (Map.Entry<Method, Class<?>> entry : map.entrySet()) {
            NettyRequestMapping mapping = ReflectResolver.getAnnotation(entry.getKey(), NettyRequestMapping.class);

            String path = getRequestPathMapping(entry.getKey(), mapping, controller);
            addMapping(path, entry.getKey());
        }
    }

    public static String getRequestPathMapping(Method method, NettyRequestMapping mann, NettyController cann) {
        String cpath = cann.value();
        String mpath = mann.value();
        if ("".equals(mpath)) {
            mpath = method.getName();
        }
        if (cpath.startsWith("/")) {
            cpath = cpath.substring(1);
        }
        if (cpath.endsWith("/")) {
            cpath = cpath.substring(0, cpath.length() - 1);
        }
        if (mpath.startsWith("/")) {
            mpath = mpath.substring(1);
        }
        if (mpath.endsWith("/")) {
            mpath = mpath.substring(0, cpath.length() - 1);
        }
        return "/" + cpath + "/" + mpath;
    }

    private static boolean checkSupportMethod(HttpMethod method, Method exec) {
        NettyRequestMapping mapping = exec.getAnnotation(NettyRequestMapping.class);
        if (mapping != null) {
            NettyHttpMethod[] methods = mapping.methods();
            boolean isIn = false;
            if (methods.length > 0) {
                for (NettyHttpMethod item : methods) {
                    if (item.getMethod() == method) {
                        isIn = true;
                        break;
                    }
                }
            }
            return isIn;
        }
        return true;
    }

    @Override
    public Map<String, Object> deserializeJsonAsMap(String json) {
        Object obj = jsonSerializer.deserialize(json, Map.class);
        return (Map<String, Object>) obj;
    }

    @Override
    public void doRequest(HttpWebRequest request, HttpWebResponse response) {
        String path = request.getUri().getPath();
        HttpMethod method = request.getMethod();

        if (pathMapping.containsKey(path)) {
            try {
                Method exec = pathMapping.get(path);
                boolean isSupport = checkSupportMethod(method, exec);
                if (!isSupport) {
                    response.setStatus(HttpResponseStatus.METHOD_NOT_ALLOWED);
                    return;
                }
                Object[] args = inflateMethodArgs(request, response, exec);
                Object obj = exec.getDeclaringClass().getConstructor().newInstance();
                Object ret = exec.invoke(obj, args);
                if (!exec.getReturnType().equals(void.class)) {
                    String retStr = jsonSerializer.serialize(ret);
                    response.ok(retStr, "UTF-8", "application/json;charset=UTF-8");
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            response.setStatus(HttpResponseStatus.NOT_FOUND);
        }
    }

    protected Object[] inflateMethodArgs(HttpWebRequest request, HttpWebResponse response, Method method) throws ParseException {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i += 1) {
            args[i] = null;
            Parameter item = parameters[i];
            if (HttpWebRequest.class.equals(item.getType())) {
                args[i] = request;
                continue;
            }
            if (HttpWebResponse.class.equals(item.getType())) {
                args[i] = response;
                continue;
            }

            Map.Entry<String, Object> entry = findParam(item, request);
            args[i] = entry.getValue();
        }
        return args;
    }

    private Map.Entry<String, Object> findParam(Parameter param, HttpWebRequest request) throws ParseException {
        String argName = param.getName();
        boolean ignoreCase = false;
        NettyParam pann = param.getAnnotation(NettyParam.class);
        if (pann != null) {
            argName = pann.value();
            ignoreCase = pann.ignoreCase();
        }
        if (ignoreCase) {
            argName = argName.toLowerCase();
        }
        List<Object> params = new ArrayList<>();


        List<Object> urlParams = new ArrayList<>();
        for (Map.Entry<String, Object> item : request.getUrlParams().entrySet()) {
            String ikey = item.getKey();
            if (ignoreCase) {
                ikey = ikey.toLowerCase();
            }
            if (ikey.equals(argName)) {
                urlParams.add(item.getValue());
            }
        }
        List<Object> bodyParams = new ArrayList<>();
        for (Map.Entry<String, Object> item : request.getBodyParams().entrySet()) {
            String ikey = item.getKey();
            if (ignoreCase) {
                ikey = ikey.toLowerCase();
            }
            if (ikey.equals(argName)) {
                bodyParams.add(item.getValue());
            }
        }
        List<Object> headerParams = new ArrayList<>();
        for (Map.Entry<String, String> item : request.getHeaders().entries()) {
            String ikey = item.getKey();
            if (ignoreCase) {
                ikey = ikey.toLowerCase();
            }
            if (ikey.equals(argName)) {
                headerParams.add(item.getValue());
            }
        }

        if (pann != null) {
            NettyParamSource[] sources = pann.source();
            for (NettyParamSource item : sources) {
                if (item == NettyParamSource.URL) {
                    params.addAll(urlParams);
                }
                if (item == NettyParamSource.BODY) {
                    params.addAll(bodyParams);
                }
                if (item == NettyParamSource.HEADER) {
                    params.addAll(headerParams);
                }
            }
        } else {
            params.addAll(urlParams);
            params.addAll(bodyParams);
        }

        Object retVal = null;
        String retKey = argName;
        Class type = param.getType();
        if (type.isArray()) {

        } else {
            if (!params.isEmpty()) {
                Object obj = params.get(0);
                if (String.class.equals(type)) {
                    retVal = String.valueOf(obj);
                } else if (Integer.class.equals(type) || int.class.equals(type)
                        || Long.class.equals(type) || long.class.equals(type)
                        || Short.class.equals(type) || short.class.equals(type)
                        || BigInteger.class.equals(type)) {
                    BigInteger val = new BigInteger(String.valueOf(obj));
                    if (BigInteger.class.equals(type)) {
                        retVal = val;
                    } else if (Integer.class.equals(type) || int.class.equals(type)) {
                        retVal = val.intValue();
                    } else if (Long.class.equals(type) || long.class.equals(type)) {
                        retVal = val.longValue();
                    } else if (Short.class.equals(type) || short.class.equals(type)) {
                        retVal = val.shortValue();
                    } else {
                        throw new ParseException("bad type of:" + type.getName() + " when parse:" + obj, 0);
                    }
                } else if (Float.class.equals(type) || float.class.equals(type)
                        || Double.class.equals(type) || double.class.equals(type)
                        || BigDecimal.class.equals(type)) {
                    BigDecimal val = new BigDecimal(String.valueOf(obj));
                    if (BigDecimal.class.equals(type)) {
                        retVal = val;
                    } else if (Float.class.equals(type) || float.class.equals(type)) {
                        retVal = val.floatValue();
                    } else if (Double.class.equals(type) || double.class.equals(type)) {
                        retVal = val.doubleValue();
                    } else {
                        throw new ParseException("bad type of:" + type.getName() + " when parse:" + obj, 0);
                    }
                } else if (Date.class.equals(type)) {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (pann != null) {
                        fmt = new SimpleDateFormat(pann.dateFmt());
                    }
                    retVal = fmt.parse(String.valueOf(obj));
                } else {
                    throw new ParseException("bad type of:" + type.getName() + " when parse:" + obj, 0);
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(retKey, retVal);
    }
}
