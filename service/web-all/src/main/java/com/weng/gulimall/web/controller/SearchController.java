package com.weng.gulimall.web.controller;

import com.weng.gulimall.common.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    @GetMapping("/list.html")
    public String search() {

        return "list/index";
    }
}
