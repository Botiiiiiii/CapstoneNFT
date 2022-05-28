var express = require('express');
var router = express.Router();

// kas-caver 설정
const CaverExtKAS = require('caver-js-ext-kas');
const caver = new CaverExtKAS()
caver.initKASAPI(1001, "KASKRBFM59EAPBU18N42853W", "lvE-eEv4_O-eCApp5pHRFRp8U5mQcTc-bMBkwp8h")

// kip17instance = new caver.kct.kip17('0xad7be282f80720b230ff4a7222931183b1d10a1b');

const mysql = require('mysql2');
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



router.post('/mint', async function(req, res, next) {
});

router.post('/:tokenid/regist', async function(req, res, next) {
});

router.post('/:tokenid/buy', async function(req, res, next) {
});

router.post('/:tokenid/burn', async function(req, res, next) {
});

// token list
router.get('/list', async function(req, res, next) {
    const result = await caver.kas.kip17.getTokenList('sejong-nft')
	
	res.send(result);
});

// token info
router.get('/:tokenid/info', async function(req, res, next) {
    const result = await caver.kas.kip17.getToken('sejong-nft', req.params.tokenid);
	
	res.send(result);
});

module.exports = router;