package i2f.springboot.ops.ssh.controller;

import com.jcraft.jsch.ChannelSftp;
import i2f.extension.sftp.basic.SftpUtil;
import i2f.os.OsUtil;
import i2f.springboot.ops.common.OpsException;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.springboot.ops.host.data.HostFileItemDto;
import i2f.springboot.ops.ssh.data.SshOperateDto;
import i2f.springboot.ops.util.HumanUtil;
import i2f.web.servlet.ServletFileUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/11/13 20:57
 * @desc
 */
@ConditionalOnClass(ChannelSftp.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/ssh")
public class SshOpsController implements IOpsProvider {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Override
    public List<OpsHomeMenuDto> getMenus() {
        return Collections.singletonList(new OpsHomeMenuDto()
                .title("Ssh")
                .subTitle("连接/操作SSH主机命令/文件")
                .icon("el-icon-link")
                .href("./ssh/index.html")
        );
    }

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("./index.html").forward(request, response);
    }

    @PostMapping("/workdir")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> workdir(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            SshOperateDto req = transfer.recv(reqDto, SshOperateDto.class);
            try (SftpUtil util = new SftpUtil(req.getMeta()).login()) {
                String pwd = util.pwd();
                SshOperateDto resp = new SshOperateDto();
                resp.setWorkdir(pwd);
                return transfer.success(resp);
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/file-list")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> fileList(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            SshOperateDto req = transfer.recv(reqDto, SshOperateDto.class);
            try (SftpUtil util = new SftpUtil(req.getMeta()).login()) {
                String workdir = req.getWorkdir();
                String dir = util.realpath(workdir);
                List<HostFileItemDto> resp = new ArrayList<>();
                List<HostFileItemDto> dirList = new ArrayList<>();
                List<HostFileItemDto> fileList = new ArrayList<>();
                if (util.existDir(dir)) {
                    Vector<ChannelSftp.LsEntry> files = util.listFiles(dir);
                    if (files != null) {
                        for (ChannelSftp.LsEntry file : files) {
                            HostFileItemDto item = new HostFileItemDto();
                            item.setName(file.getFilename());
                            if (".".equals(item.getName())) {
                                continue;
                            }
                            if ("..".equals(item.getName())) {
                                continue;
                            }
                            item.setPath(dir.endsWith("/") ? dir + item.getName() : dir + "/" + item.getName());
                            item.setSize(file.getAttrs().getSize());
                            if (file.getAttrs().isDir()) {
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
                    String parentFile = dir;
                    if (parentFile.endsWith("/")) {
                        parentFile = parentFile.substring(0, parentFile.length() - 1);
                    }
                    int idx = parentFile.lastIndexOf("/");
                    if (idx >= 0) {
                        parentFile = parentFile.substring(0, idx);
                    }
                    item.setPath(parentFile.isEmpty() ? "/" : parentFile);
                    item.setSize(0);
                    item.setType("dir");
                    resp.add(item);
                }

                if (dir != null) {
                    HostFileItemDto item = new HostFileItemDto();
                    item.setName(".");
                    item.setPath(dir);
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
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> delete(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            SshOperateDto req = transfer.recv(reqDto, SshOperateDto.class);
            try (SftpUtil util = new SftpUtil(req.getMeta()).login()) {
                String path = req.getPath();
                util.recursiveDelete(path);
                return transfer.success(true);
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/mkdirs")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> mkdirs(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            SshOperateDto req = transfer.recv(reqDto, SshOperateDto.class);
            try (SftpUtil util = new SftpUtil(req.getMeta()).login()) {
                String path = req.getPath();
                util.mkdirs(path);
                return transfer.success(true);
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/download")
    public void download(@RequestBody OpsSecureDto reqDto, HttpServletResponse response) throws Exception {
        try {
            SshOperateDto req = transfer.recv(reqDto, SshOperateDto.class);
            try (SftpUtil util = new SftpUtil(req.getMeta()).login()) {
                String path = req.getPath();
                File file = new File(path);
                int idx = path.lastIndexOf("/");
                String serverPath = null;
                String serverFileName = path;
                if (idx >= 0) {
                    serverPath = path.substring(0, idx);
                    serverFileName = path.substring(idx + 1);
                }
                InputStream is = util.download(serverPath, serverFileName);
                ServletFileUtil.responseAsFileAttachment(is, true, file.getName(), null, !req.isInline(), response);
            }
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
    public void tail(@RequestBody OpsSecureDto reqDto, HttpServletResponse response) throws Exception {
        try {
            SshOperateDto req = transfer.recv(reqDto, SshOperateDto.class);
            try (SftpUtil util = new SftpUtil(req.getMeta()).login()) {
                String path = req.getPath();
                File file = new File(path);
                int idx = path.lastIndexOf("/");
                String serverPath = null;
                String serverFileName = path;
                if (idx >= 0) {
                    serverPath = path.substring(0, idx);
                    serverFileName = path.substring(idx + 1);
                }
                int lineCount = req.getLineCount();
                if (lineCount <= 0) {
                    lineCount = 100;
                }
                int count = 0;
                LinkedList<String> lines = new LinkedList<>();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(util.download(serverPath, serverFileName)))) {
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
            }
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
    public void head(@RequestBody OpsSecureDto reqDto, HttpServletResponse response) throws Exception {
        try {
            SshOperateDto req = transfer.recv(reqDto, SshOperateDto.class);
            try (SftpUtil util = new SftpUtil(req.getMeta()).login()) {
                String path = req.getPath();
                File file = new File(path);
                int idx = path.lastIndexOf("/");
                String serverPath = null;
                String serverFileName = path;
                if (idx >= 0) {
                    serverPath = path.substring(0, idx);
                    serverFileName = path.substring(idx + 1);
                }
                int lineCount = req.getLineCount();
                if (lineCount <= 0) {
                    lineCount = 100;
                }
                int count = 0;
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(util.download(serverPath, serverFileName)))) {
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
            }
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
    public OpsSecureReturn<OpsSecureDto> upload(MultipartFile file, OpsSecureDto reqDto) throws Exception {
        try {
            SshOperateDto req = transfer.recv(reqDto, SshOperateDto.class);
            try (SftpUtil util = new SftpUtil(req.getMeta()).login()) {
                String path = req.getPath();
                String serverPath = null;
                String serverFileName = path;
                if (path != null && !path.isEmpty()) {
                    int idx = path.lastIndexOf("/");
                    if (idx >= 0) {
                        serverPath = path.substring(0, idx);
                        serverFileName = path.substring(idx + 1);
                    }
                } else {
                    String workdir = req.getWorkdir();
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null || originalFilename.isEmpty()) {
                        throw new OpsException("missing origin file name");
                    }
                    File originFile = new File(originalFilename);
                    serverPath = workdir;
                    serverFileName = originFile.getName();
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
                    util.upload(serverPath, serverFileName, new FileInputStream(tmpFile));
                    return transfer.success(true);
                } finally {
                    if (tmpFile != null && tmpFile.exists()) {
                        tmpFile.delete();
                    }
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/cmd")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> cmd(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            SshOperateDto req = transfer.recv(reqDto, SshOperateDto.class);
            try (SftpUtil util = new SftpUtil(req.getMeta()).login()) {
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
                        bashFile = File.createTempFile("cmd-" + (UUID.randomUUID().toString().replace("-", "")), suffix);
                        try (FileOutputStream fos = new FileOutputStream(bashFile)) {
                            fos.write(cmd.getBytes(OsUtil.getCmdCharset()));
                        }
                        util.upload(workdir, bashFile.getName(), new FileInputStream(bashFile));
                        util.cd(workdir);
                        resp = util.exec(true, waitForSeconds * 1000, "sh -c " + bashFile.getName(), workdir, null);
                    } else {
                        resp = util.exec(true, waitForSeconds * 1000, cmd, workdir, null);
                    }
                    return transfer.success(resp);
                } finally {
                    if (bashFile != null && bashFile.exists()) {
                        bashFile.delete();
                    }
                    if (runAsFile) {
                        try {
                            util.delete(workdir, bashFile.getName());
                        } catch (Exception e) {

                        }
                    }
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

}
