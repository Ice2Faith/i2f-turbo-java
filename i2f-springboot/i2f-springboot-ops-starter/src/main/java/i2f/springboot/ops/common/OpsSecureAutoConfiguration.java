package i2f.springboot.ops.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2025/11/2 12:32
 * @desc
 */
@Configuration
@EnableConfigurationProperties(OpsSecureProperties.class)
@Data
@NoArgsConstructor
public class OpsSecureAutoConfiguration {
    @Autowired
    protected OpsSecureProperties opsSecureProperties;

    @Autowired
    protected OpsSecureHelper opsSecureHelper;

    @Bean
    public OpsSecureKeyPair opsSecureKeyPair() throws Exception {
        String cert = opsSecureProperties.getCert();
        if(cert==null){
            cert="";
        }
        cert=cert.trim();
        if(cert.isEmpty()){
            OpsSecureCertPair certPair = opsSecureHelper.generateCertPair();
            System.out.println("serverCert:\n"+certPair.getServerCert());
            System.out.println("clientCert:\n"+certPair.getClientCert());
            opsSecureProperties.setCert(certPair.getServerCert());
            test();
        }
        return opsSecureHelper.deserializeKeyPair(opsSecureProperties.getCert());
    }

    public void test() throws Exception {
        OpsSecureCertPair certPair = opsSecureHelper.generateCertPair();
        OpsSecureTransfer serverTransfer=new OpsSecureTransfer();
        serverTransfer.setOpsSecureKeyPair(opsSecureHelper.deserializeKeyPair(certPair.serverCert));
        serverTransfer.setObjectMapper(new ObjectMapper());
        OpsSecureDto dto = serverTransfer.send("1234");
        OpsSecureTransfer clientTransfer=new OpsSecureTransfer();
        clientTransfer.setOpsSecureKeyPair(opsSecureHelper.deserializeKeyPair(certPair.clientCert));
        clientTransfer.setObjectMapper(new ObjectMapper());
        String resp = clientTransfer.recv(dto,String.class);
    }
}
