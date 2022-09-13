package com.jpagesipan.account.controller;

import com.jpagesipan.account.domain.Account;
import com.jpagesipan.account.dto.SignUpForm;
import com.jpagesipan.account.reository.AccountRepository;
import com.jpagesipan.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;


    /*signUpForm 이라는 데인터를 받을 때 바인딩 설정*/
    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {

        /* 카멜케이스로 이름이 같으면  생략 가능*/
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    /*Spring 2.3 이상 부터는 validation 의존성을 따로 추가*/
    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }
        accountService.processNewAccount(signUpForm);


        //회원가입 처리
        return "redirect:/";
    }



}
