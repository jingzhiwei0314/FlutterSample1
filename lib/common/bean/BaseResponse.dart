class BaseResponse<T> {
  int requestType;
  String displayMessage;
  T data;

  BaseResponse(this.requestType, this.displayMessage, this.data);

}
