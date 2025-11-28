package i2f.springboot.ops.host.controller;

import i2f.os.OsUtil;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.springboot.ops.host.data.HostFileItemDto;
import i2f.springboot.ops.host.data.HostOperateDto;
import i2f.springboot.ops.util.HumanUtil;
import i2f.web.servlet.ServletFileUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/11/4 21:53
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/host")
public class HostOpsController implements IOpsProvider {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private HostIdHelper hostIdHelper;

    @Override
    public List<OpsHomeMenuDto> getMenus() {
        return Collections.singletonList(new OpsHomeMenuDto()
                .title("Host")
                .subTitle("连接/操作Host主机命令/文件")
                .icon("el-icon-monitor")
                .href("./host/index.html")
        );
    }

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("./index.html").forward(request, response);
    }

    @PostMapping("/hostId")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> hostId(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            String hostIp = hostIdHelper.getHostId();
            return transfer.success(hostIp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    public void assertHostId(HostOperateDto req) {
        if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
            throw new OpsException("request not equals require hostId");
        }
    }

    @PostMapping("/workdir")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> workdir(@RequestBody OpsSecureDto reqDto,
                                                 HttpServletRequest request) throws Exception {
        try {
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            HostOperateDto resp = new HostOperateDto();
            File file = new File(".");
            file = new File(file.getAbsolutePath());
            resp.setWorkdir(file.getAbsolutePath());
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
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            String workdir = req.getWorkdir();
            File dir = new File(workdir);
            List<HostFileItemDto> resp = new ArrayList<>();
            List<HostFileItemDto> dirList = new ArrayList<>();
            List<HostFileItemDto> fileList = new ArrayList<>();
            if (dir.exists()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
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
                File parentFile = dir.getParentFile();
                item.setPath(parentFile != null ? parentFile.getAbsolutePath() : "/");
                item.setSize(0);
                item.setType("dir");
                resp.add(item);
            }

            if (dir != null) {
                HostFileItemDto item = new HostFileItemDto();
                item.setName(".");
                item.setPath(new File(dir.getAbsolutePath()).getAbsolutePath());
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
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            String path = req.getPath();
            File file = new File(path);
            deleteFile(file);
            return transfer.success(true);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    public void deleteFile(File file) throws Exception {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File item : files) {
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
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            String path = req.getPath();
            File file = new File(path);
            boolean resp = file.mkdirs();
            return transfer.success(resp);
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
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            String path = req.getPath();
            File file = new File(path);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    hostIdHelper.proxyHostIdDownload(req, req.getHostId(), request, proxyResp -> {
                        InputStream is = proxyResp.getBody();
                        ServletFileUtil.responseAsFileAttachment(is, true, file.getName(), null, !req.isInline(), response);
                    });
                    return;
                }
            }
            assertHostId(req);
            ServletFileUtil.responseFileAttachment(file, null, !req.isInline(), response);
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
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            String path = req.getPath();
            File file = new File(path);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    hostIdHelper.proxyHostIdDownload(req, req.getHostId(), request, proxyResp -> {
                        InputStream is = proxyResp.getBody();
                        ServletFileUtil.responseAsFileAttachment(is, true, file.getName(), null, !req.isInline(), response);
                    });
                    return;
                }
            }
            assertHostId(req);

            int lineCount = req.getLineCount();
            if (lineCount <= 0) {
                lineCount = 100;
            }

            int count = 0;
            LinkedList<String> lines = new LinkedList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            String path = req.getPath();
            File file = new File(path);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    hostIdHelper.proxyHostIdDownload(req, req.getHostId(), request, proxyResp -> {
                        InputStream is = proxyResp.getBody();
                        ServletFileUtil.responseAsFileAttachment(is, true, file.getName(), null, !req.isInline(), response);
                    });
                    return;
                }
            }
            assertHostId(req);

            int lineCount = req.getLineCount();
            if (lineCount <= 0) {
                lineCount = 100;
            }

            int count = 0;
            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostIdUpload(req, req.getHostId(), request, file);
                }
            }
            assertHostId(req);
            String path = req.getPath();
            File saveFile = null;
            if (path != null && !path.isEmpty()) {
                saveFile = new File(path);
            } else {
                String workdir = req.getWorkdir();
                File dir = new File(workdir);
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.isEmpty()) {
                    throw new OpsException("missing origin file name");
                }
                File originFile = new File(originalFilename);
                saveFile = new File(dir, originFile.getName());
            }
            File tmpFile = new File(saveFile.getParentFile(), "upload-" + (UUID.randomUUID().toString().replace("-", "")) + ".tmp");
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
                Files.move(Paths.get(tmpFile.getAbsolutePath()), Paths.get(saveFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
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


    @PostMapping("/cmd")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> cmd(@RequestBody OpsSecureDto reqDto,
                                             HttpServletRequest request) throws Exception {
        try {
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            String workdir = req.getWorkdir();
            String cmd = req.getCmd();
            boolean runAsFile = req.isRunAsFile();
            long waitForSeconds = req.getWaitForSeconds();
            if (waitForSeconds < 0) {
                waitForSeconds = 120;
            }
            if (waitForSeconds >= 500) {
                waitForSeconds = 500;
            }
            File bashFile = null;
            try {
                String resp = null;
                if (runAsFile) {
                    String suffix = ".sh";
                    if (OsUtil.isWindows()) {
                        suffix = ".bat";
                    }
                    bashFile = new File(workdir, "cmd-" + (UUID.randomUUID().toString().replace("-", "")) + suffix);
                    try (FileOutputStream fos = new FileOutputStream(bashFile)) {
                        fos.write(cmd.getBytes(OsUtil.getCmdCharset()));
                    }
                    String[] cmdArr = new String[]{"sh", "-c", bashFile.getName()};
                    if (OsUtil.isWindows()) {
                        cmdArr = new String[]{"cmd.exe", "/c", bashFile.getName()};
                    }
                    resp = OsUtil.execCmd(true, waitForSeconds, cmdArr, null, new File(workdir), null);
                } else {
                    resp = OsUtil.execCmd(true, waitForSeconds, cmd, null, new File(workdir), null);
                }
                return transfer.success(resp);
            } finally {
                if (bashFile != null && bashFile.exists()) {
                    bashFile.delete();
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }
}
