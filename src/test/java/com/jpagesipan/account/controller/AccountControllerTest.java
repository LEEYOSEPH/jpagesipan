package com.jpagesipan.account.controller;

import com.jpagesipan.account.reository.AccountRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private AccountRepository accountRepository;

    @MockBean
    JavaMailSender javaMailSender;


    @DisplayName("회원가입 화면 보이는 테스트")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @DisplayName("회원 가입 처리- 입력값 오류")
    @Test
    void signUpSubmit_with_Wrong_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "yoseph")
                        .param("email", "email..")
                        .param("password", "1234")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));

    }

    @DisplayName("회원 가입 처리- 입력값 저장")
    @Test
    void signUpSubmit_with_Correct_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "yoseph")
                        .param("email", "email@email.com")
                        .param("password", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        assertTrue(accountRepository.existsByEmail("email@email.com"));
    }
}