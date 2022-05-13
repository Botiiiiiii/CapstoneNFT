package com.example.app.repository;

import com.example.app.model.Nft;
import com.example.app.model.User;
import com.example.app.model.UserInfo;

import java.util.ArrayList;

public interface JDBCRepository {
    int insertUser(User user);

    User getUser(UserInfo userInfo);

    ArrayList<Nft> getNftList();

    int addNft(Nft nft);
}
