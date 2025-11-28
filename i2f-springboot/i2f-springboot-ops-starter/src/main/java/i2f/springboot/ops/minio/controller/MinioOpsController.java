package i2f.springboot.ops.minio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.filesystem.minio.MinioFileSystem;
import i2f.io.filesystem.IFile;
import i2f.io.stream.StreamUtil;
import i2f.springboot.ops.common.OpsException;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.springboot.ops.host.data.HostFileItemDto;
import i2f.springboot.ops.minio.data.MinioOperateDto;
import i2f.springboot.ops.util.HumanUtil;
import i2f.web.servlet.ServletFileUtil;
import io.minio.MinioClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/11/8 17:55
 * @desc
 */
@ConditionalOnClass(MinioClient.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/minio")
public class MinioOpsController implements IOpsProvider {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<OpsHomeMenuDto> getMenus() {
        return Collections.singletonList(new OpsHomeMenuDto()
                .title("Minio")
                .subTitle("Minio Oss 对象存储管理")
                .icon("el-icon-receiving")
                .href("./minio/index.html")
        );
    }

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("./index.html").forward(request, response);
    }

    @PostMapping("/workdir")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> workdir(@RequestBody OpsSecureDto reqDto,
                                                 HttpServletRequest request) throws Exception {
        try {
            MinioOperateDto req = transfer.recv(reqDto, MinioOperateDto.class);
            MinioOperateDto resp = new MinioOperateDto();
            resp.setWorkdir("/");
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    @PostMapping("/file-list")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> fileList(@RequestBody OpsSecureDto reqDto,
                                                  HttpServletRequest request) throws Exception {
        try {
            MinioOperateDto req = transfer.recv(reqDto, MinioOperateDto.class);
            MinioFileSystem fs = new MinioFileSystem(req.getMeta());
            String workdir = req.getWorkdir();
            IFile dir = fs.getFile(workdir);
            List<HostFileItemDto> resp = new ArrayList<>();
            List<HostFileItemDto> dirList = new ArrayList<>();
            List<HostFileItemDto> fileList = new ArrayList<>();
            if (dir.isExists()) {
                List<IFile> files = dir.listFiles();
                if (files != null) {
                    for (IFile file : files) {
                        HostFileItemDto item = new HostFileItemDto();
                        item.setName(file.getName());
                        item.setPath(file.getAbsolutePath());
                        item.setSize(file.length());
                        if (file.isDirectory()) {
                            item.setType("dir");
                            dirList.add(item);
                        } else {
                            item.setType("file");
                            fileList.add(item);
                        }
                    }
                }
            }

            dirList.sort((f1, f2) -> f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase()));
            fileList.sort((f1, f2) -> f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase()));

            if (dir != null) {
                HostFileItemDto item = new HostFileItemDto();
                item.setName("..");
                String path = dir.getAbsolutePath();
                IFile parentFile = fs.getDirectory(path);
                item.setPath(parentFile != null ? parentFile.getAbsolutePath() : "/");
                item.setSize(0);
                item.setType("dir");
                resp.add(item);
            }

            if (dir != null) {
                HostFileItemDto item = new HostFileItemDto();
                item.setName(".");
                item.setPath(dir.getAbsolutePath());
                item.setSize(0);
                item.setType("dir");
                resp.add(item);
            }

            resp.addAll(dirList);
            resp.addAll(fileList);

            for (HostFileItemDto item : resp) {
                item.setSizeText(HumanUtil.humanFileSize(item.getSize()));
            }

            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }


    @PostMapping("/delete")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> delete(@RequestBody OpsSecureDto reqDto,
                                                HttpServletRequest request) throws Exception {
        try {
            MinioOperateDto req = transfer.recv(reqDto, MinioOperateDto.class);
            MinioFileSystem fs = new MinioFileSystem(req.getMeta());
            String path = req.getPath();
            IFile file = fs.getFile(path);
            deleteFile(file);
            return transfer.success(true);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    public void deleteFile(IFile file) throws Exception {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            List<IFile> files = file.listFiles();
            if (files != null) {
                for (IFile item : files) {
                    deleteFile(item);
                }
            }
        }
        file.delete();
    }


    @PostMapping("/mkdirs")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> mkdirs(@RequestBody OpsSecureDto reqDto,
                                                HttpServletRequest request) throws Exception {
        try {
            MinioOperateDto req = transfer.recv(reqDto, MinioOperateDto.class);
            MinioFileSystem fs = new MinioFileSystem(req.getMeta());
            String path = req.getPath();
            IFile file = fs.getFile(path);
            file.mkdirs();
            return transfer.success(true);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }


    @PostMapping("/download")
    public void download(@RequestBody OpsSecureDto reqDto,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        try {
            MinioOperateDto req = transfer.recv(reqDto, MinioOperateDto.class);
            MinioFileSystem fs = new MinioFileSystem(req.getMeta());
            String path = req.getPath();
            IFile file = fs.getFile(path);

            ServletFileUtil.responseAsFileAttachment(file.getInputStream(), true, file.getName(), null, !req.isInline(), response);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            PrintWriter writer = response.getWriter();
            writer.write("Internal Server Error");
            writer.flush();
            return;
        }
    }

    @PostMapping("/tail")
    public void tail(@RequestBody OpsSecureDto reqDto,
                     HttpServletRequest request,
                     HttpServletResponse response) throws Exception {
        try {
            MinioOperateDto req = transfer.recv(reqDto, MinioOperateDto.class);
            MinioFileSystem fs = new MinioFileSystem(req.getMeta());
            String path = req.getPath();
            IFile file = fs.getFile(path);

            int lineCount = req.getLineCount();
            if (lineCount <= 0) {
                lineCount = 100;
            }

            int count = 0;
            LinkedList<String> lines = new LinkedList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                    count++;
                    if (count > lineCount) {
                        lines.removeFirst();
                        count--;
                    }
                }
            }
            StringBuilder builder = new StringBuilder();
            for (String item : lines) {
                builder.append(item).append("\n");
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(builder.toString().getBytes());
            ServletFileUtil.responseAsFileAttachment(bis, true, file.getName(), null, !req.isInline(), response);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            PrintWriter writer = response.getWriter();
            writer.write("Internal Server Error");
            writer.flush();
            return;
        }
    }


    @PostMapping("/head")
    public void head(@RequestBody OpsSecureDto reqDto,
                     HttpServletRequest request,
                     HttpServletResponse response) throws Exception {
        try {
            MinioOperateDto req = transfer.recv(reqDto, MinioOperateDto.class);
            MinioFileSystem fs = new MinioFileSystem(req.getMeta());
            String path = req.getPath();
            IFile file = fs.getFile(path);

            int lineCount = req.getLineCount();
            if (lineCount <= 0) {
                lineCount = 100;
            }

            int count = 0;
            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                    count++;
                    if (count > lineCount) {
                        break;
                    }
                }
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(builder.toString().getBytes());
            ServletFileUtil.responseAsFileAttachment(bis, true, file.getName(), null, !req.isInline(), response);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            PrintWriter writer = response.getWriter();
            writer.write("Internal Server Error");
            writer.flush();
            return;
        }
    }


    @PostMapping("/upload")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> upload(MultipartFile file, OpsSecureDto reqDto,
                                                HttpServletRequest request) throws Exception {
        try {
            MinioOperateDto req = transfer.recv(reqDto, MinioOperateDto.class);
            MinioFileSystem fs = new MinioFileSystem(req.getMeta());
            String path = req.getPath();
            IFile saveFile = null;
            if (path != null && !path.isEmpty()) {
                saveFile = fs.getFile(path);
            } else {
                String workdir = req.getWorkdir();
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.isEmpty()) {
                    throw new OpsException("missing origin file name");
                }
                File originFile = new File(originalFilename);
                saveFile = fs.getFile(workdir, originFile.getName());
            }
            File tmpFile = File.createTempFile("upload-" + (UUID.randomUUID().toString().replace("-", "")), ".tmp");
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                OutputStream os = new FileOutputStream(tmpFile);
                byte[] buffer = new byte[2048];
                int len = 0;
                InputStream is = file.getInputStream();
                while ((len = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, len);
                    os.write(buffer, 0, len);
                }
                os.close();
                byte[] bytes = digest.digest();
                StringBuilder builder = new StringBuilder();
                for (byte bt : bytes) {
                    builder.append(String.format("%02x", (int) (bt & 0x0ff)));
                }
                String calcMd5 = builder.toString();
                if (!calcMd5.equalsIgnoreCase(req.getMd5())) {
                    throw new OpsException("file check sum error");
                }
                OutputStream sos = saveFile.getOutputStream();
                StreamUtil.streamCopy(new FileInputStream(tmpFile), sos, true);
                return transfer.success(true);
            } finally {
                if (tmpFile != null && tmpFile.exists()) {
                    tmpFile.delete();
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }
}
