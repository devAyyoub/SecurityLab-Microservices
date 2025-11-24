package com.skistation.universityhousingms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/universityhousing")
public class UniversityHousingController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
