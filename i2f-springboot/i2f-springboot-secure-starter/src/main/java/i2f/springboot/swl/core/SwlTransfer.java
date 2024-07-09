package i2f.springboot.swl.core;

import i2f.clock.SystemClock;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.springboot.swl.data.SecureHeader;
import i2f.springboot.swl.std.ISwlAsymmetricEncryptor;
import i2f.springboot.swl.std.ISwlMessageDigester;
import i2f.springboot.swl.std.ISwlStore;
import i2f.springboot.swl.std.ISwlSymmetricEncryptor;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/7/9 20:22
 * @desc
 */
public class SwlTransfer {
    private Supplier<ISwlAsymmetricEncryptor> asymmetricEncryptorSupplier;
    private Supplier<ISwlSymmetricEncryptor> symmetricEncryptorSupplier;
    private ISwlMessageDigester messageDigester;
    private ISwlStore store;
    private long nonceWindowSeconds =30;
    private long nonceTimeoutSeconds = 30;
    private SecureRandom random=new SecureRandom();

    public SwlData clientSend(String serverPublicKey,List<String> parts){
        SwlData ret=new SwlData();
        ret.setParts(new ArrayList<>());
        ret.setHeader(new SecureHeader());

        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorSupplier.get();
        ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorSupplier.get();

        AsymKeyPair selfKeyPair = store.getSelfKeyPair();

        String clientAsymSign=messageDigester.digest(selfKeyPair.getPublicKey());
        ret.getHeader().setClientAsymSign(clientAsymSign);

        String serverAsymSign=messageDigester.digest(serverPublicKey);
        ret.getHeader().setServerAsymSign(serverAsymSign);

        long timestamp = SystemClock.currentTimeMillis() / 1000;
        long seq=random.nextInt(0x7fff);
        String nonce=Long.toString(timestamp,16)+"-"+Long.toString(seq,16);
        ret.getHeader().setNonce(nonce);

        String key=symmetricEncryptor.generateKey();

        asymmetricEncryptor.setPublicKey(serverPublicKey);
        String randomKey=asymmetricEncryptor.encrypt(key);
        ret.getHeader().setRandomKey(randomKey);

        symmetricEncryptor.setKey(key);
        StringBuilder builder=new StringBuilder();
        for (String part : parts) {
            String data=null;
            if(part!=null){
                data = symmetricEncryptor.encrypt(part);
            }
            if(data!=null){
                builder.append(data);
            }
            ret.getParts().add(data);
        }

        String data=builder.toString();

        String sign=messageDigester.digest(data+randomKey+nonce+clientAsymSign+serverAsymSign);
        ret.getHeader().setSign(sign);

        asymmetricEncryptor.setPrivateKey(selfKeyPair.getPrivateKey());
        String digital=asymmetricEncryptor.sign(sign);
        ret.getHeader().setDigital(digital);

        return ret;
    }

    public SwlData serverReceive(String clientId,SwlData request){
        SwlData ret=new SwlData();
        ret.setParts(new ArrayList<>());
        ret.setHeader(request.getHeader());

        long currentTimestamp=SystemClock.currentTimeMillis()/1000;

        String nonce=request.getHeader().getNonce();
        if(nonce==null || nonce.isEmpty()){
            throw new RuntimeException("nonce cannot is empty!");
        }

        String[] nonceArr = nonce.split("-", 2);
        if(nonceArr.length!=2){
            throw new RuntimeException("nonce is invalid!");
        }

        long timestamp=Long.parseLong(nonceArr[0],16);
        String seq=nonceArr[1];

        long window= nonceWindowSeconds;

        if(Math.abs(currentTimestamp-timestamp)>window){
            throw new RuntimeException("timestamp is exceed allow window seconds!");
        }

        String nonceKey=clientId+nonce;
        if(store.containsNonce(nonceKey)){
            throw new RuntimeException("nonce key already exists!");
        }

        store.setNonce(nonceKey,nonceTimeoutSeconds);

        String sign=request.getHeader().getSign();
        if(sign==null || sign.isEmpty()){
            throw new RuntimeException("sign is invalid!");
        }

        StringBuilder builder=new StringBuilder();
        List<String> parts = request.getParts();
        if(parts!=null){
            for(String part : parts){
                if(part!=null){
                    builder.append(part);
                }
            }
        }

        String data = builder.toString();

        String randomKey=request.getHeader().getRandomKey();
        if(randomKey==null || randomKey.isEmpty()){
            throw new RuntimeException("random key cannot be empty!");
        }

        String clientAsymSign=request.getHeader().getClientAsymSign();
        if(clientAsymSign==null || clientAsymSign.isEmpty()){
            throw new RuntimeException("client asym sign cannot be empty!");
        }

        String serverAsymSign=request.getHeader().getServerAsymSign();
        if(serverAsymSign==null || serverAsymSign.isEmpty()){
            throw new RuntimeException("server asym sign cannot be empty!");
        }

        boolean signOk=messageDigester.verify(sign,data+randomKey+nonce+clientAsymSign+serverAsymSign);
        if(!signOk){
            throw new RuntimeException("verify sign failure!");
        }

        String digital=request.getHeader().getDigital();
        if(digital==null || digital.isEmpty()){
            throw new RuntimeException("digital cannot be empty!");
        }

        String clientPublicKey = store.getOtherPublicKey(clientAsymSign);
        if(clientPublicKey==null || clientPublicKey.isEmpty()){
            throw new RuntimeException("client key not found!");
        }

        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorSupplier.get();
        asymmetricEncryptor.setPublicKey(clientPublicKey);

        boolean digitalOk=asymmetricEncryptor.verify(digital,sign);
        if(!digitalOk){
            throw new RuntimeException("verify digital failure!");
        }

        String serverPrivateKey = store.getSelfPrivateKey(serverAsymSign);
        if(serverPrivateKey==null || serverPrivateKey.isEmpty()){
            throw new RuntimeException("server key not found!");
        }

        asymmetricEncryptor.setPrivateKey(serverPrivateKey);
        String key=asymmetricEncryptor.decrypt(randomKey);
        if(key==null || key.isEmpty()){
            throw new RuntimeException("random key is invalid!");
        }

        ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorSupplier.get();
        symmetricEncryptor.setKey(key);

        if(parts!=null){
            for (String part : parts) {
                String item=null;
                if(part!=null){
                    item=symmetricEncryptor.decrypt(part);
                }
                ret.getParts().add(item);
            }
        }

        return ret;
    }


}
