package com.example.app.model;

import java.util.ArrayList;

public class Main {
    private ArrayList<User> arrUser;
    private ArrayList<Nft> arrNft;

    public Main(){}

    public Main(ArrayList<User> arrUser, ArrayList<Nft> arrNft){
        this.arrUser = arrUser;
        this.arrNft = arrNft;
    }

    public ArrayList<User> getArrUser() {
        return arrUser;
    }

    public void setArrUser(ArrayList<User> arrUser) {
        this.arrUser = arrUser;
    }

    public ArrayList<Nft> getArrNft() {
        return arrNft;
    }

    public void setArrNft(ArrayList<Nft> arrNft) {
        this.arrNft = arrNft;
    }
}
