import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_app/app/ui/LoginRoute.dart';
import 'package:flutter_app/common/DomainType.dart';
import 'package:flutter_app/common/bean/BaseException.dart';
import 'package:flutter_app/common/bean/BaseResponse.dart';
import 'package:flutter_app/domain/DoeController.dart';

// void main() {
//   runApp(MyApp());
// }
//

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  DoeController doeController = new DoeController();

  String state;

  void onFinish(BaseResponse baseResponse) {
    switch (baseResponse.requestType) {
      case DomainType.DOE_CREATE_LABEL:
        String result = baseResponse.data;
        setState(() {
          state = '创建Label成功，label=$result';
        });
        break;
      case DomainType.DOE_SIGN_DATA:
        String result = baseResponse.data;
        setState(() {
          state = '签名数据成功，label=$result';
        });
        break;
      case DomainType.DOE_VERIFY_SIGN:
        String result = baseResponse.data;
        setState(() {
          state = '验证签名成功，label=$result';
        });
        break;
      case DomainType.DUN_DECRYPT_DATA:
        String result = baseResponse.data;
        setState(() {
          state = '手机盾解密成功，result=$result';
        });
        break;
      case DomainType.DUN_ENCRYPT_DATA:
        String result = baseResponse.data;
        setState(() {
          state = '手机盾加密成功，result=$result';
        });
        break;
    }
  }

  void onError(BaseException exception) {
    switch (exception.requestType) {
      case DomainType.DOE_CREATE_LABEL:
        String data = exception.data;
        String message = exception.displayMessage;
        setState(() {
          state = '创建Label失败，状态=$data，错误文案$message';
        });
        break;
      case DomainType.DOE_SIGN_DATA:
        String data = exception.data;
        String message = exception.displayMessage;
        setState(() {
          state = '签名数据失败，状态=$data，错误文案$message';
        });
        break;
      case DomainType.DOE_VERIFY_SIGN:
        String data = exception.data;
        String message = exception.displayMessage;
        setState(() {
          state = '验证签名失败，状态=$data，错误文案$message';
        });
        break;
      case DomainType.DUN_DECRYPT_DATA:
        String data = exception.data;
        String message = exception.displayMessage;
        setState(() {
          state = '手机盾解密失败，状态=$data，错误文案$message';
        });
        break;
      case DomainType.DUN_ENCRYPT_DATA:
        String data = exception.data;
        String message = exception.displayMessage;
        setState(() {
          state = '手机盾加密失败，状态=$data，错误文案$message';
        });
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            // RaisedButton.icon(
            //   icon: Icon(Icons.add),
            //   label: Text("创建Label"),
            //   onPressed: () {
            //     doeController.createLabel("6666", onFinish, onError);
            //   },
            // ),
            // FlatButton.icon(
            //   color: Colors.green,
            //   icon: Icon(Icons.send),
            //   label: Text("签名数据"),
            //   onPressed: () {
            //     doeController.signData(onFinish, onError);
            //   },
            // ),
            // OutlineButton.icon(
            //   icon: Icon(Icons.verified_user),
            //   label: Text("验证签名"),
            //   onPressed: () {
            //     doeController.verifySign(onFinish, onError);
            //   },
            // ),
            RaisedButton.icon(
              icon: Icon(Icons.add),
              label: Text("手机盾加密"),
              onPressed: () {
                doeController.dunEncrypt(onFinish, onError, "12314", "1231");
              },
            ),
            RaisedButton.icon(
              icon: Icon(Icons.add),
              label: Text("手机盾解密"),
              onPressed: () {
                doeController.dunDecrypt(
                    onFinish,
                    onError,
                    "4a16e37a14845f0e42a12a26e34934e21ae6e394058c644a5b64f6970239b1fa",
                    "gQwFOfhf5UhgHvHVsqKq+4hVXQaaSmH2+qDDNxsE/ZHZkSieMoVdIA+DyOyMTIRun3S9uVvyoN6jMxbxfk7y4+vWYPmIU4s5oZZAuL3Hd8vv0Xe5CJhoITnip1MrdMvjlGxBJzV581EQMB/oieh9gtJfTkKi");
              },
            ),
            RaisedButton(
              child: Text(
                "去登录",
                // style: TextStyle(color: Colors.white),
              ),
              //按钮文字颜色
              textColor: Colors.white,
              //按钮背景色
              color: Colors.blue,
              //按钮按下时背景色
              highlightColor: Colors.blue[700],
              //点击时，水波动画中水波颜色
              splashColor: Colors.grey,
              //按钮主题，默认是浅色主题
              colorBrightness: Brightness.light,
              //外形
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20.0)),
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) {
                  return LoginRoute();
                }));
              },
            ),
            Text('调用接口情况：$state'),
          ],
        ),
      ),
    );
  }
}
