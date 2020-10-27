package com.example.flutter_app;

import android.util.Base64;

import com.sansec.mobileshield.bx.asym.define.AsymKeyOperateUtil;
import com.sansec.mobileshield.bx.asym.impl.RSAImpl;
import com.sansec.mobileshield.bx.asym.impl.SM2Impl;

public class DunUtils {
    public static SM2Impl sm2Impl = new SM2Impl();
    public static RSAImpl rsaImpl = new RSAImpl();

    public static String privateKeyDecrypt(EnumCertAlgorithm algorithm, String privateKey, String base64Data) throws Exception {
        byte[] buffer = Base64.decode(base64Data, Base64.NO_WRAP);
        byte[] cleartextBuffer = privateKeyDecrypt(algorithm, privateKey, buffer);
        final String plainTextString = Base64.encodeToString(cleartextBuffer, Base64.NO_WRAP);
        return plainTextString;
    }

    public static byte[] privateKeyDecrypt(EnumCertAlgorithm algorithm, String privateKey, byte[] buffer) throws Exception {
        final byte[] privateKeyBuffer = Base64.decode(privateKey, Base64.NO_WRAP);
        final AsymKeyOperateUtil operateImpl = getAsymKeyOperateImpl(algorithm);
        final byte[] cleartextBuffer = operateImpl.privateKeyDecrypt(privateKeyBuffer, buffer);
        return cleartextBuffer;
    }


    public static byte[] privateKeyDecrypt(EnumCertAlgorithm algorithm, byte[] privateKeyBuffer, byte[] buffer) throws Exception {
        final AsymKeyOperateUtil operateImpl = getAsymKeyOperateImpl(algorithm);
        final byte[] cleartextBuffer = operateImpl.privateKeyDecrypt(privateKeyBuffer, buffer);
        return cleartextBuffer;
    }


    public static String publicKeyEncrypt(EnumCertAlgorithm algorithm, String publicKey, String base64Data) throws Exception {
        byte[] buffer = Base64.decode(base64Data, Base64.NO_WRAP);
        final byte[] ciphertextBuffer = publicKeyEncrypt(algorithm, publicKey, buffer);
        final String ciphertextString = Base64.encodeToString(ciphertextBuffer, Base64.NO_WRAP);
        return ciphertextString;
    }

    public static byte[] publicKeyEncrypt(EnumCertAlgorithm algorithm, String publicKey, byte[] buffer) throws Exception {
        final byte[] publicKeyBytes = Base64.decode(publicKey, Base64.NO_WRAP);
        return publicKeyEncrypt(algorithm, publicKeyBytes, buffer);
    }

    public static byte[] publicKeyEncrypt(EnumCertAlgorithm algorithm, byte[] publicKeyBytes, byte[] buffer) throws Exception {
        final AsymKeyOperateUtil operateImpl = getAsymKeyOperateImpl(algorithm);
        final byte[] ciphertextBuffer = operateImpl.publicKeyEncrypt(publicKeyBytes, buffer);
        return ciphertextBuffer;
    }


    public static byte[] hexToBytes(String hex) {
        hex = hex.length() % 2 != 0 ? "0" + hex : hex;

        byte[] b = new byte[hex.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(hex.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    //字节数组转十六进制
    public static String byte2Hex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        if (b == null) return "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(0x00ff & b[i]);
            if (hex.length() < 2) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    private static AsymKeyOperateUtil getAsymKeyOperateImpl(EnumCertAlgorithm algorithm) {
        switch (algorithm) {
            case SM2_256:
                return sm2Impl;
            default:
                return rsaImpl;
        }
    }

}
