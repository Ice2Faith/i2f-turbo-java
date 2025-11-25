package i2f.springboot.ops.minio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.filesystem.minio.MinioFileSystem;
import i2f.extension.groovy.GroovyScript;
import i2f.io.filesystem.IFile;
import i2f.springboot.ops.app.data.AppKeyValueItemDto;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.app.data.AppServiceInstanceDto;
import i2f.springboot.ops.app.data.AppThreadInfoDto;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.host.data.HostFileItemDto;
import i2f.springboot.ops.host.data.HostOperateDto;
import i2f.springboot.ops.minio.data.MinioOperateDto;
import i2f.springboot.ops.util.HumanUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/11/8 17:55
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/minio")
public class MinioOpsController {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;


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
                IFile parentFile=fs.getDirectory(path);
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
            return transfer.error(e.getMessage());
        }
    }


}
