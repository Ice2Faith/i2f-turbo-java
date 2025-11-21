package i2f.springboot.ops.common;


import com.antherd.smcrypto.sm2.Keypair;
import com.antherd.smcrypto.sm2.Sm2;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Ice2Faith
 * @date 2025/11/2 12:35
 * @desc
 */
@Data
@NoArgsConstructor
@Component
public class OpsSecureHelper {
    @Autowired
    protected ObjectMapper objectMapper;

    public OpsSecureKeyPair deserializeKeyPair(String cert) throws Exception {
        String json = new String(Base64.getDecoder().decode(cert), StandardCharsets.UTF_8);
        return objectMapper.readValue(json, OpsSecureKeyPair.class);
    }

    public String serializeKeyPair(OpsSecureKeyPair keyPair) throws Exception {
        String json = objectMapper.writeValueAsString(keyPair);
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    public OpsSecureCertPair generateCertPair() throws Exception {
        Keypair serverPair = Sm2.generateKeyPairHex();
        Keypair clientPair = Sm2.generateKeyPairHex();
        clientPair = serverPair;
        OpsSecureKeyPair serverKeyPair = new OpsSecureKeyPair(clientPair.getPublicKey(), serverPair.getPrivateKey());
        OpsSecureKeyPair clientKeyPair = new OpsSecureKeyPair(serverKeyPair.getPublicKey(), clientPair.getPrivateKey());
        String serverCert = serializeKeyPair(serverKeyPair);
        String clientCert = serializeKeyPair(clientKeyPair);
        return new OpsSecureCertPair(serverCert, clientCert);
    }
}
