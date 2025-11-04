package i2f.springboot.ops.host.controller;

import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.host.data.HostFileItemDto;
import i2f.springboot.ops.host.data.HostOperateDto;
import i2f.springboot.ops.redis.data.RedisOperateDto;
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

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
public class HostOpsController {
    @Autowired
    protected OpsSecureTransfer transfer;

    @PostMapping("/workdir")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> workdir(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            HostOperateDto resp=new HostOperateDto();
            File file = new File(".");
            file=new File(file.getAbsolutePath());
            resp.setWorkdir(file.getAbsolutePath());
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/file-list")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> fileList(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            String workdir = req.getWorkdir();
            File dir = new File(workdir);
            List<HostFileItemDto> resp=new ArrayList<>();
            if(dir.exists()){
                File[] files = dir.listFiles();
                if(files!=null){
                    for(File file : files){
                        HostFileItemDto item=new HostFileItemDto();
                        item.setName(file.getName());
                        item.setPath(file.getAbsolutePath());
                        item.setSize(file.length());
                        if(file.isDirectory()){
                            item.setType("dir");
                        }else{
                            item.setType("file");
                        }
                        resp.add(item);
                    }
                }
            }

            resp.sort((f1,f2)->f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase()));

            if(dir!=null){
                HostFileItemDto item=new HostFileItemDto();
                item.setName(".");
                item.setPath(new File(dir.getAbsolutePath()).getAbsolutePath());
                item.setSize(0);
                item.setType("dir");
                resp.add(0,item);
            }
            if(dir!=null){
                HostFileItemDto item=new HostFileItemDto();
                item.setName("..");
                File parentFile = dir.getParentFile();
                item.setPath(parentFile!=null?parentFile.getAbsolutePath():"/");
                item.setSize(0);
                item.setType("dir");
                resp.add(0,item);
            }

            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> delete(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            String path = req.getPath();
            File file = new File(path);
            boolean resp = file.delete();
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/mkdirs")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> mkdirs(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            String path = req.getPath();
            File file = new File(path);
            boolean resp = file.mkdirs();
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/download")
    public void download(@RequestBody OpsSecureDto reqDto, HttpServletResponse response) throws Exception {
        try {
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            String path = req.getPath();
            File file = new File(path);
            ServletFileUtil.responseFileAttachment(file,null,!req.isInline(),response);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            PrintWriter writer = response.getWriter();
            writer.write("Internal Server Error");
            writer.flush();
            return;
        }
    }

}
