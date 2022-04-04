package com.example.app.controller;

//import com.example.app.model.Artist;
//import com.example.app.model.Picture;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class GalleryController {

    @GetMapping("/test")
    public String getGallery(){
//        ArrayList<Picture> testData = new ArrayList<>();
//        Picture picture = new Picture();
//        Artist artist = new Artist();
//        artist.setArtistId(102120130);
//        artist.setName("testName");
//        picture.setContent("testContent");
//        picture.setArtist(artist);
//        picture.setFavorite(0);
//        picture.setTitle("testTitle");
//        picture.setImageSrc("testSrc");
//        picture.setImageId(12013);
//        testData.add(picture);
//        testData.add(picture);
//        testData.add(picture);
//        testData.add(picture);
//        testData.add(picture);
//        testData.add(picture);
//        testData.add(picture);
//        testData.add(picture);
//        testData.add(picture);
//        testData.add(picture);
//        return testData;
        return "0000000";
    }
}
