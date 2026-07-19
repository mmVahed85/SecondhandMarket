package com.secondhand.controller;

import com.secondhand.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping
    public String test() {
        return testService.test();
    }

}