import 'package:flutter/services.dart';
import 'package:flutter_app/common/DomainType.dart';
import 'package:flutter_app/domain/BaseController.dart';

class DoeController extends BaseController {
  static const platform = const MethodChannel('com.example.flutter_app/doe');

  void createLabel(String ticket, var finishCall, var errCall) async {
    int requestType = DomainType.DOE_CREATE_LABEL;
    try {
      Map<String, String> map = {"ticket": ticket};
      final String label = await platform.invokeMethod("CreateLabel", map);
      onFinish(onFinish: finishCall, requestType: requestType, data: label);
    } on PlatformException catch (e) {
      onError(onError: errCall, requestType: requestType, e: e, data: '出现错误');
    }
  }

  void signData(var finalCall, var errCall) async {
    int requestType = DomainType.DOE_SIGN_DATA;
    try {
      final String result = await platform.invokeMethod("SignData");
      onFinish(onFinish: finalCall, requestType: requestType, data: result);
    } catch (e) {
      onError(onError: errCall, requestType: requestType, e: e);
    }
  }

  void verifySign(var finishCall, var errCall) async {
    int requestType = DomainType.DOE_VERIFY_SIGN;
    try {
      final String result = await platform.invokeMethod("VerifySignData");
      onFinish(onFinish: finishCall, requestType: requestType, data: result);
    } catch (e) {
      onError(onError: errCall, requestType: requestType, e: e);
    }
  }

  void dunDecrypt(
      var finishCall, var errCall, var privateKey, var encData) async {
    int requestType = DomainType.DUN_DECRYPT_DATA;
    try {
      Map<String, String> map = {"privateKey": privateKey, "encData": encData};
      final String result = await platform.invokeMethod("DunDecrypt", map);
      onFinish(onFinish: finishCall, requestType: requestType, data: result);
    } on PlatformException catch (e) {
      onError(onError: errCall, requestType: requestType, e: e, data: e.code);
    } catch (e) {
      onError(onError: errCall, requestType: requestType, e: e);
    }
  }

  void dunEncrypt(
      var finishCall, var errCall, var publicKey, var painData) async {
    int requestType = DomainType.DUN_ENCRYPT_DATA;
    try {
      Map<String, String> map = {"publicKey": publicKey, "painData": painData};
      final String result = await platform.invokeMethod("DunDecrypt", map);
      onFinish(onFinish: finishCall, requestType: requestType, data: result);
    } on PlatformException catch (e) {
      onError(onError: errCall, requestType: requestType, e: e, data: e.code);
    } catch (e) {
      onError(onError: errCall, requestType: requestType, e: e);
    }
  }
}
