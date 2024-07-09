package i2f.secure;


import i2f.springboot.secure.EnableSecureConfig;
import i2f.springboot.secure.crypto.SecureProvider;
import i2f.springboot.secure.preset.SecureProviderPresets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSecureConfig
@SpringBootApplication
public class I2fSecureApplication {

    public static void main(String[] args) {
        SecureProvider.asymmetricEncryptorSupplier = SecureProviderPresets.asymmetricEncryptorSupplier_SM2;

        SecureProvider.asymmetricKeyPairSupplier = SecureProviderPresets.asymmetricKeyPairSupplier_SM2;

        SecureProvider.symmetricEncryptorSupplier = SecureProviderPresets.symmetricEncryptorSupplier_SM4;

        SecureProvider.symmetricKeySupplier = SecureProviderPresets.symmetricKeySupplier_SM4;

        SecureProvider.messageDigesterSupplier = SecureProviderPresets.messageDigesterSupplier_SM3;

        SpringApplication.run(I2fSecureApplication.class, args);
    }

}
