import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class CreatePage extends StatefulWidget{
  State<CreatePage> createState() => _CreateState();

}

class _CreateState extends State<CreatePage>{
  TextEditingController namedata = TextEditingController();//NAME 입력값
  TextEditingController descriptiondata = TextEditingController();//Description입력값

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text("CreatPage"),
        backgroundColor: Color(0xffa11735),
        elevation: 0.0,
      ),
      body: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              SizedBox(height: 50.0),
              Center(child: Image.network('https://picsum.photos/250?image=9',width: 320.0,height: 180.0)),
              SizedBox(height: 40.0),

              Container(
                margin: EdgeInsets.fromLTRB(10, 0, 0, 0),
                width: 300.0,
                  child:TextField(
                    controller: namedata,
                    decoration: InputDecoration(
                      labelText: 'NAME',
                        labelStyle: TextStyle(color: Colors.black),
                      enabledBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Colors.black,width: 5.0),
                        borderRadius: BorderRadius.all(Radius.circular(3.0)),
                      )
                    ),
                    keyboardType: TextInputType.name,
                  ),

              ),
              SizedBox(height: 20,),
              Container(
                margin: EdgeInsets.fromLTRB(10, 0, 0, 0),
                width: 300.0,
                child:TextField(
                  controller: descriptiondata,
                  decoration: InputDecoration(
                      labelText: 'Description',
                      labelStyle: TextStyle(color: Colors.black),
                      enabledBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Colors.black,width: 5.0),
                        borderRadius: BorderRadius.all(Radius.circular(3.0)),
                      )
                  ),
                  keyboardType: TextInputType.name,
                ),
              ),
              SizedBox(height: 20.0,),
              Text("구매자의 컨텐츠 사용 동의"),
            ],
          ),
        )
      ,
    );
  }

}
