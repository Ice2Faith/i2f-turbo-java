package i2f.springboot.swl.filter;

import i2f.extension.swl.impl.sm.antherd.SwlAntherdSm2AsymmetricEncryptor;
import i2f.extension.swl.impl.sm.antherd.SwlAntherdSm3MessageDigester;
import i2f.extension.swl.impl.sm.antherd.SwlAntherdSm4SymmetricEncryptor;
import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.std.ISwlAsymmetricEncryptor;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.ISwlObfuscator;
import i2f.swl.std.ISwlSymmetricEncryptor;
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
    protected boolean filterResponseException=false;
    protected List<String> whiteListIn;
    protected List<String> whiteListOut;
    protected Class<? extends ISwlAsymmetricEncryptor> asymAlgoClass= SwlAntherdSm2AsymmetricEncryptor.class;
    protected Class<? extends ISwlSymmetricEncryptor> symmAlgoClass= SwlAntherdSm4SymmetricEncryptor.class;
    protected Class<? extends ISwlMessageDigester> digestAlgoClass= SwlAntherdSm3MessageDigester.class;
    protected Class<? extends ISwlObfuscator> obfuscateAlgoClass= SwlBase64Obfuscator.class;
}
