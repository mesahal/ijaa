package com.iitju.ijaa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String test() {
        return "home.html";
    }

}
