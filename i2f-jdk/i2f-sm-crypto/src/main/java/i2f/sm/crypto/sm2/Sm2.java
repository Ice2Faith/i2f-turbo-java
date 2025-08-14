package i2f.sm.crypto.sm2;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:39
 */
public class Sm2 {
    public static KeyPair generateKeyPairHex() {
        return Utils.generateKeyPairHex();
    }

    public static String compressPublicKeyHex(String s) {
        return Utils.compressPublicKeyHex(s);
    }

    public static boolean comparePublicKeyHex(String publicKey1, String publicKey2) {
        return Utils.comparePublicKeyHex(publicKey1, publicKey2);
    }

    public static boolean verifyPublicKey(String publicKey) {
        return Utils.verifyPublicKey(publicKey);
    }

    public static String doEncrypt(String msg, String publicKey) {
        return doEncrypt(msg, publicKey, null);
    }

    public static String doEncrypt(String msg, String publicKey, Sm2Cipher.CipherMode cipherMode) {
        return Sm2Cipher.doEncrypt(msg, publicKey, cipherMode);
    }

    public static byte[] doEncrypt(byte[] msg, String publicKeyHex) {
        return doEncrypt(msg, publicKeyHex, null);
    }

    public static byte[] doEncrypt(byte[] msg, String publicKeyHex, Sm2Cipher.CipherMode cipherMode) {
        return Sm2Cipher.doEncrypt(msg, publicKeyHex, cipherMode);
    }

    public static String doDecrypt(String encryptData, String privateKey) {
        return doDecrypt(encryptData, privateKey, null);
    }

    public static String doDecrypt(String encryptData, String privateKey, Sm2Cipher.CipherMode cipherMode) {
        return Sm2Cipher.doDecrypt(encryptData, privateKey, cipherMode);
    }

    public static byte[] doDecrypt(byte[] encryptData, String privateKeyHex) {
        return doDecrypt(encryptData, privateKeyHex, null);
    }

    public static byte[] doDecrypt(byte[] encryptData, String privateKeyHex, Sm2Cipher.CipherMode cipherMode) {
        return Sm2Cipher.doDecrypt(encryptData, privateKeyHex, cipherMode);
    }

    public static String doSignature(String msg, String privateKey
    ) {
        return doSignature(msg, privateKey, null, false, false, null, null);
    }

    public static String doSignature(String msg, String privateKey,
                                     List<Sm2Cipher.Point> pointPool, boolean der, boolean hash, String publicKey, String userId
    ) {
        return Sm2Cipher.doSignature(msg, privateKey, pointPool, der, hash, publicKey, userId);
    }

    public static byte[] doSignature(byte[] msg, String privateKey) {
        return doSignature(msg, privateKey, null, false, false, null, null);
    }

    public static byte[] doSignature(byte[] msg, String privateKey,
                                     List<Sm2Cipher.Point> pointPool, boolean der, boolean hash, String publicKey, String userId
    ) {
        return Sm2Cipher.doSignature(msg, privateKey, pointPool, der, hash, publicKey, userId);
    }

    public static boolean doVerifySignature(String msg, String signHex, String publicKey) {
        return doVerifySignature(msg, signHex, publicKey, false, false, null);
    }

    public static boolean doVerifySignature(String msg, String signHex, String publicKey,
                                            boolean der, boolean hash, String userId) {
        return Sm2Cipher.doVerifySignature(msg, signHex, publicKey, der, hash, userId);
    }

    public static boolean doVerifySignature(byte[] msg, byte[] signHex, String publicKey) {
        return doVerifySignature(msg, signHex, publicKey, false, false, null);
    }

    public static boolean doVerifySignature(byte[] msg, byte[] signHexByte, String publicKey,
                                            boolean der, boolean hash, String userId) {
        return Sm2Cipher.doVerifySignature(msg, signHexByte, publicKey, der, hash, userId);
    }

    public static String getPublicKeyFromPrivateKey(String privateKey) {
        return Sm2Cipher.getPublicKeyFromPrivateKey(privateKey);
    }

    public static Sm2Cipher.Point getPoint() {
        return Sm2Cipher.getPoint();
    }
}
