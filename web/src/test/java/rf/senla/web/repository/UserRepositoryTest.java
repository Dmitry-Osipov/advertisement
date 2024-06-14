package rf.senla.web.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.domain.repository.UserRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@SuppressWarnings("java:S5778")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository sut;

    @Test
    void saveUserDoesNotThrowsException() {
        User expected = User.builder()
                .username("testuser")
                .password("testpassword")
                .phoneNumber("+7(777)777-77-77")
                .rating(0.0)
                .email("testemail@example.com")
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();

        User actual = assertDoesNotThrow(() -> sut.save(expected));

        assertEquals(expected.getUsername(), actual.getUsername());
    }

    @Test
    void saveUserWithDuplicateUsernameThrowsDataIntegrityViolationException() {
        User expected = User.builder()
                .username("user123")
                .password("testpassword")
                .phoneNumber("+7(777)777-77-77")
                .rating(0.0)
                .email("testemail@example.com")
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> sut.save(expected));
    }

    @Test
    void saveUserWithDuplicateEmailThrowsDataIntegrityViolationException() {
        User expected = User.builder()
                .username("testuser")
                .password("testpassword")
                .phoneNumber("+7(777)777-77-77")
                .rating(0.0)
                .email("storm-yes@yandex.ru")
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> sut.save(expected));
    }

    @Test
    void findByUsernameDoesNotThrowsException() {
        String expected = "user123";

        String actual = assertDoesNotThrow(() -> sut.findByUsername(expected).orElseThrow().getUsername());

        assertEquals(expected, actual);
    }

    @Test
    void findByUsernameThrowsUsernameNotFoundException() {
        String expected = "testuser";

        assertThrows(UsernameNotFoundException.class, () -> sut.findByUsername(expected)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Test
    void existsByUsernameReturnsTrue() {
        String expected = "cool_guy";

        boolean actual = assertDoesNotThrow(() -> sut.existsByUsername(expected));

        assertTrue(actual);
    }

    @Test
    void existsByUsernameReturnsFalse() {
        String expected = "testuser";

        boolean actual = assertDoesNotThrow(() -> sut.existsByUsername(expected));

        assertFalse(actual);
    }

    @Test
    void existsByEmailReturnsTrue() {
        String expected = "olivia.taylor@live.com";

        boolean actual = assertDoesNotThrow(() -> sut.existsByEmail(expected));

        assertTrue(actual);
    }

    @Test
    void existsByEmailReturnsFalse() {
        String expected = "testemail@example.com";

        boolean actual = assertDoesNotThrow(() -> sut.existsByEmail(expected));

        assertFalse(actual);
    }

    @Test
    void deleteByUsernameWithoutRelatedEntitiesDoesNotThrowsException() {
        String expected = "foodie";

        assertDoesNotThrow(() -> sut.deleteByUsername(expected));

        assertThrows(UsernameNotFoundException.class, () -> sut.findByUsername(expected)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Test
    void findResetByPasswordTokenDoesNotThrowsException() {
        String expected = "token";
        User user = sut.findByUsername("user123").orElseThrow();
        user.setResetPasswordToken("token");
        user.setResetPasswordTokenExpiryDate(new Date());
        sut.save(user);

        User actual = assertDoesNotThrow(() -> sut.findByResetPasswordToken(expected).orElseThrow());

        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getResetPasswordToken(), actual.getResetPasswordToken());
    }
}
