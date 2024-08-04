package i2f.springboot.swl.spring;


import i2f.swl.annotation.SecureParams;
import i2f.swl.core.SwlTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ice2Faith
 * @date 2022/6/30 11:34
 * @desc
 */
@AutoConfigureAfter(SwlSpringAutoConfiguration.class)
@ConditionalOnBean(SwlTransfer.class)
@ConditionalOnExpression("${i2f.swl.api.enable:true}")
@RestController
@RequestMapping("swl")
public class SwlSpringController {

    @Autowired
    private SwlTransfer swlTransfer;

    @SecureParams(in = false, out = false)
    @PostMapping("key")
    public String key() {
        return swlTransfer.getSelfSwapKey();
    }

    @SecureParams(in = false, out = false)
    @PostMapping("swapKey")
    public String swapKey(@RequestBody String swapKey) throws Exception {
        swlTransfer.acceptOtherSwapKey(swapKey);
        return key();
    }
}
