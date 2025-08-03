package i2f.springcloud.alibaba.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.resp.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/5/29 16:13
 * @desc
 */
@ConditionalOnExpression("${i2f.springcloud.sentinel.global-exception-handler.enable:true}")
@Slf4j
@Configuration
public class DefaultSentinelBlockExceptionHandler implements UrlBlockHandler {
    @Override
    public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException e) throws IOException {
        log.warn("DefaultSentinelBlockExceptionHandler handle exception by rule:"+e.getRule());
        e.printStackTrace();

        ApiResp ret=ApiResp.error("sentinel blocked.");

        if(e instanceof FlowException){
            ret=ApiResp.error("接口被限流了");
        }else if(e instanceof DegradeException){
            ret=ApiResp.error("服务降级了");
        }else if(e instanceof ParamFlowException){
            ret=ApiResp.error("热点参数限流了");
        }else if(e instanceof SystemBlockException){
            ret=ApiResp.error("触发系统保护规则了");
        }else if(e instanceof AuthorityException){
            ret=ApiResp.error("权限规则不通过");
        }

        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(),ret);
    }
}
