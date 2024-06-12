package rf.senla.web.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.Message;
import rf.senla.domain.entity.User;
import rf.senla.domain.repository.AdvertisementRepository;
import rf.senla.domain.repository.MessageRepository;
import rf.senla.domain.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MessageRepositoryTest {
    @Autowired
    private MessageRepository sut;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveDoesNotThrowsException() {
        Advertisement advertisement = advertisementRepository.findById(2L).orElseThrow();
        User sender = userRepository.findByUsername("user123").orElseThrow();
        User recipient = userRepository.findByUsername("cool_guy").orElseThrow();
        Message expected = Message.builder()
                .sender(sender)
                .recipient(recipient)
                .advertisement(advertisement)
                .text("Test message")
                .read(false)
                .build();

        Message actual = assertDoesNotThrow(() -> sut.save(expected));

        assertEquals(expected, actual);
    }

    @Test
    void findMessagesBetweenUsersReturnsCorrectData() {
        User sender = userRepository.findByUsername("user123").orElseThrow();
        User recipient = userRepository.findByUsername("cool_guy").orElseThrow();
        Pageable pageable = Pageable.ofSize(20);
        int expected = 2;

        int actual = assertDoesNotThrow(() ->
                sut.findMessagesBetweenUsers(sender.getId(), recipient.getId(), pageable).size());

        assertEquals(expected, actual);
    }

    @Test
    void deleteBySenderIdOrRecipientIdDoesNotThrowsException() {
        Long id = userRepository.findByUsername("user123").orElseThrow().getId();

        assertDoesNotThrow(() -> sut.deleteBySender_IdOrRecipient_Id(id, id));
    }

    @Test
    void deleteByAdvertisementIdDoesNotThrowsException() {
        Long id = advertisementRepository.findById(2L).orElseThrow().getId();

        assertDoesNotThrow(() -> sut.deleteByAdvertisement_Id(id));
    }
}
