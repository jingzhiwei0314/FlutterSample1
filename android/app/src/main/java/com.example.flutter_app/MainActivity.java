package com.example.flutter_app;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.sense.data.api.DoeApi;
import com.sense.data.api.bean.RetUserLabel;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    private static final String DOE_CHANNEL = "com.example.flutter_app/doe";

    /********************DOE库********************/
    private static final String DOE_METHOD_CREATE_LABEL = "CreateLabel";
    private static final String DOE_METHOD_SIGN = "SignData";
    private static final String DOE_METHOD_VERIFY_SIGN = "VerifySignData";

    /********************手机盾库********************/
    private static final String DUN_DECRYPT_DATA = "DunDecrypt";
    private static final String DUN_ENCRYPT_DATA = "DunEncrypt";
    private static final String DUN_SIGN_DATA = "DunSignData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "荆志伟MainActivity .....");
//        setContentView(R.layout.activity_main);

        new MethodChannel(getFlutterView(), DOE_CHANNEL).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
                if (call.method.equals(DOE_METHOD_CREATE_LABEL)) {//创建Label
                    String ticket = (String) call.argument("ticket");
                    Log.i("MainActivity", "DOE_METHOD_CREATE_LABEL ticket -> " + ticket);
//                    RetUserLabel userLabel = new RetUserLabel();
//                    int retCode = DoeApi.JniCreateUserKey(ticket, userLabel);
//                    userLabel.userKeyLabel = "66666666Label";
//                    int retCode = -1;
//                    if (retCode == 0) {
//                        result.success(userLabel.userKeyLabel);
//                    } else {
//                        result.error(String.valueOf(retCode), "JniCreateUserKey is err！", null);
//                    }

                    result.success("原生返回创建Lable成功");
                } else if (call.method.equals(DOE_METHOD_SIGN)) {//签名数据
                    result.success("原生返回签名成功");
                } else if (call.method.equals(DOE_METHOD_VERIFY_SIGN)) {//验证签名
                    result.success("原生返回验签成功");
                } else if (call.method.equals(DUN_DECRYPT_DATA)) {//手机盾解密
                    try {
                        String privateKey = (String) call.argument("privateKey");
                        String encData = (String) call.argument("encData");

                        byte[] priBuff = DunUtils.hexToBytes(privateKey);
                        byte[] encBuff = Base64.decode(encData, Base64.NO_WRAP);

                        byte[] painDataBuff = DunUtils.privateKeyDecrypt(EnumCertAlgorithm.SM2_256, priBuff, encBuff);
                        String painDataBase64 = DunUtils.byte2Hex(painDataBuff);

                        Log.i("MainActivity", "DUN_DECRYPT_DATA painDataBase64 -> " + painDataBase64);
                        result.success(painDataBase64);
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.error(String.valueOf(9999), "privateKeyDecrypt is err！", null);
                    }
                } else if (call.method.equals(DUN_ENCRYPT_DATA)) {//手机盾加密
                    try {
                        String publicKey = (String) call.argument("publicKey");
                        String painData = (String) call.argument("painData");

                        byte[] pubBuff = DunUtils.hexToBytes(publicKey);
                        byte[] painBuff = DunUtils.hexToBytes(painData);

                        byte[] encBuff = DunUtils.publicKeyEncrypt(EnumCertAlgorithm.SM2_256, pubBuff, painBuff);
                        String encDataBase64 = DunUtils.byte2Hex(encBuff);

                        Log.i("MainActivity", "DUN_ENCRYPT_DATA encDataBase64 -> " + encDataBase64);
                        result.success(encDataBase64);
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.error(String.valueOf(9999), "privateKeyDecrypt is err！", null);
                    }
                } else if (call.method.equals(DUN_SIGN_DATA)) {//手机盾加密
                    try {
                        String publicKey = (String) call.argument("publicKey");
                        String painData = (String) call.argument("painData");

                        byte[] pubBuff = DunUtils.hexToBytes(publicKey);
                        byte[] painBuff = DunUtils.hexToBytes(painData);

                        byte[] encBuff = DunUtils.publicKeyEncrypt(EnumCertAlgorithm.SM2_256, pubBuff, painBuff);
                        String encDataBase64 = DunUtils.byte2Hex(encBuff);

                        Log.i("MainActivity", "DUN_ENCRYPT_DATA encDataBase64 -> " + encDataBase64);
                        result.success(encDataBase64);
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.error(String.valueOf(9999), "privateKeyDecrypt is err！", null);
                    }
                } else {
                    //未实现接口
                    result.notImplemented();
                }
            }
        });
    }
}
