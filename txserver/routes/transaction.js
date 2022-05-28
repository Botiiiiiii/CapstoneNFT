var express = require('express');
var router = express.Router();

// kas-caver 설정
// const CaverExtKAS = require('caver-js-ext-kas');

// const accessKeyId = "KASKRBFM59EAPBU18N42853W";
// const secretAccessKey = "lvE-eEv4_O-eCApp5pHRFRp8U5mQcTc-bMBkwp8h";
// const chainId = 8217;

// const caver = new CaverExtKAS()
// caver.initKASAPI(chainId, accessKeyId, secretAccessKey)

// caver 설정
const Caver = require('caver-js');
const caver = new Caver('https://public-node-api.klaytnapi.com/v1/cypress');
// const caver = new Caver('https://api.baobab.klaytn.net:8651');

// middleware that is specific to this router
router.use(function funtion(req, res, next) {
    // 기능 넣기

    next();
});

/* GET home page. */
router.get('/:txhash/test', async function(req, res, next) {
    console.log(req.params.txhash);

    // const myContract = new caver.contract('0x{address in hex}')
    // caver.contract.decodeFunctionCall().then(console.log);
    // const contract = caver.contract.create([], )

    // var result = await caver.rpc.klay.getTransactionByHash(req.params.txhash);

    // var result = await caver.contract.findContractMethodBySignature('0xbbbfa60c')

    // var result = await caver.abi.decodeFunctionCall({
    //     name: 'function1',
    //     type: 'function',
    //     inputs: [{
    //         type: 'address',
    //         name: 'from'
    //     },{
    //         type: 'address',
    //         name: 'to'
    //     },{
    //         type: 'uint256',
    //         name: 'value'
    //     }]
    // }, '0x42842e0e000000000000000000000000921f6b1ddab9ef01c70efac18e842c3534c23d0d000000000000000000000000921f6b1ddab9ef01c70efac18e842c3534c23d0d000000000000000000000000000000000000000000000000000000000001b20d');

    var result = await caver.abi.decodeParameters(['address', 'address', 'uint256'], '0x000000000000000000000000921f6b1ddab9ef01c70efac18e842c3534c23d0d000000000000000000000000921f6b1ddab9ef01c70efac18e842c3534c23d0d000000000000000000000000000000000000000000000000000000000001b20d');


    console.log(result);

    res.send(result);
});

module.exports = router;