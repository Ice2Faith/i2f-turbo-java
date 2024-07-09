package i2f.secure;


import i2f.springboot.secure.EnableSecureConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({
        i2f.springboot.spring.core.SpringCoreAutoConfiguration.class,
        i2f.springboot.spring.core.SpringContextHolderAutoConfiguration.class,
        i2f.springboot.spring.web.SpringWebAutoConfiguration.class,
        i2f.springboot.spring.async.SpringAsyncAutoConfiguration.class,
        i2f.springboot.spring.cors.SpringCorsAutoConfiguration.class,
        i2f.springboot.spring.schedule.SpringScheduleAutoConfiguration.class,
        i2f.springboot.spring.web.SpringObjectMapperCustomizerConfiguration.class,
        i2f.springboot.spring.web.converter.SpringJacksonMvcConfigurer.class,
        i2f.springboot.spring.web.converter.SpringJacksonMessageConverter.class,
})
//@EnableSecureConfig
@SpringBootApplication
public class I2fSecureApplication {

    public static void main(String[] args) {
//        SecureProvider.asymmetricEncryptorSupplier = SecureProviderPresets.asymmetricEncryptorSupplier_SM2;
//
//        SecureProvider.asymmetricKeyPairSupplier = SecureProviderPresets.asymmetricKeyPairSupplier_SM2;
//
//        SecureProvider.symmetricEncryptorSupplier=SecureProviderPresets.symmetricEncryptorSupplier_SM4;
//
//        SecureProvider.symmetricKeySupplier=SecureProviderPresets.symmetricKeySupplier_SM4;
//
//        SecureProvider.messageDigesterSupplier=SecureProviderPresets.messageDigesterSupplier_SM3;

        SpringApplication.run(I2fSecureApplication.class, args);
    }

}
