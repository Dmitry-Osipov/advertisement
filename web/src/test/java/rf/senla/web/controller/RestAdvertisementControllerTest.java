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
import rf.senla.web.dto.CreateAdvertisementRequest;
import rf.senla.web.dto.UpdateAdvertisementRequest;
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
@SuppressWarnings({"java:S4144", "java:S1117"})
class RestAdvertisementControllerTest {
    private User user;
    private Advertisement advertisement;
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
    void getAllReturnsCorrectData() {
        sut.perform(get("/api/advertisements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getAllByPriceAndHeadlineReturnsCorrectData() {
        sut.perform(get("/api/advertisements/filter")
                        .param("min", "500")
                        .param("max", "5000")
                        .param("headline", "smartphone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getAllByPriceAndHeadlineWithoutHeadlineReturnsCorrectData() {
        sut.perform(get("/api/advertisements/filter")
                        .param("min", "5000")
                        .param("max", "7000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getAllByPriceAndHeadlineWithIncorrectDataThrowsException() {
        sut.perform(get("/api/advertisements/filter")
                        .param("min", "5000")
                        .param("max", "1000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getAllByPriceAndHeadlineWithoutThrowsException() {
        sut.perform(get("/api/advertisements/filter")
                        .param("headline", "smartphone"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getAllByUserReturnsCorrectData() {
        createReturnsCorrectData();

        sut.perform(get("/api/advertisements/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void getAllByUserWithOnlyActiveStatusReturnsCorrectData() {
        createReturnsCorrectData();

        sut.perform(get("/api/advertisements/user123")
                        .contentType("application/json")
                        .param("active", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void createReturnsCorrectData() {
        CreateAdvertisementRequest advertisement = new CreateAdvertisementRequest(3000, "Test headline",
                "Test description");
        String request = objectMapper.writeValueAsString(advertisement);

        sut.perform(post("/api/advertisements")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value(userMapper.toDto(user)))
                .andExpect(jsonPath("$.price").value(advertisement.getPrice()))
                .andExpect(jsonPath("$.headline").value(advertisement.getHeadline()))
                .andExpect(jsonPath("$.description").value(advertisement.getDescription()))
                .andExpect(jsonPath("$.status").value(String.valueOf(AdvertisementStatus.REVIEW)));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void createWithIncorrectDataThrowsException() {
        CreateAdvertisementRequest advertisement = new CreateAdvertisementRequest(-20, "Test headline",
                "Test description");
        String request = objectMapper.writeValueAsString(advertisement);

        sut.perform(post("/api/advertisements")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void updateReturnsCorrectData() {
        UpdateAdvertisementRequest advertisement = new UpdateAdvertisementRequest(1L, 5000,
                "New Headline", "New Description");
        String request = objectMapper.writeValueAsString(advertisement);

        sut.perform(put("/api/advertisements")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value(userMapper.toDto(user)))
                .andExpect(jsonPath("$.price").value(advertisement.getPrice()))
                .andExpect(jsonPath("$.headline").value(advertisement.getHeadline()))
                .andExpect(jsonPath("$.description").value(advertisement.getDescription()))
                .andExpect(jsonPath("$.status").value(String.valueOf(AdvertisementStatus.ACTIVE)));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void updateWithIncorrectDataThrowsException() {
        UpdateAdvertisementRequest advertisement = new UpdateAdvertisementRequest(1L, 5000,
                " ", "New Description");
        String request = objectMapper.writeValueAsString(advertisement);

        sut.perform(put("/api/advertisements")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("soccer_fanatic")
    void updateByAnotherUserThrowsException() {
        UpdateAdvertisementRequest advertisement = new UpdateAdvertisementRequest(1L, 5000,
                " ", "New Description");
        String request = objectMapper.writeValueAsString(advertisement);

        sut.perform(put("/api/advertisements")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void deleteDoesNotThrowException() {
        sut.perform(delete("/api/advertisements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deleted advertisement with ID: 1"));
    }

    @Test
    @SneakyThrows
    @WithMockUser("soccer_fanatic")
    void deleteByAnotherUserThrowsException() {
        sut.perform(delete("/api/advertisements/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void sellReturnsCorrectData() {
        sut.perform(put("/api/advertisements/1/sold"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value(userMapper.toDto(user)))
                .andExpect(jsonPath("$.price").value(advertisement.getPrice()))
                .andExpect(jsonPath("$.headline").value(advertisement.getHeadline()))
                .andExpect(jsonPath("$.description").value(advertisement.getDescription()))
                .andExpect(jsonPath("$.status").value(String.valueOf(AdvertisementStatus.SOLD)));
    }

    @Test
    @SneakyThrows
    @WithMockUser("soccer_fanatic")
    void sellByAnotherUserThrowsException() {
        sut.perform(put("/api/advertisements/1/sold"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void sellWithIncorrectDataThrowsException() {
        sut.perform(put("/api/advertisements/0/sold"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser(value = "admin", roles = "ADMIN")
    void updateByAdminReturnsCorrectData() {
        advertisement.setStatus(AdvertisementStatus.REVIEW);
        String request = objectMapper.writeValueAsString(advertisement);

        sut.perform(put("/api/advertisements/admin")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value(userMapper.toDto(advertisement.getUser())))
                .andExpect(jsonPath("$.price").value(advertisement.getPrice()))
                .andExpect(jsonPath("$.headline").value(advertisement.getHeadline()))
                .andExpect(jsonPath("$.description").value(advertisement.getDescription()))
                .andExpect(jsonPath("$.status").value(String.valueOf(AdvertisementStatus.REVIEW)));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void updateByRoleUserThrowsException() {
        advertisement.setStatus(AdvertisementStatus.REVIEW);
        String request = objectMapper.writeValueAsString(advertisement);

        sut.perform(put("/api/advertisements/admin")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser(value = "admin", roles = "ADMIN")
    void deleteByAdminDoesNotThrowException() {
        sut.perform(delete("/api/advertisements/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deleted advertisement with ID: 1"));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void deleteByRoleUserThrowsException() {
        sut.perform(delete("/api/advertisements/admin/1"))
                .andExpect(status().isBadRequest());
    }
}
