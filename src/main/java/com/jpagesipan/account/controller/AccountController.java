package com.jpagesipan.account.controller;

import com.jpagesipan.account.dto.SignUpForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        
        /* 카멜케이스로 이름이 같으면  생략 가능*/
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }
}
