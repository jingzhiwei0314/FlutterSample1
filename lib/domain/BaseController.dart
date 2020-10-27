import 'package:flutter_app/common/bean/BaseException.dart';
import 'package:flutter_app/common/bean/BaseResponse.dart';

class BaseController {
  onFinish({var onFinish, int requestType, String displayMessage, var data}) {
    onFinish(new BaseResponse(requestType, displayMessage, data));
  }

  onError({var onError, int requestType, Exception e, var data}) {
    String displayMessage = "出错啦";
    onError(new BaseException(requestType, displayMessage, data));
  }
}
