package com.example.app.model;

public class Nft {
    private Integer tokenId;
    private String title;
    private String description;
    private String imageSrc;
    private String owner;
    private String creator;

    public Nft(){}

    public Nft(Integer tokenId,
               String title,
               String description,
               String imageSrc,
               String owner,
               String creator) {
        this.tokenId = tokenId;
        this.title = title;
        this.description = description;
        this.imageSrc = imageSrc;
        this.owner = owner;
        this.creator = creator;
    }

    public Integer getTokenId() {
        return tokenId;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
