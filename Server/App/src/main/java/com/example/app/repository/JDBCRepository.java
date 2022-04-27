package com.example.app.repository;

import com.example.app.model.User;
import com.example.app.model.UserInfo;

public interface JDBCRepository {
    int insertUser(User user);

    User getUser(UserInfo userInfo);
}
