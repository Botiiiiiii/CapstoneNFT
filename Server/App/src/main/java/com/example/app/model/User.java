package com.example.app.model;

public class User {

    private String address;
    private String publicKey;
    private String nickname;
    private Integer alert = 0;
    private String token;
    private String pw;

    public User(){}

    public User(String address,
                String publicKey,
                String nickname,
                Integer alert,
                String token,
                String pw) {
        this.address = address;
        this.publicKey = publicKey;
        this.nickname = nickname;
        this.alert = alert;
        this.token = token;
        this.pw = pw;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setAlert(Integer alert) {
        this.alert = alert;
    }

    public Integer getAlert() {
        return alert;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPw() {
        return pw;
    }
}
