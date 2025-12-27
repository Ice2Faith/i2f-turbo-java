package i2f.springboot.ops.home;

import com.jcraft.jsch.ChannelSftp;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.provider.IOpsProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/28 14:30
 */
@ConditionalOnClass(ChannelSftp.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops")
public class OpsHomeController {
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("./index.html").forward(request, response);
    }

    @RequestMapping("/menus")
    @ResponseBody
    public OpsSecureReturn<List<OpsHomeMenuDto>> menus() {
        try {
            List<OpsHomeMenuDto> resp = new ArrayList<>();
            String[] names = applicationContext.getBeanNamesForType(IOpsProvider.class);
            for (String name : names) {
                try {
                    IOpsProvider bean = applicationContext.getBean(name, IOpsProvider.class);
                    resp.addAll(bean.getMenus());
                } catch (Exception e) {

                }
            }
            resp.sort((v1, v2) -> {
                return String.CASE_INSENSITIVE_ORDER.compare(v1.getTitle(), v2.getTitle());
            });
            return OpsSecureReturn.success(resp);
        } catch (Exception e) {
            return OpsSecureReturn.error("Internal Server Error!");
        }
    }
}
