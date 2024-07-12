package i2f.springboot.swl.filter;

import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.impl.SwlSha256MessageDigester;
import i2f.swl.impl.supplier.SwlAesSymmetricEncryptorSupplier;
import i2f.swl.impl.supplier.SwlRsaAsymmetricEncryptorSupplier;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.ISwlObfuscator;
import i2f.swl.std.supplier.ISwlAsymmetricEncryptorSupplier;
import i2f.swl.std.supplier.ISwlSymmetricEncryptorSupplier;
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
    protected List<String> attachedHeaderNames;
    protected Class<? extends ISwlAsymmetricEncryptorSupplier> asymAlgoClass = SwlRsaAsymmetricEncryptorSupplier.class;
    protected Class<? extends ISwlSymmetricEncryptorSupplier> symmAlgoClass = SwlAesSymmetricEncryptorSupplier.class;
    protected Class<? extends ISwlMessageDigester> digestAlgoClass = SwlSha256MessageDigester.class;
    protected Class<? extends ISwlObfuscator> obfuscateAlgoClass= SwlBase64Obfuscator.class;
}
