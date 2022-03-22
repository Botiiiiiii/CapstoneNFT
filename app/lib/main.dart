import 'package:app/scress/main/MainPage.dart';
import 'package:flutter/material.dart';
import 'package:splashscreen/splashscreen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'NTF-Market',
        theme: ThemeData(primarySwatch: Colors.blue),
        home: SplashScreen(
          seconds: 5,
          navigateAfterSeconds: MainPage(),
          title: const Text(
            "세종 NFT",
            style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 20.0,
                color: Colors.white),
          ),
          backgroundColor: const Color(0xffa11735),
        ));
  }
}
