var express = require('express');
var router = express.Router();
var path = require("path");
var fs = require("fs");

// db 설정
// const mysql = require('mysql2');
// const { createCipheriv } = require('crypto');
// const { normalize } = require('node:path/win32');
// const { profile } = require('console');
// var con = mysql.createConnection({
//     host: 'ec2-18-220-138-199.us-east-2.compute.amazonaws.com',
//     user: 'user',
//     password: 'User123!',
//     database: 'nft'
// });

// con.connect((err) => {
//     if(err) throw err;
//     console.log('connect');
// });

const CaverExtKAS = require('caver-js-ext-kas');

const accessKeyId = "KASKRBFM59EAPBU18N42853W";
const secretAccessKey = "lvE-eEv4_O-eCApp5pHRFRp8U5mQcTc-bMBkwp8h";
const chainId = 1001;

const caver = new CaverExtKAS('https://api.baobab.klaytn.net:8651/')
caver.initKASAPI(chainId, accessKeyId, secretAccessKey)

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

});

let storage = multer.memoryStorage();
let image_file = multer({
    storage, 
    limits: { 
        fileSize: 52428800
    } 
});

const uploadFile = async (fileName, title, desc, creator, res) =>{
    const content = {
        title: title,
        description: desc,
        creator: creator,
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
            image: fileName,
            uri: data.Location            
        }
    
        res.send(result);

        // return data.Location;
    });
}

/* GET home page. */
router.get('/', function(req, res, next) {
    
});

router.post('/image', image_file.single('upload'), async function(req, respond, next){
    var image = req.file;
    var token_id;
    var score;
    var sim_token_id;
    
    // try{
    //     [rows, field] = await con.promise().query('SELECT tokenId FROM NFT ORDER BY tokenId DESC LIMIT 1');
    //     token_id = rows[0].tokenId+1;
    // }catch(e){
    //     token_id = 1111111;
    // }

    const tl = await caver.kas.kip17.getTokenList('sejong-nft')

    var items = tl.items;
    items = items.sort(function(a, b){
        if(parseInt(a['tokenId']) > parseInt(b['tokenId'])) return -1;
        if(parseInt(a['tokenId']) < parseInt(b['tokenId'])) return 1;
    });

    token_id = parseInt(items[1]['tokenId'])+1;

    console.log(token_id);

    const FormData = require("form-data");
    const axios = require("axios");
    
    var sendingData = new FormData();

    fs.writeFileSync('test.png', image.buffer);

    sendingData.append('file', fs.createReadStream("test.png"));
    sendingData.append('token_id', token_id);

    axios({
        method: "post",
        url: "http://112.187.174.215:11962/scoring",
        data: sendingData,
        headers: { ...sendingData.getHeaders() }
    }).then(res => {
        score = res.data.score;
        sim_token_id = res.data.token_id

        console.log('[score]', score);

        if(score > 30){
            console.log('log');
    
            respond.send({
                message: 'false',
                score: score,
                sim_token_id: sim_token_id,
                imageUrl: 'false'
            })
        }
        else{
            console.log('log2');
            sendingData = new FormData();
    
            sendingData.append('upload', fs.createReadStream("test.png"));
    
            var result;
    
            axios({
                method: "post",
                url: "http://3.36.62.18:3000/upload/upload",
                data: sendingData,
                headers: { ...sendingData.getHeaders() }
            }).then(res => {
                result = {
                    message: 'true',
                    score: score,
                    sim_token_id: sim_token_id,
                    imageUrl: res.data.imageUrl
                }

                respond.send(result);
            });
        }
    });
})

router.post('/upload', upload.single('upload'), async function(req, res, next){
    var result = {
        message: 'true',
        imageUrl: req.file.location
    };

    res.send(result);
})

router.post('/uri', async function(req, res, next){
    var title = req.body.title;
    var desc = req.body.desc;
    var creator = req.body.creator;
    var location = req.body.image;

    console.log({
        title: title,
        creator: creator,
        desc: desc
    })
    
    try{
        var uri = await uploadFile(location, title, desc, creator, res);
    }catch(e){
        // res.send('fail');
        console.log(e);
        res.send('fail');
    }
})

module.exports = router;