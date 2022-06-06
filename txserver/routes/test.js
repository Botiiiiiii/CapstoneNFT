var express = require('express');
var router = express.Router();
var path = require("path");
var fs = require("fs");

let AWS = require("aws-sdk");
AWS.config.loadFromPath(__dirname + "/../config/awsconfig.json"); // 인증
let s3 = new AWS.S3();

let multer = require("multer");
let multerS3 = require('multer-s3');
let upload = multer({

    storage: multerS3({
        s3: s3,
        bucket: "sejong-nft",
        key: function (req, file, cb) {
             let extension = path.extname(file.originalname);
             cb(null, Date.now().toString() + extension)
        }
    })

})

// let upload = multer();

const uploadFile = async (fileName, res) =>{
    const content = {
        title: 'title',
        description: 'test',
        image: fileName
    }

    fs.writeFileSync('text.txt', JSON.stringify(content), 'utf8');
    const fileContent = fs.readFileSync('text.txt');

    const params = {
        Bucket: 'sejong-nft',
        Key: Date.now().toString()+'.json',
        Body: fileContent
    };

    s3.upload(params, function(err, data){
        if(err){
            throw err;
        }
        console.log(`file upload success, ${data.Location}`);

        var result = {
            message: 'true',
            uri: data.Location
        }
    
        res.send(result);

        // return data.Location;
    });

    // var test = s3.upload(params);

    // console.log(test);
}

// middleware that is specific to this router
// router.use(function funtion(req, res, next) {
//     // 기능 넣기

//     next();
// });

/* GET home page. */
router.get('/', function(req, res, next) {
    
});

router.post('/upload', upload.single("upload"), async function(req, res, next){   
    // upload.single("imgFile")

    console.log(req.file)

    try{
        var uri = await uploadFile(req.file.location, res);
    }catch(e){
        // res.send('fail');
        console.log(e);
        res.send('fail');
    }

    // var result = {
    //     message: 'true',
    //     uri: uri
    // }

    // res.send(result);
})

// router.post('/upload', upload.single("file"), async function(req, res, next){
//     console.log(req.body);
//     console.log(req.headers);
//     console.log(req.file);
//     console.log(req.params.file);

//     res.send('hello');
// })

module.exports = router;