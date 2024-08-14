package i2f.swl.cert;

import i2f.swl.cert.data.SwlCert;
import i2f.swl.cert.data.SwlCertPair;
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

    public SwlData sendByCertId(String certId,
                                List<String> parts) {
        SwlCert cert = certManager.loadClient(certId);
        return sendByCert(cert, parts);
    }

    public SwlData sendByCertId(String certId,
                                List<String> parts,
                                List<String> attaches) {
        SwlCert cert = certManager.loadClient(certId);
        return sendByCert(cert, parts, attaches);
    }

    public SwlData receiveByCertId(SwlData request,
                                   String certId) {
        SwlCert cert = certManager.loadClient(certId);
        return receiveByCert(request, cert);
    }

    public SwlData receiveByCertId(SwlData request,
                                   String certId,
                                   String clientId) {
        SwlCert cert = certManager.loadClient(certId);
        return receiveByCert( request, cert,clientId);
    }


    public SwlData responseByCertId(String certId,
                                    List<String> parts) {
        SwlCert cert = certManager.loadServer(certId);
        return sendByCert(cert, parts);
    }

    public SwlData responseByCertId(String certId,
                                    List<String> parts,
                                    List<String> attaches) {
        SwlCert cert = certManager.loadServer(certId);
        return sendByCert(cert, parts, attaches);
    }

    public SwlData acceptByCertId(SwlData request,
                                  String certId) {
        SwlCert cert = certManager.loadServer(certId);
        return receiveByCert(request, cert);
    }

    public SwlData acceptByCertId(SwlData request,
                                  String certId,
                                  String clientId) {
        SwlCert cert = certManager.loadServer(certId);
        return receiveByCert( request, cert,clientId);
    }


}
