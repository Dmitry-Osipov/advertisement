package rf.senla.web.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rf.senla.domain.entity.Rating;
import rf.senla.domain.entity.User;
import rf.senla.domain.repository.RatingRepository;
import rf.senla.domain.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RatingRepositoryTest {
    @Autowired
    private RatingRepository sut;
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveDoesNotThrowsException() {
        User sender = userRepository.findByUsername("foodie").orElseThrow();
        User recipient = userRepository.findByUsername("foodie").orElseThrow();
        Rating expected = Rating.builder()
                .sender(sender)
                .recipient(recipient)
                .evaluation(5)
                .createdAt(LocalDateTime.now())
                .build();

        Rating actual = assertDoesNotThrow(() -> sut.save(expected));

        assertEquals(expected, actual);
    }

    @Test
    void getAverageRatingGivesCurrentData() {
        User recipient = userRepository.findByUsername("music_lover").orElseThrow();
        Double expected = 3.5;

        Double actual = assertDoesNotThrow(() -> sut.getAverageRatingByRecipient(recipient));

        assertEquals(expected, actual);
    }

    @Test
    void existsBySenderAndRecipientReturnsTrue() {
        User sender = userRepository.findByUsername("user123").orElseThrow();
        User recipient = userRepository.findByUsername("soccer_fanatic").orElseThrow();

        boolean actual = assertDoesNotThrow(() -> sut.existsBySenderAndRecipient(sender, recipient));

        assertTrue(actual);
    }

    @Test
    void existsBySenderAndRecipientReturnsFalse() {
        User sender = userRepository.findByUsername("user123").orElseThrow();
        User recipient = userRepository.findByUsername("user123").orElseThrow();

        boolean actual = assertDoesNotThrow(() -> sut.existsBySenderAndRecipient(sender, recipient));

        assertFalse(actual);
    }

    @Test
    void deleteBySenderIdAndRecipientIdDoesNotThrowException() {
        User sender = userRepository.findByUsername("user123").orElseThrow();
        User recipient = userRepository.findByUsername("soccer_fanatic").orElseThrow();

        assertDoesNotThrow(() -> sut.deleteBySender_IdOrRecipient_Id(sender.getId(), recipient.getId()));
    }
}
