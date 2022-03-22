import 'package:flutter/cupertino.dart';

class MyPage extends StatefulWidget{
  State<MyPage> createState() => _MyState();

}

class _MyState extends State<MyPage>{
  @override
  Widget build(BuildContext context) {
    return Container(
        alignment: Alignment.center,

        child: Text("MyPage", textAlign: TextAlign.center)
    );
  }

}
