package rf.senla.web.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.Comment;
import rf.senla.domain.entity.User;
import rf.senla.domain.repository.AdvertisementRepository;
import rf.senla.domain.repository.CommentRepository;
import rf.senla.domain.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    @Autowired
    private CommentRepository sut;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveDoesNotTrowsException() {
        User sender = userRepository.findByUsername("user123").orElseThrow();
        Advertisement advertisement = advertisementRepository.findById(5L).orElseThrow();
        Comment expected = Comment.builder()
                .advertisement(advertisement)
                .user(sender)
                .text("Test comment")
                .createdAt(LocalDateTime.now())
                .build();

        Comment actual = assertDoesNotThrow(() -> sut.save(expected));

        assertEquals(expected, actual);
    }

    @Test
    void findByAdvertisementIdReturnsCorrectData() {
        Advertisement advertisement = advertisementRepository.findById(1L).orElseThrow();
        Pageable pageable = Pageable.ofSize(20);
        int expected = 2;

        int actual = assertDoesNotThrow(() -> sut.findByAdvertisement_Id(advertisement.getId(), pageable).size());

        assertEquals(expected, actual);
    }

    @Test
    void deleteByUserIdDoesNotTrowsException() {
        User sender = userRepository.findByUsername("user123").orElseThrow();

        assertDoesNotThrow(() -> sut.deleteByUser_Id(sender.getId()));
    }

    @Test
    void deleteByAdvertisementUserIdDoesNotTrowsException() {
        Advertisement advertisement = advertisementRepository.findById(1L).orElseThrow();

        assertDoesNotThrow(() -> sut.deleteByAdvertisement_Id(advertisement.getUser().getId()));
    }

    @Test
    void deleteByAdvertisementIdDoesNotThrowException() {
        Advertisement advertisement = advertisementRepository.findById(1L).orElseThrow();

        assertDoesNotThrow(() -> sut.deleteByAdvertisement_Id(advertisement.getId()));
    }
}
