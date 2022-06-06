var express = require('express');
var router = express.Router();

// kas-caver 설정
const CaverExtKAS = require('caver-js-ext-kas');

const accessKeyId = "KASKRBFM59EAPBU18N42853W";
const secretAccessKey = "lvE-eEv4_O-eCApp5pHRFRp8U5mQcTc-bMBkwp8h";
const chainId = 1001;

const caver = new CaverExtKAS('https://api.baobab.klaytn.net:8651/')
caver.initKASAPI(chainId, accessKeyId, secretAccessKey)

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

function get_uri(imageSrc){

    console.log('get_uri');
    
    // return new Promise(function(){
    //     resolve('ipfs://testuri/testhash12345');
    // });

    return 'ipfs://testuri/testhash12345';

};

// minting
router.post('/mint', async function(req, res, next) {
    var to = req.body.to // query형식으로 받을 지, router parameter로 받을지 고민중임
    var owner = to;
    var creator = to;
    var description = req.body.desc;
    var title = req.body.title;
    var imageSrc = req.body.image;

    // var token_uri = req.body.uri;
    var token_uri = req.body.uri;

    var token_ID;

    // 타이틀 존재 여부 확인
    let [rows, field] = await con.promise().query('SELECT EXISTS (SELECT title FROM NFT where title = ?) as success', [title]);
    console.log('is title??: '+ rows[0].success);
    if(rows[0].success){
        console.log('duplicated title');
        res.send('duplicated title');

        return 0;
    }

    // 토큰 아이디 설정
    // try{
    //     [rows, field] = await con.promise().query('SELECT tokenId FROM NFT ORDER BY tokenId DESC LIMIT 1');
    //     token_ID = rows[0].tokenId+1;
    // }catch(e){
    //     token_ID = 1111111
    // }

    const tl = await caver.kas.kip17.getTokenList('sejong-nft')

    var items = tl.items;
    items = items.sort(function(a, b){
        if(parseInt(a['tokenId']) > parseInt(b['tokenId'])) return -1;
        if(parseInt(a['tokenId']) < parseInt(b['tokenId'])) return 1;
    });

    token_ID = parseInt(items[1]['tokenId'])+1;

    console.log('[token_ID] :' + token_ID);

    var token_info = [''+token_ID, title, description, imageSrc, owner, creator, 0];

    console.log(token_info);

    // KAS 민팅
    try{
        await caver.kas.kip17.mint('sejong-nft', to, token_ID, token_uri);
    }catch(e){
        console.log(e);
        return 0;
    }

    // db정보 갱신
    await con.promise().query('INSERT INTO NFT VALUES ?', [[token_info]]); 

    var result = {
        message: 'true',
        tokenid: token_ID,
        tokenuri: token_uri
    }

    res.send(result);
});

router.post('/:tokenid/test', async function(req, res, next){
    var result = {
        message: req.params.tokenid,
    }

    res.send(result);
})

// regist token sale
router.post('/:tokenid/regist', async function(req, res, next){
    // 판매등록

    // 가격, tokenid 설정해주기
    var tokenid = req.params.tokenid;
    var price = req.body.price;
    var from = req.body.from;

    var sale_info = [tokenid, price];

    // tokenid가 존재하는지 확인
    try{
        var [rows, field] = await con.promise().query('SELECT EXISTS (SELECT tokenId FROM NFT where tokenId = ?) as success', [tokenid]);
    }catch(e){
        console.log('[regist] '+e);

        result = {
            message: 'false',
            err: e.code
        }

        res.send(result);
        return 0;
    }

    if(!rows[0].success){
        console.log('[regist] no tokenid');

        result = {
            message: 'false',
            err: 'no tokenid'
        }

        res.send(result);
        return 0;
    }

    // from과 token 소유자가 일치하는지 확인해야됨
    try{
        var [rows, field] = await con.promise().query('SELECT * FROM NFT WHERE tokenId = ?', [tokenid]);
    }catch(e){
        console.log('[regist] '+e);

        result = {
            message: 'false',
            err: e.code
        }

        res.send(result);
        return 0;
    }

    console.log('[!]'+rows[0].owner);
    console.log('[!]'+from)
    console.log(rows[0].owner == from)

    if(rows[0].owner !== from){
        result = {
            message: 'false',
            err: 'tokenid error'
        }

        res.send(result);
        return 0;
    }

    // tokenid랑 price 업데이트
    try{
        await con.promise().query('INSERT INTO sale VALUES ?', [[sale_info]]);        
    }catch(e){
        console.log('[token_regist] err: ' + e);

        var result = {
            message: "false",
            err: e.code
        }

        res.send(result);
        return 0;
    }

    // 토큰 전송 권한 주기

    kip17instance = new caver.kct.kip17('0xad7be282f80720b230ff4a7222931183b1d10a1b');

    var pk;
    var [rows, field] = await con.promise().query('SELECT * FROM user WHERE address = ?', [from]);

    pk = rows[0].privateKey;

    console.log('[!]'+pk);

    const keyringContainer = new caver.keyringContainer();
    const keyring = await keyringContainer.keyring.createFromPrivateKey(pk);
    keyringContainer.add(keyring);

    console.log(keyring.address);

    kip17instance.setWallet(keyringContainer)

    await kip17instance.approve('0xda1DA25DDF16D2E89Ec50B22bA8609Ed610E3972', tokenid, {from: keyring.address});

    var result = {
        message: "true"
    }

    res.send(result);
})

// transfer token
// from, to, tokenid, price
// from이 구매자, to가 판매자?
router.post('/:tokenid/buy', async function(req, res, next){
    var from = req.body.from;
    var tokenid = req.params.tokenid;
    var price;
    var pk;

    kip17Instance = new caver.kct.kip17('0xad7be282f80720b230ff4a7222931183b1d10a1b');

    // set price
    try{
        var[rows, fields] = await con.promise().query('SELECT * FROM sale WHERE tokenId = ?', [tokenid]); 
    }catch(e){
        console.log(e);
        res.send(e.code);
    }
    price = rows[0].price;

    // set to
    try{
        to = await kip17Instance.ownerOf(tokenid);
    }catch(e){
        console.log(e);
        res.send(e.code);
    }

    // pay money
    // from의 개인키 가져오기,
    try{
        var [rows, fileds] = await con.promise().query('SELECT * FROM user WHERE address = ?', [from]);
        pk = rows[0].privateKey;
    }catch(e){
        console.log(e);
        res.send(e.code);
        return 0;
    }
    // from에서 to로 price만큼 지불
    const keyringContainer = new caver.keyringContainer();
    const keyring = await keyringContainer.keyring.createFromPrivateKey(pk);
    keyringContainer.add(keyring);
    const vt = await caver.transaction.valueTransfer.create({
        from: keyring.address,
        to: to,
        value: caver.utils.convertToPeb(price.toString(), 'KLAY'),
        gas: 25000,
    })

    const signed = await keyringContainer.sign(keyring.address, vt);

    const receipt = await caver.rpc.klay.sendRawTransaction(signed);
    console.log(receipt);

    // token transfer
    const result = await caver.kas.kip17.transfer('sejong-nft', '0xda1DA25DDF16D2E89Ec50B22bA8609Ed610E3972', to, from, tokenid)

    // sale 테이블에서 삭제
    try{
        var[rows, fields] = await con.promise().query('DELETE FROM sale WHERE tokenId = ?', [[tokenid]]); 
        await con.promise().query('UPDATE NFT SET owner = ? WHERE tokenId = ?', [from, tokenid])
        await con.promise().query('UPDATE NFT SET price = ? WHERE tokenId = ?', [price, tokenid])

        // transaction 테이블에 거래내역 추가
        // await con.promise().query('')

    }catch(e){
        console.log(e);
        res.send(e.code);
    }
	
	console.log(result)

    res.send({
        message: 'true'
    });
});

router.delete('/:tokenid/burn', async function(req, res, next){
    from = req.body.from;
    tokenid = req.params.tokenid;

    // 토큰 삭제 권한 from으로 부터 받기
    kip17instance = new caver.kct.kip17('0xad7be282f80720b230ff4a7222931183b1d10a1b');

    var pk;
    var [rows, field] = await con.promise().query('SELECT * FROM user WHERE address = ?', [from]);

    pk = rows[0].privateKey;
    // pk = '0x4af4f05b5b7e3cd0905feef1cd19a598a79ccbc206a68715f15823a45da15b0c';

    console.log('[!]'+pk);

    const keyringContainer = new caver.keyringContainer();
    const keyring = await keyringContainer.keyring.createFromPrivateKey(pk);
    keyringContainer.add(keyring);

    console.log(keyring.address);

    kip17instance.setWallet(keyringContainer)

    var result = await kip17instance.approve('0xda1DA25DDF16D2E89Ec50B22bA8609Ed610E3972', tokenid, {from: keyring.address});

    // console.log(result);

    try{
        // 토큰 삭제 하고
    // var result = await caver.kas.kip17.burn('sejong-nft', keyring.address, tokenid);
    var rc = await kip17instance.burn(tokenid, { from: keyring.address });

    }catch(e){
        console.log(e);

        res.send(e.code);
    }

    // NFT 테이블에서 토큰 삭제 해주기? or 업데이트 해주기
    // 삭제하면 현재 소유자가 아마 0x00000이 될거니까 업데이트 쪽으로 가도 될듯
    // 실제로 어떤식으로 저장되는지 확인해봐야됨
    try{
        var [rows, field] = await con.promise().query('DELETE FROM NFT WHERE tokenId = ?', [[tokenid]]);
    } catch(e){
        console.log(e);

        res.send(e.code);
    }

    result = {
        message: 'true',
        recipt: rc
    }

    res.send(result);
})

// token list
router.get('/nftlist', async function(req, res, next) {
    const result = await caver.kas.kip17.getTokenList('sejong-nft')

    console.log(result.items);

    var items = result.items;
    items.sort(function(a, b){
        if(parseInt(a['tokenId']) > parseInt(b['tokenId'])) return 1;
        if(parseInt(a['tokenId']) < parseInt(b['tokenId'])) return -1;
    })
	
	res.send(items);
});

router.get('/lasttoken', async function(req, res, next){
    const result = await caver.kas.kip17.getTokenList('sejong-nft')

    var items = result.items;
    items = items.sort(function(a, b){
        if(parseInt(a['tokenId']) > parseInt(b['tokenId'])) return -1;
        if(parseInt(a['tokenId']) < parseInt(b['tokenId'])) return 1;
    })

    res.send({
        tokenId : parseInt(items[1]['tokenId'])
    });
})

router.get('/list', async function(req, res, next){
    try{
        var [rows, field] = await con.promise().query("SELECT NFT.tokenId, NFT.title, NFT.description, NFT.imageSrc, (SELECT user.nickname FROM user WHERE user.address = NFT.owner) as owner, (SELECT user.nickname FROM user WHERE user.address = NFT.creator)  as creator, sale.price from NFT JOIN sale ON NFT.tokenId = sale.tokenId");
    }catch(e){
        console.log(e);
        res.send(e.code)
    }

    console.log(JSON.stringify(rows));
    console.log(rows);

    res.send({
        token: rows
    });
})

// token info
// router.get('/:tokenid/info', async function(req, res, next) {
//     const result = await caver.kas.kip17.getToken('sejong-nft', req.params.tokenid);
	
// 	res.send(result);
// });

router.get('/:tokenid/info', async function(req, res, next){
    var tokenId = req.params.tokenid;

    try{
        var [rows, field] = await con.promise().query('SELECT NFT.tokenId, NFT.title, NFT.description, NFT.imageSrc, (SELECT user.nickname FROM user WHERE user.address = NFT.owner) as owner, (SELECT user.nickname FROM user WHERE user.address = NFT.creator)  as creator, sale.price from NFT LEFT OUTER JOIN sale ON NFT.tokenId = sale.tokenId WHERE NFT.tokenId = ?', [tokenId]);
    }catch(e){
        console.log(e);
        res.send(e.code);
    }

    res.send({
        token: rows[0]
    });
})

module.exports = router;