package i2f.springboot.swl.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/10 15:36
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlWebConfig {
    protected SwlCtrl defaultCtrl = new SwlCtrl(true, true);
    protected String headerName = "swlh";
    protected String remoteAsymSignHeaderName = "swlras";
    protected String currentAsymKeyHeaderName = "swlcak";
    protected String realContentTypeHeaderName = "swlct";
    protected String parameterName = "swlp";
    protected String responseCharset = "UTF-8";
    protected List<String> whiteListIn;
    protected List<String> whiteListOut;
}
