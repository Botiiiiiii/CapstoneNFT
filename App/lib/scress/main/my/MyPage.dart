import 'package:app/scress/login/LoginPage.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
class MyPage extends StatefulWidget{
  State<MyPage> createState() => _MyState();
}


class _MyState extends State<MyPage>{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
       title: Text("MyPage"),
        centerTitle: true,
        elevation: 0.0,
        backgroundColor: Color(0xffa11735),
      ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text("로그인 하시오",style: TextStyle(fontSize: 20.0),),
              SizedBox(height: 20.0,),
            ButtonTheme(

                minWidth: 85.0,
                height: 50.0,
                child: RaisedButton(
                  onPressed: (){
                    Navigator.push(context,
                    MaterialPageRoute(builder:(BuildContext context)=>LoginPage()));
                  },
                  child: Text("Login Page",style: TextStyle(fontSize: 15.0,color: Colors.white)),
                  color: Color(0xffa11735),
                )
            ),

            ],
          ),
        ),
         );
  }

}
