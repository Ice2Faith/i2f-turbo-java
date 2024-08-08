package i2f.swl.core.exchanger.cert;

import i2f.swl.cert.SwlCert;
import i2f.swl.cert.SwlCertManager;
import i2f.swl.cert.SwlCertPair;
import i2f.swl.cert.impl.SwlResourceCertManager;
import i2f.swl.core.exchanger.SwlExchanger;
import i2f.swl.data.SwlData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/8/8 16:32
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlCertExchanger extends SwlExchanger {
    protected SwlCertManager certManager = new SwlResourceCertManager();

    public void createCertPair(String certId) {
        SwlCertPair pair = generateCertPair(certId);
        certManager.storePair(pair);
    }

    public SwlData send(String certId,
                        List<String> parts) {
        SwlCert cert = certManager.loadClient(certId);
        return send(cert, parts);
    }

    public SwlData send(String certId,
                        List<String> parts,
                        List<String> attaches) {
        SwlCert cert = certManager.loadClient(certId);
        return send(cert, parts, attaches);
    }

    public SwlData receive(SwlData request,
                           String certId) {
        SwlCert cert = certManager.loadClient(certId);
        return receive(request, cert);
    }

    public SwlData receive(String clientId,
                           SwlData request,
                           String certId) {
        SwlCert cert = certManager.loadClient(certId);
        return receive(clientId, request, cert);
    }


    public SwlData response(String certId,
                            List<String> parts) {
        SwlCert cert = certManager.loadServer(certId);
        return send(cert, parts);
    }

    public SwlData response(String certId,
                            List<String> parts,
                            List<String> attaches) {
        SwlCert cert = certManager.loadServer(certId);
        return send(cert, parts, attaches);
    }

    public SwlData accept(SwlData request,
                          String certId) {
        SwlCert cert = certManager.loadServer(certId);
        return receive(request, cert);
    }

    public SwlData accept(String clientId,
                          SwlData request,
                          String certId) {
        SwlCert cert = certManager.loadServer(certId);
        return receive(clientId, request, cert);
    }


}
