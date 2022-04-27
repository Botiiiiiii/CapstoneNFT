package com.example.app.controller;

import com.example.app.config.CaverConfig;
import com.example.app.model.User;
import com.example.app.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.app.repository.JDBCRepository;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.Account;

@RequiredArgsConstructor
@RestController
public class UserControler {
    @Autowired
    private JDBCRepository jdbcRepository;

    @PostMapping("/regist")
    public Integer Regist(@RequestBody UserInfo userInfo) throws ApiException {
        CaverExtKAS caver = new CaverConfig().caverConfig();
        Account account = caver.kas.wallet.createAccount();

        User user = new User(account.getAddress(), account.getPublicKey(), userInfo.getNickname(), 0, "", userInfo.getPw());
        Integer result = jdbcRepository.insertUser(user);

        return result;
    }

    @PostMapping("/login")
    public User Login(@RequestBody UserInfo userInfo) throws ApiException {
        return jdbcRepository.getUser(userInfo);
    }

}
