package com.example.app.controller;

import com.example.app.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class HelloController {

    @PostMapping("/images")
    public UserInfo upload(@RequestBody UserInfo user) {
        return user;
    }
}