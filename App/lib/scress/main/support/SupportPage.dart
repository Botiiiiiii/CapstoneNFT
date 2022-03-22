import 'package:flutter/cupertino.dart';

class SupportPage extends StatefulWidget{
  State<SupportPage> createState() => _SupportState();

}

class _SupportState extends State<SupportPage>{
  @override
  Widget build(BuildContext context) {
    return Container(
        alignment: Alignment.center,

        child: Text("SupportPage", textAlign: TextAlign.center)
    );
  }

}
