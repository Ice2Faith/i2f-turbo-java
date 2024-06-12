package i2f.web.servlet;


import i2f.serialize.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.serialize.str.xml.IXmlSerializer;
import i2f.serialize.str.xml.impl.Xml2Serializer;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * @author ltb
 * @date 2022/3/26 17:32
 * @desc
 */
public class ServletResponseUtil {
    public static volatile IJsonSerializer jsonSerializer = new Json2Serializer();
    public static volatile IXmlSerializer xmlSerializer = new Xml2Serializer();

    public static void respJson(HttpServletResponse response, Object obj) throws Exception {
        respJson(response, obj, jsonSerializer);
    }

    public static void respJson(HttpServletResponse response, Object obj, IJsonSerializer processor) throws Exception {
        String json = processor.serialize(obj);
        respJsonString(response, json);
    }

    public static void respJsonString(HttpServletResponse response, String content) throws Exception {
        respJsonString(response, content, 200);
    }

    public static void respJsonString(HttpServletResponse response, String content, int status) throws Exception {
        respString(response, content, status, "application/json", "UTF-8");
    }

    public static void respXml(HttpServletResponse response, Object obj) throws Exception {
        respXml(response, obj, xmlSerializer);
    }

    public static void respXml(HttpServletResponse response, Object obj, IXmlSerializer processor) throws Exception {
        String json = processor.serialize(obj);
        respXmlString(response, json);
    }

    public static void respXmlString(HttpServletResponse response, String content) throws Exception {
        respXmlString(response, content, 200);
    }

    public static void respXmlString(HttpServletResponse response, String content, int status) throws Exception {
        respString(response, content, status, "text/xml", "UTF-8");
    }

    public static void respString(HttpServletResponse response, String content, int status, String contentType, String charset) throws Exception {
        response.setStatus(status);
        response.setContentType(contentType);
        response.setCharacterEncoding(charset);
        response.getWriter().print(content);
    }

    public static void supportCors(HttpServletResponse resp) {
        Collection<String> headers = resp.getHeaders("Access-Control-Allow-Origin");
        if (headers.size() <= 0) {
            resp.setHeader("Access-Control-Allow-Origin", "*");
        }
        resp.setHeader("Access-Control-Allow-Methods", "*");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers",
                "X-Requested-With, Content-Type, Authorization, Accept, Origin, User-Agent, Content-Range, Content-Disposition, Content-Description");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
