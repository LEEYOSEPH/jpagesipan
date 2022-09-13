package com.jpagesipan.account.controller;

import com.jpagesipan.account.domain.Account;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired  AccountRepository accountRepository;

    @MockBean
    JavaMailSender javaMailSender;

    @DisplayName("인증 메일 확인 - 입력값 오류")
    @Test
    void checkEmailToken_with_wrong_input() throws Exception {
        mockMvc.perform(get("/check-email-token")
                        .param("token","sdfgwedfds")
                        .param("email","email@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"));
    }

    @DisplayName("인증 메일 확인 - 입력값 정상")
    @Test
    void checkEmailToken() throws Exception {

        Account account = Account.builder()
                .email("email@email.com")
                .password("12345678")
                .nickname("geee")
                .build();
        Account save = accountRepository.save(account);
        save.generateEmailCheckToken();

        mockMvc.perform(get("/check-email-token")
                        .param("token",save.getEmailCheckToken())
                        .param("email",save.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("account/checked-email"));
    }


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

        Account account = accountRepository.findByEmail("email@email.com");
        assertNull(account);
        assertNotEquals(account.getPassword(),"12345678");
        assertTrue(accountRepository.existsByEmail("email@email.com"));
    }
}