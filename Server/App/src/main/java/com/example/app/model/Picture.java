//package com.example.app.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "Picture")
//public class Picture {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "image_id", unique = true, nullable = false)
//    private Integer imageId;
//
//    @Column(nullable = false)
//    private String imageSrc;
//
//    @OneToOne
//    @JoinColumn(name = "address_id", nullable = false)
//    private Artist artist;
//
//    @Column(nullable = false)
//    private String title;
//
//    @Column(nullable = false)
//    private String content;
//
//    @Column()
//    private Integer favorite;
//}
