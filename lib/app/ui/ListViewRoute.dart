import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ListViewRoute extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return MaterialApp(
      theme: ThemeData(primarySwatch: Colors.blue),
      home: ListViewPage(),
    );
  }
}

class ListViewPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return ListViewState();
  }
}

class ListViewState extends State<ListViewPage> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    Widget divider1 = Divider(color: Colors.blue);
    Widget divider2 = Divider(color: Colors.green);
    Widget divider3 = Divider(color: Colors.red);

    return Scaffold(
      appBar: AppBar(
        title: Text("列表页", style: TextStyle(color: Colors.black,fontSize: 14.0)),
      ),
      body: ListView.separated(
          itemBuilder: (BuildContext context, int index) {
            return Text(
              '$index',
              style: TextStyle(
                  color: index % 2 == 0
                      ? Colors.red
                      : (index % 3 == 0 ? Colors.blue : Colors.black),
                  height: 2),
              textAlign: index % 2 == 0
                  ? TextAlign.left
                  : (index % 3 == 0 ? TextAlign.right : TextAlign.center),
            );
          },
          separatorBuilder: (BuildContext context, int index) {
            return index % 2 == 0
                ? divider1
                : (index % 3 == 0 ? divider3 : divider2);
          },
          itemCount: 100),
    );
    // return ListView.separated(
    //   itemCount: 100,
    //   //列表项构造器
    //   itemBuilder: (BuildContext context, int index) {
    //     return Text("$index");
    //   },
    //   //分割器构造器
    //   separatorBuilder: (BuildContext context, int index) {
    //     return index % 2 == 0 ? divider1 : divider2;
    //   },
    // );
  }
}
