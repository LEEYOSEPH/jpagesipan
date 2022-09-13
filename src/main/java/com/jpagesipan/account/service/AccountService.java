package com.jpagesipan.account.service;

import com.jpagesipan.account.domain.Account;
import com.jpagesipan.account.dto.SignUpForm;
import com.jpagesipan.account.reository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    public void processNewAccount(SignUpForm signUpForm) {

        Account newAccount = saveNewAccount(signUpForm);
        //토큰값 생성
        newAccount.generateEmailCheckToken();
        sendSignUpConformEmail(newAccount);
    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword())) // encoding 필요
                .studyCreateByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();
        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    private void sendSignUpConformEmail(Account newAccount) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setText("게시판, 회원가입인증");
        simpleMailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        javaMailSender.send(simpleMailMessage);
    }


}
