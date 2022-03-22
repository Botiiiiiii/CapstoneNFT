import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ArtPage extends StatefulWidget{
  State<ArtPage> createState() => _ArtState();

}

class _ArtState extends State<ArtPage>{
  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.center,
      child: Text("artPage", textAlign: TextAlign.center)
    );
  }

}
