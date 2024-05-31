package rf.senla.advertisement.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.service.JwtService;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JwtServiceTest {
    private User user;
    @Value("${token.signing.key}")
    private String jwtSigningKey;
    @InjectMocks
    private JwtService sut;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        user = User.builder()
                .id(1L)
                .username("user123")
                .password("$2a$10$.PSEN9QPfyvpoXh9RQzdy.Wlok/5KO.iwcNYQOe.mmVgdTAeOO0AW")
                .phoneNumber("+7(123)456-78-90")
                .rating(0)
                .email("storm-yes@yandex.ru")
                .boosted(true)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();

        for (Field field : sut.getClass().getDeclaredFields()) {
            if (field.getName().equals("jwtSigningKey")) {
                field.setAccessible(true);
                field.set(sut, jwtSigningKey);
            }
        }
    }

    @Test
    void extractUsernameGivesCurrentData() {
        String expected = sut.generateToken(user);

        String username = assertDoesNotThrow(() -> sut.extractUsername(expected));

        assertEquals(user.getUsername(), username);
    }

    @Test
    void generateTokenDoesNotThrowsException() {
        assertNotNull(sut.generateToken(user));
    }

    @Test
    void isTokenValidGivesCurrentData() {
        String expected = sut.generateToken(user);

        assertTrue(sut.isTokenValid(expected, user));
    }
}
