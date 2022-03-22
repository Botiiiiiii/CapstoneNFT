import 'package:flutter/cupertino.dart';

class CreatePage extends StatefulWidget{
  State<CreatePage> createState() => _CreateState();

}

class _CreateState extends State<CreatePage>{
  @override
  Widget build(BuildContext context) {
    return Container(
        alignment: Alignment.center,

        child: Text("CreatePage", textAlign: TextAlign.center)
    );
  }

}
