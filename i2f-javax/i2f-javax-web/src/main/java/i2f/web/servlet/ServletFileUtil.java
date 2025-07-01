package i2f.web.servlet;


import i2f.io.file.FileUtil;
import i2f.io.file.core.FileMime;
import i2f.io.stream.StreamUtil;
import i2f.text.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

/**
 * @author Ice2Faith
 * @date 2022/3/26 17:34
 * @desc
 */
public class ServletFileUtil {
    public static void downloadFileRangeSupport(HttpServletRequest request,
                                                HttpServletResponse response,
                                                File responseFile
    ) throws IOException {
        downloadFileRangeSupport(request, response, responseFile, null, null, true, true);
    }

    public static void downloadFileRangeSupport(HttpServletRequest request,
                                                HttpServletResponse response,
                                                File responseFile,
                                                String virtualFileName
    ) throws IOException {
        downloadFileRangeSupport(request, response, responseFile, virtualFileName, null, true, true);
    }

    public static void downloadFileRangeSupport(HttpServletRequest request,
                                                HttpServletResponse response,
                                                File responseFile,
                                                String virtualFileName,
                                                String mimeType
    ) throws IOException {
        downloadFileRangeSupport(request, response, responseFile, virtualFileName, mimeType, true, true);
    }

    public static void downloadFileRangeSupport(HttpServletRequest request,
                                                HttpServletResponse response,
                                                File responseFile,
                                                String virtualFileName,
                                                String mimeType,
                                                boolean allowCache
    ) throws IOException {
        downloadFileRangeSupport(request, response, responseFile, virtualFileName, mimeType, allowCache, true);
    }

    /**
     * 支持断点续传的下载方式
     *
     * @param request         需要从请求中解析得到断点的请求范围
     * @param response        响应数据
     * @param responseFile    要响应回去的真实文件
     * @param virtualFileName 响应回去的文件名
     * @param mimeType        可为null的MIME类型，用于指定Content-Type,为null时将根据virtualFileName的后缀自动识别映射
     * @param allowCache      是否允许客户端缓存
     * @param useAttachment   是否指定为附件下载形式，否则就直接打开
     * @throws IOException
     */
    public static void downloadFileRangeSupport(HttpServletRequest request,
                                                HttpServletResponse response,
                                                File responseFile,
                                                String virtualFileName,
                                                String mimeType,
                                                boolean allowCache,
                                                boolean useAttachment
    ) throws IOException {
//        response.reset();
        if (virtualFileName == null || virtualFileName.isEmpty()) {
            virtualFileName = responseFile.getName();
        }
        //接受范围下载
        response.setHeader("Accept-Ranges", "bytes");
        //设置MIME,特别指定则用指定的，否则用默认匹配的
        if (null != mimeType) {
            response.setContentType(mimeType + ";charset=UTF-8");
        } else {
            response.setContentType(FileMime.getMimeType(virtualFileName) + ";charset=UTF-8");
        }

        //编码文件名
        String urlEncodedFileName = java.net.URLEncoder.encode(virtualFileName, "UTF-8");
        //设置要求浏览器的接受形式：直接打开 inline 附件下载 attachment
        if (useAttachment) {
            response.setHeader("Content-Disposition", "attachment; filename=" + urlEncodedFileName); // 设置文件名称
        } else {
            response.setHeader("Content-Disposition", "inline; filename=" + urlEncodedFileName);
        }
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        //禁用缓存
        if (!allowCache) {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "No-cache");
            response.setHeader("Expires", Long.toString(0L));
        }

        //获取文件大小
        long sumLen = responseFile.length();

        long responseLen = sumLen;
        //解析范围下载请求的范围
        String rangeHead = request.getHeader("Range");
        long rangeBegin = 0L, rangeEnd = sumLen - 1;
        boolean hasRange = false;
        if (!StringUtils.isEmpty(rangeHead)) {
            rangeHead = rangeHead.trim();
            if (rangeHead.contains("-")) {
                String[] rangeArr = rangeHead.replace("bytes=", "").split("-");
                if (rangeArr.length > 0) {
                    rangeBegin = Long.parseLong(rangeArr[0]);
                }
                if (rangeArr.length > 1) {
                    rangeEnd = Long.parseLong(rangeArr[1]);
                }
                hasRange = true;
            }
        }

        //规范范围参数
        if (rangeBegin < 0) {
            rangeBegin = 0;
        }

        if (rangeEnd < 0) {
            rangeEnd = sumLen - 1L;
        }

        if (rangeBegin >= sumLen) {
            rangeBegin = 0;
        }

        if (rangeEnd >= sumLen) {
            rangeEnd = sumLen - 1L;
        }

        responseLen = rangeEnd - rangeBegin + 1L;

        if (rangeBegin + responseLen >= sumLen) {
            responseLen = sumLen - rangeBegin;
        }

        if (hasRange) {
            response.setStatus(206);
            response.setHeader("Accept-Ranges", String.format("bytes %d-%d/%d", rangeBegin, rangeEnd, responseLen));
        } else {
            response.setStatus(200);
        }

        response.setHeader("Content-Length", Long.toString(responseLen));
        response.setHeader("Content-Range", String.format("bytes %d-%d/%d", rangeBegin, rangeEnd, responseLen));

        InputStream is = new BufferedInputStream(new FileInputStream(responseFile));
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        long transLen = StreamUtil.streamCopyRange(is, os, rangeBegin, responseLen, false);

        is.close();
        os.close();
    }


    public static void responseNotFileFound(String fileName, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(404);
        response.getWriter().write("<script>alert('404,not found file:" + fileName + "');</script>");
    }

    public static void responseAsFileAttachment(InputStream is, String virtualFileName, HttpServletResponse response) throws IOException {
        responseAsFileAttachment(is, true, virtualFileName, null, true, response);
    }

    public static void responseAsFileAttachment(InputStream is, String virtualFileName, String mimeType, HttpServletResponse response) throws IOException {
        responseAsFileAttachment(is, true, virtualFileName, mimeType, true, response);
    }

    public static void responseAsFileAttachment(InputStream is, boolean closeStream, String virtualFileName, String mimeType, HttpServletResponse response) throws IOException {
        responseAsFileAttachment(is, closeStream, virtualFileName, mimeType, true, response);
    }

    /**
     * 普通文件直接下载方式
     *
     * @param is              输入流
     * @param closeStream     是否关闭输入流
     * @param virtualFileName 虚拟文件名
     * @param mimeType        响应类型
     * @param useAttachment   是否附件下载，如果为false就是内联模式
     * @param response        HTTP响应
     * @throws IOException
     */
    public static void responseAsFileAttachment(InputStream is, boolean closeStream, String virtualFileName, String mimeType, boolean useAttachment, HttpServletResponse response) throws IOException {
//        response.reset();
        if (null != mimeType) {
            response.setContentType(mimeType + ";charset=UTF-8");
        } else {
            response.setContentType(FileMime.getMimeType(virtualFileName) + ";charset=UTF-8");
        }
        if (useAttachment) {
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(virtualFileName, "UTF-8")); // 设置文件名称
        } else {
            response.setHeader("Content-Disposition", "inline; filename=" + java.net.URLEncoder.encode(virtualFileName, "UTF-8")); // 设置文件名称
        }
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        OutputStream os = response.getOutputStream();
        StreamUtil.streamCopy(is, os, false);

        os.close();
        if (closeStream) {
            is.close();
        }
    }

    public static void responseFileAttachment(File filePath, HttpServletResponse response) throws IOException {
        responseFileAttachment(filePath, null, true, response);
    }

    public static void responseFileAttachment(File filePath, String mimeType, HttpServletResponse response) throws IOException {
        responseFileAttachment(filePath, mimeType, true, response);
    }

    public static void responseFileAttachment(File filePath, String mimeType, boolean useAttachment, HttpServletResponse response) throws IOException {
        String fileName = filePath.getName();
        InputStream is = new FileInputStream(filePath);
        responseAsFileAttachment(is, true, fileName, mimeType, useAttachment, response);
    }

    public static File saveAsFile2ContextPath(InputStream is, String fileName, HttpSession session, String relativePath) throws IOException {
        File saveFile = ServletContextUtil.getContextPath(session.getServletContext(), relativePath);
        FileUtil.useDir(saveFile);
        saveFile = new File(saveFile, fileName);
        FileUtil.save(is, saveFile);
        return saveFile;
    }
}
