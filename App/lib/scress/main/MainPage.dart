import 'package:app/scress/main/art/ArtPage.dart';
import 'package:app/scress/main/create/CreatePage.dart';
import 'package:app/scress/main/my/MyPage.dart';
import 'package:app/scress/main/support/SupportPage.dart';
import 'package:flutter/material.dart';

class MainPage extends StatelessWidget {
  const MainPage({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage>
    with SingleTickerProviderStateMixin {
  int _page = 1;
  late PageController _pController;
  late TabController _tController;

  @override
  void initState() {
    super.initState();
    _pController = PageController();
    _tController = TabController(length: 4, vsync: this);
  }

  @override
  void dispose() {
    _pController.dispose();
    _tController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      bottomNavigationBar: Container(
          color: Color(0xffa11735),
          child: TabBar(
            controller: _tController,
            onTap: (value) {
              _page = value;
              _pController.animateToPage(_page,
                  duration: Duration(milliseconds: 300), curve: Curves.ease);
            },
            tabs: const [
              Tab(
                icon: Icon(Icons.account_circle),
                text: "My",

              ),
              Tab(
                icon: Icon(Icons.apps_outlined),
                text: "Gallery",
              ),
              Tab(
                icon: Icon(Icons.add_circle_outline),
                text: "Create",
              ),
              Tab(
                icon: Icon(Icons.contact_support_outlined),
                text: "Support",
              )
            ],
          )),
      body: PageView(
        controller: _pController,
        scrollDirection: Axis.horizontal,
        onPageChanged: (value) {
          _page = value;
          _tController.animateTo(_page,
              duration: Duration(milliseconds: 300), curve: Curves.ease);
        },
        children: [
          MyPage(),
          ArtPage(),
          CreatePage(),
          SupportPage()
        ],
      ),
    );
  }
}
