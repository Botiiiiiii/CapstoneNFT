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
// middleware that is specific to this router
// router.use(function funtion(req, res, next) {
//     // 기능 넣기

//     next();
// });

/* GET home page. */
router.post('/login', async function(req, res, next) {
    var name;
    var pw;
    var pk;
    var profile;
    var token_list;
    var token_sale;

    try{
        var [rows, field] = await con.promise().query('SELECT * FROM user WHERE nickname = ?', [req.body.name]);

        // 차단된 유저 확인
        if(rows[0].alert != 0){
            res.send({
                message: 'blocked user',
                alert: rows[0].alert
            });
            return 0;
        }
    } catch(e){
        console.log(e);

        var result = {
            message: 'false',
            err: e.code
        }
        
        res.send(result);
        return 0;
    }

    try{
        name = rows[0].nickname;
        pw = rows[0].pw;
        pk = rows[0].privateKey;
        addr = rows[0].address;
        profile = rows[0].profile_img;
    } catch(e){
        console.log(e);

        var result = {
            message: 'false',
            err: e.code
        }

        res.send(result);
        return 0;
    } 

    try{
        var [rows, field] = await con.promise().query('SELECT NFT.tokenId, NFT.title, NFT.description, NFT.imageSrc, (SELECT user.nickname FROM user WHERE user.address = NFT.owner) as owner, (SELECT user.nickname FROM user WHERE user.address = NFT.creator)  as creator, sale.price from NFT INNER JOIN sale ON NFT.tokenId = sale.tokenId WHERE NFT.owner = ?', [addr]);
        token_sale = rows;

        var [rows, field] = await con.promise().query('SELECT NFT.tokenId, NFT.title, NFT.description, NFT.imageSrc, (SELECT user.nickname FROM user WHERE user.address = NFT.owner) as owner, (SELECT user.nickname FROM user WHERE user.address = NFT.creator)  as creator, sale.price from NFT LEFT OUTER JOIN sale ON NFT.tokenId = sale.tokenId WHERE NFT.owner = ?', [addr]);
        token_list = rows;

    }catch(e){
        console.log(e);

        var result = {
            message: 'false',
            err: e.code
        }

        res.send(result);
        return 0;
    }

    try{
        tokens = rows;
    }catch(e){
        console.log(e);

        var result = {
            message: 'false',
            err: e.code
        }

        res.send(result);
        return 0;
    }

    if(req.body.pw == pw){
        // 로그인 성공 시

        // token update

        var result = {
            message: 'true',
            name: name,
            address: addr,
            privatekey: pk,
            profile_img: profile,
            token_list: token_list,
            token_sale: token_sale
        }

        res.send(result);
    }
    else{
        var result = {
            message: 'false'
        }

        res.send(result);
    }
});

router.post('/regist', async function(req, res, next) {
    var name = req.body.name;
    var pw = req.body.pw;
    var fcm = req.body.fcm;
    var address;
    var privatekey;

    // 중복 아이디 있는지 확인할거면 여기서
    // 여기서 안해도 아래서 오류 출력됨

    // 키링 생성
    const keyring = await caver.wallet.keyring.generate();

    address = keyring.address;
    privatekey = keyring.key.privateKey;

    console.log(privatekey);

    // db에 입력
    // fcm 추가해야댐
    var params;

    params = [address, address, name, 0, pw, privatekey];

    try{
        await con.promise().query('INSERT INTO user(address, publicKey, nickname, alert, pw, privateKey) VALUES ?', [[params]]);
    } catch(e){
        console.log(e);

        var result = {
            message: 'false',
            err: e.code
        }

        res.send(result);
        return 0;

    };

    // 로그인 성공 시
    var result = {
        message: 'true',
        name: name,
        address: address,
        privatekey: privatekey
    }

    res.send(result);
});

router.get('/:name/balance', async function(req, res, next){
    var name = req.params.name;

    try{
        var [rows, field] = await con.promise().query('SELECT * FROM user WHERE nickname = ?', [name]);

        var addr = rows[0].address
    }catch(e){
        console.log(e);
    }

    var peb = await caver.rpc.klay.getBalance(addr);
    console.log(peb);
    klay = peb/(10**18);
    console.log(klay);

    result = {
        peb: peb,
        klay: klay
    }

    res.send(result);
})

router.post('/:name/profile', async function(req, res, next){
    var name = req.params.name;
    var profile = req.body.profile_img;

    try{
        await con.promise().query('UPDATE user SET profile_img = ? WHERE nickname = ?', [profile, name]);
    }catch(e){
        console.log(e);

        res.send({
            error: e.code
        })
        return 0;
    }

    res.send({
        message: 'true'
    });
})

router.get('/:name/info', async function(req, res, next){
    var name = req.params.name;
    var userinfo
    var token_list
    var token_sale

    try{
        var [rows, field] = await con.promise().query('SELECT address, nickname, profile_img FROM user WHERE nickname = ?', [name]);
        userinfo = rows[0];

        var [rows, field] = await con.promise().query('SELECT NFT.tokenId, NFT.title, NFT.description, NFT.imageSrc, (SELECT user.nickname FROM user WHERE user.address = NFT.owner) as owner, (SELECT user.nickname FROM user WHERE user.address = NFT.creator)  as creator, sale.price from NFT INNER JOIN sale ON NFT.tokenId = sale.tokenId WHERE NFT.owner = ?', [userinfo.address]);
        token_sale = rows;

        var [rows, field] = await con.promise().query('SELECT NFT.tokenId, NFT.title, NFT.description, NFT.imageSrc, (SELECT user.nickname FROM user WHERE user.address = NFT.owner) as owner, (SELECT user.nickname FROM user WHERE user.address = NFT.creator)  as creator, NFT.price from NFT LEFT OUTER JOIN sale ON NFT.tokenId = sale.tokenId WHERE NFT.owner = ?', [userinfo.address]);
        token_list = rows;
    }catch(e){
        console.log(e);
        res.send(e.code);
        return 0;
    }

    res.send({
        userinfo: userinfo,
        token_list: token_list,
        token_sale: token_sale
    })
})

router.post('/test', async function(req, res){
    // const keyring = await caver.wallet.keyring.generate();

    // console.log(keyring.address);
    // console.log(keyring.key.privateKey);

    var result = {
        message: req.body.test,
        
    }

    res.send(result);
});

module.exports = router;