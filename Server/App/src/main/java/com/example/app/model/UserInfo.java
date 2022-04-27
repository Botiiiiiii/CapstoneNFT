package com.example.app.model;

public class UserInfo {
    private String nickname;
    private String pw;

    public UserInfo(String nickname, String pw){
        this.nickname = nickname;
        this.pw = pw;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPw() {
        return pw;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
