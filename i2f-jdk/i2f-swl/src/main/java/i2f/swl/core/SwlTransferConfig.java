package i2f.swl.core;

import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/7/10 17:34
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlTransferConfig {
    /**
     * 默认RSA交换秘钥
     */
    public static final String DEFAULT_SWAP_RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnhyhaYnyql1Vt0CFroJqDKNcQAvcHyigtjTijXwg5h4841Zlt8xl98HX7a8YZmUTIAnNeKF5tMX6OAYGl/GteUXuL8QFbj7nNSo9cDdT7ZEfK+rs+d/Pz7zxbuMI1UbUs+OFXIICLqOT+Ze1KJoBlm9r42qqvwMEYntKG8KI4bwIDAQAB";
    public static final String DEFAULT_SWAP_RSA_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKeHKFpifKqXVW3QIWugmoMo1xAC9wfKKC2NOKNfCDmHjzjVmW3zGX3wdftrxhmZRMgCc14oXm0xfo4BgaX8a15Re4vxAVuPuc1Kj1wN1PtkR8r6uz538/PvPFu4wjVRtSz44VcggIuo5P5l7UomgGWb2vjaqq/AwRie0obwojhvAgMBAAECgYBRxxzwLiZsAc6uecxupOn3/54oD0J2V30Qa2EMAu5ZYu6LkF1Cukol25VEQ4Ji6ZoVRnj5rBwYEnKUMHEQzSy9hD2noFfLEKQq5LBYeSBbXzuQfGzI0NWHp2w2lRGT+E4YmyMtcF5XJ5WB2MZLc2XGwKgkFyUNyPiuq0VfM/gDNQJBAO4/natq9B0zWIUyDqk6Q7zJmPwcLnaBEMz9Bmds2f0/Y/OlhxTgXxMSFAFZlXI/wd5CfgCS4P8/vanEHrdTfz0CQQC0ApvMRZ2IxO61X9JaPzmgyaVE8iN/daDI1zi5TsllZmr5PX0PGrfxX/pFVeZPztkipLBsgr+kxyYSUt+e79EbAkBqDL8uMml/Jf/dKi8EfP7x5frHHfRAo6rK1EYpe3Z9F95x8dhzHnyzjHSDNVEkjeTJ/mb/8mFcvQ67pqTVjcExAkAyADd+eifUAb+8qa0oXD+Jpfk+OXQax3Wt0/pxnqzaeaRlLus58tX9OgeukrmymWY+9Tf8LCVHg/nTRSnQYBTZAkASxUAldEWu7y5dZFMpEPnlw/OYM8EizKYjKa5rYXVVIRWDqSskz4k6ePwX4ajsZCeWPcs7usq2U0I83r5ChZ+w";
    /**
     * 默认SM2交换秘钥
     */
    public static final String DEFAULT_SWAP_SM2_PUBLIC_KEY = "04ba6a9a137978cbee04f0783e14f7ea43eb81e2897c04c4561183b0bac1fbcf8c3aa63a5bd443bc1c58af06b55bb434322f5dbe296f780610f2d460beb41ef29a";
    public static final String DEFAULT_SWAP_SM2_PRIVATE_KEY = "0c9425c5d12588b86a583a2f3ff115ecc903a4282dd8cedce74ef90ace50e1a1";

    /**
     * 默认非对称交换秘钥
     */
    public static final String DEFAULT_SWAP_PUBLIC_KEY = DEFAULT_SWAP_RSA_PUBLIC_KEY;
    public static final String DEFAULT_SWAP_PRIVATE_KEY = DEFAULT_SWAP_RSA_PRIVATE_KEY;

    private String cacheKeyPrefix;

    private long timestampExpireWindowSeconds = TimeUnit.MINUTES.toSeconds(30);
    private long nonceTimeoutSeconds = TimeUnit.MINUTES.toSeconds(30);

    private long certExpireSeconds = TimeUnit.MINUTES.toSeconds(30);

    private AsymKeyPair swapKeyPair=new AsymKeyPair(null,DEFAULT_SWAP_PRIVATE_KEY);

}
