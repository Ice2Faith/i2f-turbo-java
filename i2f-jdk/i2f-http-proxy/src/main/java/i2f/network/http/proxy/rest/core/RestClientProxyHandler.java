package i2f.network.http.proxy.rest.core;



import i2f.annotations.core.naming.Name;
import i2f.convert.obj.ObjectConvertor;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;
import i2f.net.http.data.MultipartFile;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.network.http.proxy.rest.annotations.*;
import i2f.proxy.impl.BasicDynamicProxyHandler;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.serialize.str.IStringObjectSerializer;
import i2f.typeof.TypeOf;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/5/18 9:52
 * @desc
 */
public class RestClientProxyHandler extends BasicDynamicProxyHandler {
    protected IStringObjectSerializer processor;

    public RestClientProxyHandler(IStringObjectSerializer processor) {
        this.processor = processor;
    }

    @Override
    public Object resolve(Object context, Object ivkObj, Method method, Object... args) {
        HttpRequest request = new HttpRequest();
        Class<?> clazz = method.getDeclaringClass();
        Parameter[] parameters = method.getParameters();
        RestClient client = ReflectResolver.getAnnotation(clazz, RestClient.class);
        if (client == null) {
            throw new IllegalStateException("interface not an RestClient interface");
        }

        IHttpProcessor http=null;
        try{
            http=ReflectResolver.getInstance(client.http());
        }catch(Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }

        String url= joinUrlPath(client.url(),client.path());

        String path="";
        String urlMethod= HttpRequest.GET;
        RestMapping mapping=ReflectResolver.getAnnotation(method,RestMapping.class);
        if(mapping!=null){
            path=mapping.value();
            urlMethod=mapping.method();
        }
        RestGetMapping getMapping=ReflectResolver.getAnnotation(method,RestGetMapping.class);
        if(getMapping!=null){
            path=getMapping.value();
            urlMethod=HttpRequest.GET;
        }
        RestPostMapping postMapping=ReflectResolver.getAnnotation(method,RestPostMapping.class);
        if(postMapping!=null){
            path=postMapping.value();
            urlMethod=HttpRequest.POST;
        }
        RestPutMapping putMapping=ReflectResolver.getAnnotation(method,RestPutMapping.class);
        if(putMapping!=null){
            path=putMapping.value();
            urlMethod=HttpRequest.PUT;
        }
        RestDeleteMapping deleteMapping=ReflectResolver.getAnnotation(method,RestDeleteMapping.class);
        if(deleteMapping!=null){
            path=deleteMapping.value();
            urlMethod=HttpRequest.DELETE;
        }

        url=joinUrlPath(url,path);

        if("".equals(urlMethod)){
            urlMethod=HttpRequest.GET;
        }
        request.setUrl(url);
        request.setMethod(urlMethod);

        Map<String,Object> headers=new HashMap<>();

        Map<String,Object> params=new HashMap<>();

        int datasCount=0;
        Map<String,Object> datas=new HashMap<>();

        RestHeaders annHeaders=ReflectResolver.getAnnotation(method,RestHeaders.class);
        if(annHeaders!=null){
            for(RestHeader item : annHeaders.value()){
                String name=item.name();
                if("".equals(name)){
                    continue;
                }
                if("".equals(item.param())){
                    String val=item.value();
                    headers.put(name,val);
                }else{
                    Object paramObj=null;
                    String param=item.param();
                    if(param.matches("^\\d+$")){
                        int paramIdx=Integer.parseInt(param);
                        if(paramIdx<args.length){
                            paramObj=args[paramIdx];
                        }
                    }else{
                        String paramName=param;
                        for(int i=0;i<parameters.length;i+=1){
                            Parameter pm=parameters[i];
                            if(pm.getName().equals(paramName)){
                                paramObj=args[i];
                                break;
                            }
                            Name aname=ReflectResolver.getAnnotation(pm, Name.class);
                            if(aname!=null){
                                if(aname.value().equals(paramName)){
                                    paramObj=args[i];
                                    break;
                                }
                            }
                        }
                    }
                    if("".equals(item.attr())){
                        headers.put(name,paramObj);
                    }else{
                        Object val= Visitor.visit(item.attr(), paramObj).get();
                        headers.put(name,val);
                    }
                }
            }
        }

        for(int i=0;i<parameters.length;i++){
            Parameter item = parameters[i];
            String name= item.getName();
            Object val=args[i];
            if(val instanceof MultipartFile){
                request.addFile((MultipartFile)val);
                continue;
            }
            if(val instanceof File){
                try{
                    request.addFile((File)val);
                }catch(Exception e){
                    throw new IllegalStateException(e.getMessage(),e);
                }
                continue;
            }
            RestHeader annHeader=ReflectResolver.getAnnotation(item,RestHeader.class);
            if(annHeader!=null){
                if(!"".equals(annHeader.name())) {
                    name = annHeader.name();
                }
                headers.put(name,val);
                continue;
            }
            RestBody annBody=ReflectResolver.getAnnotation(item,RestBody.class);
            if(annBody!=null){
                if(!"".equals(annBody.value())) {
                    name = annBody.value();
                }
                datas.put(name,val);
                datasCount++;
                continue;
            }
            RestParam annParam=ReflectResolver.getAnnotation(item,RestParam.class);
            if(annParam!=null){
                if(!"".equals(annParam.value())){
                    name=annParam.value();
                }
            }
            params.put(name,val);
        }


        request.setHeader(headers);

        request.setParams(params);

        request.setData(datas);

        Class<?> retType=method.getReturnType();
        Object ret=null;
        try{

            HttpResponse response = http.doHttp(request);

            String content = response.getContentAsString("UTF-8");

            ret = ObjectConvertor.tryConvertAsType(content, retType);
            if(!TypeOf.instanceOf(ret,retType)){
                ret = processor.deserialize(content, retType);
            }
        }catch(Exception e){

            throw new IllegalStateException(e.getMessage(),e);
        }

        return ret;
    }

    public static String joinUrlPath(String basePath,String subPath){
        if(!"".equals(subPath)){
            if(basePath.endsWith("/")){
                if(subPath.startsWith("/")){
                    basePath=basePath+subPath.substring(1);
                }else{
                    basePath=basePath+subPath;
                }
            }else{
                if(subPath.startsWith("/")){
                    basePath=basePath+subPath;
                }else{
                    basePath=basePath+"/"+subPath;
                }
            }
        }

        return basePath;
    }
}
