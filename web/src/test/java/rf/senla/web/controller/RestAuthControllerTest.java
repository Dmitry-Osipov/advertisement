package rf.senla.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.web.dto.ForgotPasswordRequest;
import rf.senla.web.dto.ResetPasswordRequest;
import rf.senla.web.dto.SignInRequest;
import rf.senla.web.dto.SignUpRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Rollback
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class RestAuthControllerTest {
    @Autowired
    private MockMvc sut;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void signUpReturnsCorrectData() {
        SignUpRequest user = new SignUpRequest();
        user.setUsername("new_user");
        user.setPassword("pass_for_new_user");
        user.setEmail("example@example.com");
        user.setPhoneNumber("+7(777)777-77-77");
        String request = objectMapper.writeValueAsString(user);

        sut.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    @SneakyThrows
    void signUpWithDuplicateDataThrowsException() {
        SignUpRequest user = new SignUpRequest();
        user.setUsername("cool_guy");
        user.setPassword("pass_for_new_user");
        user.setEmail("example@example.com");
        user.setPhoneNumber("+7(777)777-77-77");
        String request = objectMapper.writeValueAsString(user);

        sut.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void signUpWithIncorrectDataThrowsException() {
        SignUpRequest user = new SignUpRequest();
        user.setUsername("new");
        user.setPassword("pass_for_new_user");
        user.setEmail("example@example.com");
        user.setPhoneNumber("+7(777)777-77-77");
        String request = objectMapper.writeValueAsString(user);

        sut.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void signInReturnsCorrectData() {
        SignInRequest user = new SignInRequest();
        user.setUsername("cool_guy");
        user.setPassword("secretPass");
        String request = objectMapper.writeValueAsString(user);

        sut.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    @SneakyThrows
    void signInWithIncorrectDataThrowsException() {
        SignInRequest user = new SignInRequest();
        user.setUsername("cool_guy");
        user.setPassword("wrong_password");
        String request = objectMapper.writeValueAsString(user);

        sut.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void forgotPasswordDoesNotThrowException() {
        ForgotPasswordRequest user = new ForgotPasswordRequest("soccer_fanatic", "storm-yes@yandex.ru");
        String request = objectMapper.writeValueAsString(user);

        sut.perform(post("/api/auth/password/forgot")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Reset password email has been sent"));
    }

    @Test
    @SneakyThrows
    void forgotPasswordWithIncorrectDataThrowsException() {
        ForgotPasswordRequest user = new ForgotPasswordRequest("testuser", "storm-yes@yandex.ru");
        String request = objectMapper.writeValueAsString(user);

        sut.perform(post("/api/auth/password/forgot")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void resetPasswordWithIncorrectDataThrowsException() {
        ResetPasswordRequest user = new ResetPasswordRequest("newSecr3tPassword");
        String request = objectMapper.writeValueAsString(user);

        sut.perform(post("/api/auth/password/reset")
                        .contentType("application/json")
                        .content(request)
                        .param("token", "wrong_token"))
                .andExpect(status().isBadRequest());
    }
}
