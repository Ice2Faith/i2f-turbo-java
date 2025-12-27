package i2f.springboot.ops.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.app.data.service.AppServiceInstanceDto;
import i2f.springboot.ops.common.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/8 17:55
 * @desc
 */
@ConditionalOnClass(DiscoveryClient.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/app")
public class AppCloudOpsController {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HostIdHelper hostIdHelper;

    public void assertHostId(AppOperationDto req) {
        if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
            throw new OpsException("request not equals require hostId");
        }
    }

    @PostMapping("/service-instances")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> serviceInstances(@RequestBody OpsSecureDto reqDto,
                                                          HttpServletRequest request) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            List<AppServiceInstanceDto> resp = new ArrayList<>();
            DiscoveryClient client = applicationContext.getBean(DiscoveryClient.class);
            List<String> services = client.getServices();
            for (String service : services) {
                List<ServiceInstance> instances = client.getInstances(service);
                for (ServiceInstance instance : instances) {
                    resp.add(AppServiceInstanceDto.of(instance));
                }
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }


}
