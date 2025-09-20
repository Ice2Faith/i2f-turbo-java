package i2f.authentication.password.impl;

import i2f.authentication.password.IPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author Ice2Faith
 * @date 2025/9/20 17:26
 */
public class MessageDigestPasswordEncoder implements IPasswordEncoder {
    public static final MessageDigestPasswordEncoder SHA_1=new MessageDigestPasswordEncoder("SHA-1");
    private String algorithm="SHA-1";
    private String provider;
    private String salt="";
    private int repeat=0;

    public MessageDigestPasswordEncoder() {
    }

    public MessageDigestPasswordEncoder(String algorithm) {
        this.algorithm = algorithm;
    }

    public MessageDigestPasswordEncoder(String algorithm, String salt) {
        this.algorithm = algorithm;
        this.salt = salt;
    }

    public MessageDigestPasswordEncoder(String algorithm, String provider, String salt) {
        this.algorithm = algorithm;
        this.provider = provider;
        this.salt = salt;
    }

    public MessageDigestPasswordEncoder(String algorithm, String provider, String salt, int repeat) {
        this.algorithm = algorithm;
        this.provider = provider;
        this.salt = salt;
        this.repeat = repeat;
    }

    public MessageDigest getDigester() throws Exception {
        if(provider==null || provider.isEmpty()){
            return MessageDigest.getInstance(algorithm);
        }
        return MessageDigest.getInstance(algorithm, provider);
    }

    @Override
    public String encode(String rawPassword) {
        if(rawPassword==null){
            return null;
        }
        try {
            MessageDigest digester=getDigester();
            byte[] input=((salt==null || salt.isEmpty()?"":salt)+rawPassword).getBytes(StandardCharsets.UTF_8);
            byte[] output=digester.digest(input);
            for (int i = 0; i < repeat; i++) {
                output= digester.digest(output);
            }
            StringBuilder builder=new StringBuilder();
            for (byte bt : output) {
                builder.append(String.format("%02x",(int)(bt&0x0ff)));
            }
            return builder.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
