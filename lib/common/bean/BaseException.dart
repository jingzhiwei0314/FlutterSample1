class BaseException<T> {
  int requestType;
  String displayMessage;
  T data;

  BaseException(this.requestType, this.displayMessage, this.data);


}
