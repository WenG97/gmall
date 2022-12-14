package com.weng.gulimall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("login.html")
    public String loginPage(@RequestParam(value = "originUrl",required = false,defaultValue = "http://gmall.com/")String originUrl,
                            Model model){
        model.addAttribute("originUrl",originUrl);

        return "login";
    }
}
