var express = require('express');
var router = express.Router();

// kas-caver 설정
const CaverExtKAS = require('caver-js-ext-kas');

const accessKeyId = "KASKRBFM59EAPBU18N42853W";
const secretAccessKey = "lvE-eEv4_O-eCApp5pHRFRp8U5mQcTc-bMBkwp8h";
const chainId = 1001;

const caver = new CaverExtKAS()
caver.initKASAPI(chainId, accessKeyId, secretAccessKey)

// db 설정
const mysql = require('mysql');
var con = mysql.createConnection({
    host: 'ec2-18-220-138-199.us-east-2.compute.amazonaws.com',
    user: 'user',
    password: 'User123!',
    database: 'nft'
});

con.connect((err) => {
    if(err) throw err;
    console.log('connect');
});

// middleware that is specific to this router
router.use(function funtion(req, res, next) {
    // 기능 넣기

    next();
});

/* GET home page. */
router.get('/', function(req, res, next) {
    
});

module.exports = router;