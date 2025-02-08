package i2f.web.swl.filter;

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
    protected SwlWebCtrl defaultCtrl = new SwlWebCtrl(true, true);
    protected String headerName = "swlh";
    protected String remoteAsymSignHeaderName = "swlras";
    protected String currentAsymKeyHeaderName = "swlcak";
    protected String realContentTypeHeaderName = "swlct";
    protected String parameterName = "swlp";
    protected String responseCharset = "UTF-8";
    protected boolean filterResponseException = false;
    protected List<String> whiteListIn;
    protected List<String> whiteListOut;
    protected List<String> attachedHeaderNames;
}
