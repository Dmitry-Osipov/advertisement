package rf.senla.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.web.dto.ChangePasswordRequest;
import rf.senla.web.dto.UpdateUserRequest;
import rf.senla.web.utils.UserMapper;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Rollback
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("java:S4144")
class RestUserControllerTest {
    private User user;
    @Autowired
    private MockMvc sut;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("user123")
                .password("$2a$10$.PSEN9QPfyvpoXh9RQzdy.Wlok/5KO.iwcNYQOe.mmVgdTAeOO0AW")
                .phoneNumber("+7(123)456-78-90")
                .rating(0.0)
                .email("storm-yes@yandex.ru")
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getByUsernameReturnsCorrectData() {
        sut.perform(get("/api/users/" + user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(userMapper.toDto(user)));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getByUsernameThrowsException() {
        sut.perform(get("/api/users/test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getAllReturnsCorrectData() {
        sut.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    @SneakyThrows
    void updateReturnsCorrectData() {
        User currentUser = user;
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(currentUser, null, new ArrayList<>()));
        UpdateUserRequest data = new UpdateUserRequest();
        data.setId(user.getId());
        data.setUsername("testUser");
        data.setEmail("testEmail@example.com");
        data.setPhoneNumber("+7(953)180-00-61");
        String request = objectMapper.writeValueAsString(data);

        sut.perform(put("/api/users")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("testEmail@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+7(953)180-00-61"))
                .andExpect(jsonPath("$.rating").value(user.getRating()))
                .andExpect(jsonPath("$.role").value(String.valueOf(user.getRole())));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void updateThrowsException() {
        UpdateUserRequest data = new UpdateUserRequest();
        data.setId(user.getId());
        data.setUsername("test");
        data.setEmail("testEmail@example.com");
        data.setPhoneNumber("+7(953)180-00-61");
        String request = objectMapper.writeValueAsString(data);

        sut.perform(put("/api/users")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void deleteByUserDoesNotThrowException() {
        sut.perform(delete("/api/users/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deleted user with username: user123"));
    }

    @Test
    @SneakyThrows
    @WithMockUser(value = "admin", roles = "ADMIN")
    void deleteByAdminDoesNotThrowException() {
        sut.perform(delete("/api/users/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deleted user with username: " +
                        user.getUsername()));
    }

    @Test
    @SneakyThrows
    @WithMockUser("cool_guy")
    void deleteByUserThrowsException() {
        sut.perform(delete("/api/users/user123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void updatePasswordDoesNotThrowException() {
        ChangePasswordRequest data = new ChangePasswordRequest();
        data.setUsername(user.getUsername());
        data.setOldPassword("password123");
        data.setNewPassword("newPassword");
        String request = objectMapper.writeValueAsString(data);

        sut.perform(put("/api/users/password")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Password updated successfully"));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void updatePasswordWithIncorrectOldPasswordThrowsException() {
        ChangePasswordRequest data = new ChangePasswordRequest();
        data.setUsername(user.getUsername());
        data.setOldPassword("wrong_password");
        data.setNewPassword("newPassword");
        String request = objectMapper.writeValueAsString(data);

        sut.perform(put("/api/users/password")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("admin")
    void updatePasswordWithIncorrectUserThrowsException() {
        ChangePasswordRequest data = new ChangePasswordRequest();
        data.setUsername(user.getUsername());
        data.setOldPassword("password123");
        data.setNewPassword("newPassword");
        String request = objectMapper.writeValueAsString(data);

        sut.perform(put("/api/users/password")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser(value = "admin", roles = "ADMIN")
    void setRoleAdminDoesNotThrowException() {
        sut.perform(put("/api/users/admin/role-admin/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("The admin role is set to " + user.getUsername()));
    }

    @Test
    @SneakyThrows
    @WithMockUser(value = "admin", roles = "ADMIN")
    void setRoleAdminWithIncorrectDataThrowsException() {
        sut.perform(put("/api/users/admin/role-admin/test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void setRoleAdminWithRoleUserThrowsException() {
        sut.perform(put("/api/users/admin/role-admin/user123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("foodie")
    void addEvaluationReturnsCorrectData() {
        sut.perform(post("/api/users/rating")
                        .param("username", "user123")
                        .param("rating", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.role").value(String.valueOf(user.getRole())))
                .andExpect(jsonPath("$.rating").value(4.0));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void reAddingEvaluationThrowsException() {
        sut.perform(post("/api/users/rating")
                        .param("username", "soccer_fanatic")
                        .param("rating", "4"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void addEvaluationWithIncorrectDataThrowsException() {
        sut.perform(post("/api/users/rating")
                        .param("username", "user123")
                        .param("rating", "10"))
                .andExpect(status().isBadRequest());
    }
}
