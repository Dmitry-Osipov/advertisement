package rf.senla.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.AdvertisementStatus;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.web.dto.CreateCommentRequest;
import rf.senla.web.dto.DeleteByIdRequest;
import rf.senla.web.dto.UpdateCommentRequest;
import rf.senla.web.utils.AdvertisementMapper;
import rf.senla.web.utils.UserMapper;

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
class RestCommentControllerTest {
    private User user;
    private Advertisement advertisement;
    @Autowired
    private MockMvc sut;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdvertisementMapper advertisementMapper;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("user123")
                .password("$2a$10$.PSEN9QPfyvpoXh9RQzdy.Wlok/5KO.iwcNYQOe.mmVgdTAeOO0AW")
                .phoneNumber("+7(123)456-78-90")
                .rating(0.0)
                .email("storm-yes@yandex.ru")
                .boosted(true)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();

        advertisement = Advertisement.builder()
                .id(1L)
                .user(user)
                .price(1000)
                .headline("Smartphone")
                .description("A portable device combining the functions of a mobile phone and a computer, typically " +
                        "offering internet access, touchscreen interface, and various applications.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getCommentsByAdvertisementIdReturnsCorrectData() {
        sut.perform(get("/api/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getCommentsByAdvertisementIdThrowsException() {
        sut.perform(get("/api/comments/0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void createReturnsCorrectData() {
        CreateCommentRequest comment =
                new CreateCommentRequest(advertisementMapper.toDto(advertisement), "Test Comment");
        String request = objectMapper.writeValueAsString(comment);

        sut.perform(post("/api/comments")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.advertisement").value(advertisementMapper.toDto(advertisement)))
                .andExpect(jsonPath("$.user").value(userMapper.toDto(user)))
                .andExpect(jsonPath("$.text").value("Test Comment"));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void createThrowsException() {
        CreateCommentRequest comment =
                new CreateCommentRequest(advertisementMapper.toDto(advertisement), "       ");
        String request = objectMapper.writeValueAsString(comment);

        sut.perform(post("/api/comments")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void updateReturnsCorrectData() {
        UpdateCommentRequest comment = new UpdateCommentRequest(3L, "Hello");
        String request = objectMapper.writeValueAsString(comment);

        sut.perform(put("/api/comments")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.text").value("Hello"))
                .andExpect(jsonPath("$.user").value(userMapper.toDto(user)))
                .andExpect(jsonPath("$.advertisement.id").value(2L));
    }

    @Test
    @SneakyThrows
    @WithMockUser("admin")
    void updateThrowsException() {
        UpdateCommentRequest comment = new UpdateCommentRequest(3L, "Hello");
        String request = objectMapper.writeValueAsString(comment);

        sut.perform(put("/api/comments")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void deleteByUserDoesNotThrowException() {
        DeleteByIdRequest comment = new DeleteByIdRequest(3L);
        String request = objectMapper.writeValueAsString(comment);

        sut.perform(delete("/api/comments")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deleted comment with id: " + comment.getId()));
    }

    @Test
    @SneakyThrows
    @WithMockUser(value = "admin", roles = "ADMIN")
    void deleteByAdminDoesNotThrowException() {
        DeleteByIdRequest comment = new DeleteByIdRequest(3L);
        String request = objectMapper.writeValueAsString(comment);

        sut.perform(delete("/api/comments")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deleted comment with id: " + comment.getId()));
    }

    @Test
    @SneakyThrows
    @WithMockUser("soccer_fanatic")
    void deleteByAnotherUserThrowsException() {
        DeleteByIdRequest comment = new DeleteByIdRequest(3L);
        String request = objectMapper.writeValueAsString(comment);

        sut.perform(delete("/api/comments")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }
}
