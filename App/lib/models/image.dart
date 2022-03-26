class ImageData {
   String? con;
   String? cr;
   int? cs;
   String? ct;
   String? et;
   String? lt;
   String? pi;
   String? ri;
   String? rn;
   int? st;
   int? ty;

  ImageData(
      {this.con,
      this.cr,
      this.cs,
      this.ct,
      this.et,
      this.lt,
      this.pi,
      this.ri,
      this.rn,
      this.st,
      this.ty});

  factory ImageData.fromJson(Map<String, dynamic> json) {
    return ImageData(
        con: json['con'],
        cr: json['cr'],
        cs: json['cs'],
        ct: json['ct'],
        et: json['et'],
        lt: json['lt'],
        pi: json['pi'],
        ri: json['ri'],
        rn: json['rn'],
        st: json['st'],
        ty: json['ty']);
  }
}

class _MyAppState extends State<MyApp> {
  Future<ImageData> futureImage;

  @override
  void initState() {
    super.initState();
    futureImage = fetchImage();
  }

  Future<ImageData> fetchImage() async {
    final response = await http.get('https://jsonplaceholder.typicode.com/albums/1');

    if (response.statusCode == 200) {
      // If the server did return a 200 OK response,
      // then parse the JSON.
      return ImageData.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed to load album');
    }
  }
}