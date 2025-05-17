package com.dialog.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping
    public String homePage() {
        return "홈페이지입니다.";
    }
}
