import 'dart:convert';

import 'package:flutter/cupertino.dart';

class TestImageModel {
  final String name;
  final Image image;

  TestImageModel(this.name, this.image);

  TestImageModel.fromJson(Map<String, dynamic> json)
      : name = json['name'],
        image = json['image'];

  Map<String, dynamic> toJson() => {
    'name': name,
    'image': image,
  };
}

// Map<String, dynamic> userMap = jsonDecode(jsonString);
// // jsonDecode(jsonString);
// var testImageModel = TestImageModel.fromJson(userMap);