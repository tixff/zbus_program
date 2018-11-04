package com.ti.zbus_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/chart")
    public String chart() {
        return "chart";
    }

    @GetMapping("/")
    public String manager(){
        return "topic";
    }
}
