package com.example.app.repository;

import com.example.app.model.User;
import com.example.app.model.UserInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCRepositoryImpl implements JDBCRepository {

    @Autowired
    SqlSession sqlSession;

    @Override
    public int insertUser(User user) {
        System.out.println(user.getNickname());
        return sqlSession.insert("user.insertUser", user);
    }

    public User getUser(UserInfo userInfo){
        return sqlSession.selectOne("user.getUser", userInfo);
    }
}
