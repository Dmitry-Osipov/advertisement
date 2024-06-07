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
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.AdvertisementStatus;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.web.dto.CreateMessageRequest;
import rf.senla.web.dto.DeleteByIdRequest;
import rf.senla.web.dto.UpdateMessageRequest;
import rf.senla.web.utils.AdvertisementMapper;
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
class RestMessageControllerTest {
    private User sender;
    private User recipient;
    private Advertisement advertisement;
    @Autowired
    private MockMvc sut;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AdvertisementMapper advertisementMapper;
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        sender = User.builder()
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

        recipient = User.builder()
                .id(2L)
                .username("cool_guy")
                .password("$2a$10$pT4a.wJbqJ9S8egWxAsQDuGoW2/JtO3/sFNqKRywS1my1HrVk.riq")
                .phoneNumber("+7(456)789-01-23")
                .rating(0.0)
                .email("john.doe@gmail.com")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();

        advertisement = Advertisement.builder()
                .id(2L)
                .user(recipient)
                .price(2000)
                .headline("Laptop")
                .description("A portable computer that is small and light enough to be used on one's lap, typically " +
                        "with a clamshell form factor and a built-in keyboard and display.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
    }

    @Test
    @SneakyThrows
    void getUserCorrespondenceDoesNotThrowException() {
        User currentUser = sender;
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(currentUser, null, new ArrayList<>()));

        sut.perform(get("/api/messages")
                        .param("username", "cool_guy")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @SneakyThrows
    void getUserCorrespondenceThrowsException() {
        User currentUser = sender;
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(currentUser, null, new ArrayList<>()));

        sut.perform(get("/api/messages")
                        .param("username", "testuser")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void createDoesNotThrowException() {
        CreateMessageRequest message = new CreateMessageRequest();
        message.setAdvertisement(advertisementMapper.toDto(advertisement));
        message.setText("Hello!");
        message.setRecipient(userMapper.toDto(recipient));
        String request = objectMapper.writeValueAsString(message);

        sut.perform(post("/api/messages")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sender").value(userMapper.toDto(sender)))
                .andExpect(jsonPath("$.recipient").value(userMapper.toDto(recipient)))
                .andExpect(jsonPath("$.advertisement").value(advertisementMapper.toDto(advertisement)))
                .andExpect(jsonPath("$.text").value("Hello!"))
                .andExpect(jsonPath("$.read").value(false));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void createWithIncorrectDataThrowsException() {
        CreateMessageRequest message = new CreateMessageRequest();
        message.setAdvertisement(advertisementMapper.toDto(advertisement));
        message.setText("Hello!");
        String request = objectMapper.writeValueAsString(message);

        sut.perform(post("/api/messages")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void updateDoesNotThrowException() {
        UpdateMessageRequest message = new UpdateMessageRequest();
        message.setId(1L);
        message.setText("Hello!");
        message.setRead(false);
        String request = objectMapper.writeValueAsString(message);

        sut.perform(put("/api/messages")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(message.getId()))
                .andExpect(jsonPath("$.sender").value(userMapper.toDto(sender)))
                .andExpect(jsonPath("$.recipient").value(userMapper.toDto(recipient)))
                .andExpect(jsonPath("$.advertisement").value(advertisementMapper.toDto(advertisement)))
                .andExpect(jsonPath("$.text").value(message.getText()))
                .andExpect(jsonPath("$.read").value(message.getRead()));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void updateWithIncorrectDataThrowsException() {
        UpdateMessageRequest message = new UpdateMessageRequest();
        message.setId(1L);
        message.setText("        ");
        String request = objectMapper.writeValueAsString(message);

        sut.perform(put("/api/messages")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void deleteDoesNotThrowException() {
        DeleteByIdRequest message = new DeleteByIdRequest(1L);
        String request = objectMapper.writeValueAsString(message);

        sut.perform(delete("/api/messages")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deleted message with id: " + message.getId()));
    }

    @Test
    @SneakyThrows
    @WithMockUser("user123")
    void deleteWithIncorrectDataThrowsException() {
        DeleteByIdRequest message = new DeleteByIdRequest(0L);
        String request = objectMapper.writeValueAsString(message);

        sut.perform(delete("/api/messages")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest());
    }
}
