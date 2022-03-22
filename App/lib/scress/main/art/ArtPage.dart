import 'package:app/models/TestImageModel.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ArtPage extends StatefulWidget {
  const ArtPage({Key? key}) : super(key: key);

  State<ArtPage> createState() => _ArtState();
}

class _ArtState extends State<ArtPage> {
  var arrData = <TestImageModel>[];

  void getData() {
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
    arrData.add(TestImageModel(
        "name", const Image(image: AssetImage('asset/img.png'))));
  }

  @override
  void initState() {
    getData();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(),
        body: Container(
            child: GridView.builder(
                itemCount: arrData.length,
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 3, //1 개의 행에 보여줄 item 개수
                  childAspectRatio: 1 / 1, //item 의 가로 1, 세로 2 의 비율
                  mainAxisSpacing: 5, //수평 Padding
                  crossAxisSpacing: 10, //수직 Padding
                ),
                itemBuilder: (BuildContext context, int index) {
                  return Container(
                      decoration: const BoxDecoration(
                        borderRadius: BorderRadius.all(Radius.circular(5.0)),
                      ),
                      child: Column(children: [
                        arrData[index].image,
                        Text(arrData[index].name)
                      ]));
                })));
  }
}
