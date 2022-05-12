package com.example.app.repository;

import com.example.app.model.Nft;
import com.example.app.model.User;
import com.example.app.model.UserInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class JDBCRepositoryImpl implements JDBCRepository {

    @Autowired
    SqlSession sqlSession;

    @Override
    public int insertUser(User user) {
        System.out.println(user.getNickname());
        return sqlSession.insert("user.insertUser", user);
    }

    @Override
    public User getUser(UserInfo userInfo){
        return sqlSession.selectOne("user.getUser", userInfo);
    }

    @Override
    public ArrayList<Nft> getNftList() {
        ArrayList<Nft> arrNft = new ArrayList<>(sqlSession.selectList("nft.getNftList"));
        return arrNft;
    }

    @Override
    public int addNft(Nft nft) {
        return sqlSession.insert("nft.insertNft", nft);
    }
}
