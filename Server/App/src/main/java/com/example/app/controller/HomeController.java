package com.example.app.controller;

import com.example.app.model.Nft;
import com.example.app.repository.JDBCRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
public class HomeController {
    @Autowired
    private JDBCRepository jdbcRepository;

    @PostMapping("/main")
    public ArrayList<Nft> getMainItem() {
        return jdbcRepository.getNftList();
    }


    @PostMapping("/minting")
    public void addNft(@RequestBody Nft nft) {
        System.out.println("nft");
        jdbcRepository.addNft(nft);
    }

}
