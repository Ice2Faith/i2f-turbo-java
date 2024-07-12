package i2f.springboot.swl.spring;

import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.impl.SwlSha256MessageDigester;
import i2f.swl.impl.supplier.SwlAesSymmetricEncryptorSupplier;
import i2f.swl.impl.supplier.SwlRsaAsymmetricEncryptorSupplier;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.ISwlObfuscator;
import i2f.swl.std.supplier.ISwlAsymmetricEncryptorSupplier;
import i2f.swl.std.supplier.ISwlSymmetricEncryptorSupplier;
import i2f.web.swl.filter.SwlWebConfig;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2024/7/10 17:29
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.swl.web")
public class SwlWebConfigProperties extends SwlWebConfig {
    protected Class<? extends ISwlAsymmetricEncryptorSupplier> asymAlgoClass = SwlRsaAsymmetricEncryptorSupplier.class;
    protected Class<? extends ISwlSymmetricEncryptorSupplier> symmAlgoClass = SwlAesSymmetricEncryptorSupplier.class;
    protected Class<? extends ISwlMessageDigester> digestAlgoClass = SwlSha256MessageDigester.class;
    protected Class<? extends ISwlObfuscator> obfuscateAlgoClass = SwlBase64Obfuscator.class;
}
