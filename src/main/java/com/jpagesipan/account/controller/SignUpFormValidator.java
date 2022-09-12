package com.jpagesipan.account.controller;


import com.jpagesipan.account.dto.SignUpForm;
import com.jpagesipan.account.reository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/*스프링은 been과 been을 통해서만 의존할 수있다.*/
@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // email nickname 중복 조회
        SignUpForm signUpForm =(SignUpForm) errors;
        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email","invalid.email",new Object[]{signUpForm.getEmail()},"이미 사용중인 이메일입니다." );
        }

        if (accountRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname","invalid.nickname",new Object[]{signUpForm.getNickname()},"이미 사용중인 닉네임입니다." );
        }
    }
}
