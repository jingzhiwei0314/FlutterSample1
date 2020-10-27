//package com.example.flutter_app;
//
//import cn.bidsun.common.model.EnumCertAlgorithm;
//import com.sansec.mobileshield.bx.asym.define.AsymKeyOperateUtil;
//import com.sansec.mobileshield.bx.asym.hash.HashUtil;
//import com.sansec.mobileshield.bx.asym.hash.SM3Util;
//import com.sansec.mobileshield.bx.asym.impl.RSAImpl;
//import com.sansec.mobileshield.bx.asym.impl.SM2Impl;
//import org.bouncycastle.asn1.ASN1InputStream;
//import org.bouncycastle.asn1.DERBitString;
//import org.bouncycastle.asn1.x509.Certificate;
//import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.security.PublicKey;
//import java.security.Security;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;
//
//public class CAUtil {
//	public static SM2Impl sm2Impl = new SM2Impl();
//	public static RSAImpl rsaImpl = new RSAImpl();
//
//    static {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//    }
//
//    public static String privateKeyDecrypt(EnumCertAlgorithm algorithm, String privateKey, String base64Data) throws Exception {
//        byte[] buffer = Base64Util.decode(base64Data);
//        byte[] cleartextBuffer = privateKeyDecrypt(algorithm, privateKey, buffer);
//        final String plainTextString = Base64Util.encode(cleartextBuffer);
//        return plainTextString;
//    }
//
//    public static byte[] privateKeyDecrypt(EnumCertAlgorithm algorithm, String privateKey, byte[] buffer) throws Exception {
//        final byte[] privateKeyBuffer = Base64Util.decode(privateKey);
//        final AsymKeyOperateUtil operateImpl = getAsymKeyOperateImpl(algorithm);
//        final byte[] cleartextBuffer = operateImpl.privateKeyDecrypt(privateKeyBuffer, buffer);
//        return cleartextBuffer;
//    }
//
//    public static String publicKeyEncrypt(EnumCertAlgorithm algorithm, String publicKey, String base64Data) throws Exception {
//        final byte[] buffer = Base64Util.decode(base64Data);
//        final byte[] ciphertextBuffer = publicKeyEncrypt(algorithm, publicKey, buffer);
//        final String ciphertextString = Base64Util.encode(ciphertextBuffer);
//        return ciphertextString;
//    }
//
//    public static byte[] publicKeyEncrypt(EnumCertAlgorithm algorithm, String publicKey, byte[] buffer) throws Exception {
//        final byte[] publicKeyBytes = Base64Util.decode(publicKey);
//        return publicKeyEncrypt(algorithm, publicKeyBytes, buffer);
//    }
//
//    public static byte[] publicKeyEncrypt(EnumCertAlgorithm algorithm, byte[] publicKeyBytes, byte[] buffer) throws Exception {
//        final AsymKeyOperateUtil operateImpl = getAsymKeyOperateImpl(algorithm);
//        final byte[] ciphertextBuffer = operateImpl.publicKeyEncrypt(publicKeyBytes, buffer);
//        return ciphertextBuffer;
//    }
//
//    private static AsymKeyOperateUtil getAsymKeyOperateImpl(EnumCertAlgorithm algorithm){
//        switch (algorithm) {
//            case SM2_256:
//                return sm2Impl;
//            default:
//                return rsaImpl;
//        }
//    }
//
//    /**
//     * 证书验证用户签名，带预处理
//     */
//    public static boolean certVerifySignature(EnumCertAlgorithm algorithm, String signCert, String signString, String signValue) throws Exception{
//        final byte[] signCertBytes = Base64Util.decode(signCert);
//        final byte[] signStringBytes = Base64Util.decode(signString);
//        final byte[] signValueBytes = Base64Util.decode(signValue);
//
//        final byte[] pretreatmentSignatureStringBytes = pretreatmentSignatureString(algorithm, signCert, signStringBytes);
//
//        final AsymKeyOperateUtil operateImpl = getAsymKeyOperateImpl(algorithm);
//        final boolean success = operateImpl.certVerifySignature(signCertBytes, pretreatmentSignatureStringBytes, signValueBytes);
//        return success;
//    }
//
//    /**
//     * 公钥验证用户签名，带预处理
//     */
//    public static boolean publicKeyVerifySignature(EnumCertAlgorithm algorithm, String signPublicKey, String signString, String signValue) throws Exception{
//        final byte[] signPublicKeyBytes = Base64Util.decode(signPublicKey);
//        final byte[] signStringBytes = Base64Util.decode(signString);
//        final byte[] signValueBytes = Base64Util.decode(signValue);
//
//        final byte[] pretreatmentSignatureStringBytes = pretreatmentSignatureStringWithPublicKey(algorithm, signPublicKey, signStringBytes);
//
//        final AsymKeyOperateUtil operateImpl = getAsymKeyOperateImpl(algorithm);
//        final boolean success = operateImpl.publicKeyVerifySignature(signPublicKeyBytes, pretreatmentSignatureStringBytes, signValueBytes);
//        return success;
//    }
//
//    /**
//     * 证书验证用户签名，不带预处理
//     */
//    public static boolean certVerifySignatureWithoutHash(EnumCertAlgorithm algorithm, String signCert, String signString, String signValue) throws Exception{
//    	final byte[] signCertBytes = Base64Util.decode(signCert);
//    	final byte[] signStringBytes = Base64Util.decode(signString);
//    	final byte[] signValueBytes = Base64Util.decode(signValue);
//    	final AsymKeyOperateUtil operateImpl = getAsymKeyOperateImpl(algorithm);
//    	final boolean success = operateImpl.certVerifySignature(signCertBytes, signStringBytes, signValueBytes);
//    	return success;
//    }
//
//    /**
//     * 公钥验证用户签名，不带预处理
//     */
//    public static boolean publicKeyVerifySignatureWithoutHash(EnumCertAlgorithm algorithm, String signPublicKey, String signString, String signValue) throws Exception{
//        final byte[] signPublicKeyBytes = Base64Util.decode(signPublicKey);
//        final byte[] signStringBytes = Base64Util.decode(signString);
//        final byte[] signValueBytes = Base64Util.decode(signValue);
//        final AsymKeyOperateUtil operateImpl = getAsymKeyOperateImpl(algorithm);
//        final boolean success = operateImpl.publicKeyVerifySignature(signPublicKeyBytes, signStringBytes, signValueBytes);
//        return success;
//    }
//
//    // 预处理签名原文
//    private static byte[] pretreatmentSignatureString(EnumCertAlgorithm algorithm, String signCert, byte[] signStringBytes) throws Exception{
//        final byte[] publicKeyBytes = parsePublicKey(algorithm, signCert);
//
//        final byte[] bytes;
//        if (algorithm == EnumCertAlgorithm.SM2_256){
//            bytes = SM3Util.dataPreProcessSM3(publicKeyBytes, signStringBytes);
//        }else{
//            bytes = HashUtil.hashDataAndEncodeHashAlg(signStringBytes,"sha1");
//        }
//
//        return bytes;
//    }
//
//    // 预处理签名原文
//    private static byte[] pretreatmentSignatureStringWithPublicKey(EnumCertAlgorithm algorithm, String signPublicKey, byte[] signStringBytes) throws Exception{
//        final byte[] publicKeyBytes = Base64Util.decode(signPublicKey);
//
//        final byte[] bytes;
//        if (algorithm == EnumCertAlgorithm.SM2_256){
//            bytes = SM3Util.dataPreProcessSM3(publicKeyBytes, signStringBytes);
//        }else{
//            bytes = HashUtil.hashDataAndEncodeHashAlg(signStringBytes,"sha1");
//        }
//
//        return bytes;
//    }
//
//    public static byte[] parsePublicKey(EnumCertAlgorithm algorithm, String certStr) throws Exception{
//        if (algorithm == EnumCertAlgorithm.SM2_256){
//            return parseSM2PublicKey(certStr);
//        }else{
//            return parseRSAPublicKey(certStr);
//        }
//    }
//
//    private static byte[] parseSM2PublicKey(String certStr) throws Exception{
//        byte[] certBytes = Base64Util.decode(certStr);
//        InputStream inStream = new ByteArrayInputStream(certBytes);
//        ASN1InputStream aIn = new ASN1InputStream(inStream);
//        Certificate cert = Certificate.getInstance(aIn.readObject());
//        SubjectPublicKeyInfo subjectPublicKeyInfo = cert.getSubjectPublicKeyInfo();
//        DERBitString publicKey = subjectPublicKeyInfo.getPublicKeyData();
//        final byte[] publicKeyBytes = publicKey.getBytes();
//        return publicKeyBytes;
//    }
//
//    private static byte[] parseRSAPublicKey(String certStr) throws Exception {
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//        byte[] certBytes = Base64Util.decode(certStr);
//        InputStream in = new ByteArrayInputStream(certBytes);
//        X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
//        PublicKey publicKey = cert.getPublicKey();
//        final byte[] publicKeyBytes = publicKey.getEncoded();
//        return publicKeyBytes;
//    }
//
//    public static void main(String[] args) throws Exception {
////        String signCert = "MIICvTCCAmGgAwIBAgIQAqHRrinZSXm2xYghdZ39VzAMBggqgRzPVQGDdQUAMFExCzAJBgNVBAYTAkNOMTMwMQYDVQQKDCrljJfkuqzkuJbnuqrpgJ/noIHkv6Hmga/np5HmioDmnInpmZDlhazlj7gxDTALBgNVBAMMBENTQ0EwHhcNMTkwOTAzMTYwMDAwWhcNMjAwOTAzMTU1OTU5WjBsMRswGQYDVQQEDBIxMzA1MjUxNjgzMDUwMTAwMzIxEDAOBgNVBAoMB2RlZmF1bHQxEDAOBgNVBAsMB2RlZmF1bHQxKTAnBgNVBAMMIDEwMDAxXzQ3MmQ0Y2U0Yjk4ZmRlMTZkOGViZGZkNWFiMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE85fNHNSFLAdDlX4M1w9y6mrsGae7DRf9TeZQGu/JdF8wMWUaSKImMLqOiouYrcPsozbXXlFiVg8c7DZvbfgaaqOB/TCB+jAfBgNVHSMEGDAWgBS7rVIWTJokVEvYOQHs47aR4ZNybzAdBgNVHQ4EFgQUGBuFdGAv6td3Ousw+z+TTT5fJKwwCQYDVR0TBAIwADBfBgNVHR8EWDBWMFSgUqBQhk5odHRwOi8vcmEuY3NtYXJ0LmNvbS5jbi9DZXJ0aWZpY2F0ZS9HZXRjcmw/cmE9NWQ3ZDZjNTE3Y2NmNWJiMjQ1NTQ1YjcwNWVhNDdmYWMwCwYDVR0PBAQDAgP4MD8GA1UdJQQ4MDYGCisGAQQBgjcUAgIGCCsGAQUFBwMBBggrBgEFBQcDAgYIKwYBBQUHAwQGCisGAQQBgjcKAwQwDAYIKoEcz1UBg3UFAANIADBFAiB45d9s4e5RC/hDZs2l20MYPnfXmwFPYfiaCMRgVkxmUQIhANd6S3ObmubLQPjAuVt6XRKYWQ3bL6E/youasT99vKf3";
////        String signString = "eyJ1c2VyU2lnbml0dXJlVGltZXN0YW1wIjoxNTY4OTY5NDI2MDc4LCJ1c2VyTmFtZSI6IuadjnNta0NDIiwiaGFzaCI6Imhhc2gxNTY4OTY5NDI2MDc4In0=";
////        String signValue = "cof0XaSmevkURRVcPUOR+XWyJRkljd9YKOk59yNcvGJRTUZux/F3iYXIY1Bo1x5MzUGVfolz9KBGdGjuhHbOvQ==";
////
////        byte[] pretreatmentSignatureStringBytes = pretreatmentSignatureString(EnumCertAlgorithm.SM2_256, signCert, Base64Util.decode(signString));
////        String pretreatmentSignatureString = Base64Util.encode(pretreatmentSignatureStringBytes);
////        System.out.println("pretreatmentSignatureString:" + pretreatmentSignatureString);
////
////        String pubKey = "BCVn/rnav6i1eZW+anYruYQR1eKcGbXlAvZ+GWEQwR9A6FSMZF5a4c0wY34LUtEiBOrEQTGRh17XTBynDtLESYU=";
////        Base64Util.decode(pubKey);
////        String s = "asd123";
////        String base64Data = Base64Util.encode(s.getBytes("UTF-8"));
////        String encrypted = publicKeyEncrypt(EnumCertAlgorithm.SM2_256, pubKey, base64Data);
////        System.out.println(encrypted);
////        System.out.println(Base64Util.decode(pubKey).length);
////
//////        certVerifySignature(EnumCertAlgorithm.SM2_256, signCert, signString, signValue);
////
////        pubKey = "D/D0/9MtouAikYh4gIe/KJ23eq/QkktTJI0311elDZvoYWxXdPWyEASl0XIAb1ms9O09qUCosY+dgmB3xAJZIA==";
////        String result = publicKeyEncrypt(EnumCertAlgorithm.SM2_256, pubKey, base64Data);
////        System.out.println(result);
////
////        String sm2PubKey = "Q4v58+bUgj1j+NuZlf/Xc9pCn82K1P+CWpMf3X1Z+6ieN7/2kJNZtzHaPWbDdu5CQUWVOiYkZfL9F+qJoOtftg==";
////    	String sm2PriKey = "zew0AFAkzINsXsyqCg3K7jtD5HMRB+R9MjM5Z3LV2zs=";
////    	String rsaPriKey = "MIICXAIBAAKBgQDLZt4noV7dpVS5+qAqJwGlFyXBqIdPIkVjo2LHXYfRhahHLKls1fj0V0VR/oH+kzLfX3MkGefnsWoIPz9QN0lpmxHtURUBEaQ+utOMkAg2hQOqmFXRc5GcSUz/i+Xwh+MTCUavpAoeF3TiAfE0RihMSdn601zUOE4Nv3fcZAsQ8QIDAQABAoGAR7MlUJcD1+H18fE1EGbbn5R/TLrZp72O9EaeBWUlAWi6HwzRVHpA3fD9kJ/EtzawUilVbz5qjzPtCqcw7Ffw2lWw3KDiH6e38iLtPUV6vSPrYIfAhjaO5iTmtmCQpEFYrhaJg8JOV1vFaQfRZuP7BwzstOkFcy/MAEehX0MhcBkCQQD6ArPT+glgegLoS16GhpqJruFoBMCMBeIXm5QTQ28f6UIl0oATwtwzG7WRACXmeK56zdpQH9KiO5yfOrTWuUwzAkEA0EZRNJprKS4GbE2JErQiFIvmNY65BJEEMns9g5gOzbbCHV58P4RL/zNr/Wc9yVBT/Fmyp957AE5pukt72llKSwJACzcr0gqZeRaAIuOerfJD5IFP41azPJTW+7AjCGoyBgB00zWKQViwgCpyDrsu+BPZ3/B0JrwGuvhpoksD6c4OOQJAGHx75ZxWBvRjSu5etDU7YIEGIrvR4siN3sWAeO0yqFrZPYyzPOB38tv/0T4HFJj+FsgQ6g2NFn8hjQZ83DHtOwJBAOwh9vXJ2CAZSP/kTPYP886jqC3aYpp+zcHGNh6dbcgiGfXKj22cyTaTKR8op8Vhur1WIApdQSTB94w/DklFWw8=";
////    	String rsaPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLZt4noV7dpVS5+qAqJwGlFyXBqIdPIkVjo2LHXYfRhahHLKls1fj0V0VR/oH+kzLfX3MkGefnsWoIPz9QN0lpmxHtURUBEaQ+utOMkAg2hQOqmFXRc5GcSUz/i+Xwh+MTCUavpAoeF3TiAfE0RihMSdn601zUOE4Nv3fcZAsQ8QIDAQAB";
////
////    	String encryptData = publicKeyEncrypt(EnumCertAlgorithm.SM2_256, sm2PubKey, base64Data);
////    	System.out.println(new String(Base64Util.decode(privateKeyDecrypt(EnumCertAlgorithm.SM2_256, sm2PriKey, encryptData))));
////
////    	encryptData = publicKeyEncrypt(EnumCertAlgorithm.RSA_1024, rsaPubKey, base64Data);
////    	System.out.println(new String(Base64Util.decode(privateKeyDecrypt(EnumCertAlgorithm.RSA_1024, rsaPriKey, encryptData))));
////
//        String publicKey = "F1nAHGpeYOKujRcUcPEBXHDeV3aQa7jW4H8WIeIO3DYMygVv9FUmXfB4RZY5tTXVBly9hW3S+PEGrffoWGSCUA==";
//        String hash = "plT0LSJlEahwuh84KcfYlNtsWVRt7x41h2adrvoS7K8=";
//        String signValue = "OXxDr8zY7JiItO1OnZtmeb71sC6wcboePrZEnDziaQQbsCK2nfJcmHC96IP5Kw/VOLGWwq/e8kY98RyYfiOZLQ==";
//        boolean verifySignature = publicKeyVerifySignatureWithoutHash(EnumCertAlgorithm.SM2_256, publicKey, hash, signValue);
//        System.out.println(verifySignature);
//    }
//
//    private static String genTestData(int maxCount) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int count = RandomUtil.randomInt(10, maxCount);
////        int count = 10;
//
//        for (int i = 0; i < count; i++) {
//            int v = RandomUtil.randomInt(0, 300);
////            int v = i;
//            out.write(v);
//        }
//
//        byte[] bytes = out.toByteArray();
//        String encode = Base64Util.encode(bytes);
//
//        System.out.println("count:" + bytes.length);
//        System.out.println("plain:" + encode);
//        return encode;
//    }
//
//}
