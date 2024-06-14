package rf.senla.web.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.AdvertisementStatus;
import rf.senla.domain.entity.User;
import rf.senla.domain.repository.AdvertisementRepository;
import rf.senla.domain.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdvertisementRepositoryTest {
    @Autowired
    private AdvertisementRepository sut;
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveDoesNotThrowException() {
        User owner = userRepository.findByUsername("user123").orElseThrow();
        Advertisement expected = Advertisement.builder()
                .user(owner)
                .headline("Test Headline")
                .description("Test description")
                .price(0)
                .status(AdvertisementStatus.REVIEW)
                .boosted(false)
                .build();

        Advertisement actual = assertDoesNotThrow(() -> sut.save(expected));

        assertEquals(expected, actual);
    }

    @Test
    @Disabled
    void findAllWithActiveStatusReturnsCorrectData() {
        Advertisement advertisement = Advertisement.builder()
                .user(userRepository.findByUsername("user123").orElseThrow())
                .headline("Test Headline")
                .description("Test description")
                .price(0)
                .status(AdvertisementStatus.REVIEW)
                .boosted(false)
                .build();
        sut.save(advertisement);
        Pageable pageable = Pageable.ofSize(20);
        int size = 10;

        List<Advertisement> actual = assertDoesNotThrow(() -> sut.findAllWithActiveStatus(pageable));

        assertEquals(size, actual.size());
        assert actual.stream().allMatch(ad -> ad.getStatus().equals(AdvertisementStatus.ACTIVE));
    }

    @Test
    @Disabled
    void findByPriceBetweenWithActiveStatusReturnsCorrectData() {
        Pageable pageable = Pageable.ofSize(20);
        int minPrice = 5000;
        int masPrice = 7000;
        int size = 3;

        List<Advertisement> actual = assertDoesNotThrow(() ->
                sut.findByPriceBetweenWithActiveStatus(minPrice, masPrice, pageable));

        assertEquals(size, actual.size());
        assert actual.stream().allMatch(ad -> ad.getStatus().equals(AdvertisementStatus.ACTIVE));
    }

    @Test
    @Disabled
    void findByPriceBetweenAndHeadlineIgnoreCaseWithActiveStatusReturnsCorrectData() {
        Pageable pageable = Pageable.ofSize(20);
        String headline = "Smartphone";
        int minPrice = 0;
        int maxPrice = 10_000;
        Advertisement advertisement = Advertisement.builder()
                .user(userRepository.findByUsername("user123").orElseThrow())
                .headline(headline)
                .description("Test description")
                .price(0)
                .status(AdvertisementStatus.REVIEW)
                .boosted(false)
                .build();
        sut.save(advertisement);
        int size = 1;

        List<Advertisement> actual = assertDoesNotThrow(() ->
                sut.findByPriceBetweenAndHeadlineIgnoreCaseWithActiveStatus(minPrice, maxPrice, headline, pageable));

        assertEquals(size, actual.size());
        assert actual.stream().allMatch(ad -> ad.getStatus().equals(AdvertisementStatus.ACTIVE));
    }

    @Test
    void findByUserReturnsCorrectData() {
        Pageable pageable = Pageable.ofSize(20);
        User user = userRepository.findByUsername("user123").orElseThrow();
        Advertisement advertisement = Advertisement.builder()
                .user(user)
                .headline("Test headline")
                .description("Test description")
                .price(0)
                .status(AdvertisementStatus.REVIEW)
                .boosted(false)
                .build();
        sut.save(advertisement);
        int size = 2;

        List<Advertisement> actual = assertDoesNotThrow(() -> sut.findByUser(user, pageable));

        assertEquals(size, actual.size());
    }

    @Test
    void findByUserWithActiveStatusReturnsCorrectData() {
        Pageable pageable = Pageable.ofSize(20);
        User user = userRepository.findByUsername("user123").orElseThrow();
        Advertisement advertisement = Advertisement.builder()
                .user(user)
                .headline("Test headline")
                .description("Test description")
                .price(0)
                .status(AdvertisementStatus.REVIEW)
                .boosted(false)
                .build();
        sut.save(advertisement);
        int size = 1;

        List<Advertisement> actual = assertDoesNotThrow(() -> sut.findByUserWithActiveStatus(user, pageable));

        assertEquals(size, actual.size());
        assert actual.stream().allMatch(ad -> ad.getStatus().equals(AdvertisementStatus.ACTIVE));
    }

    @Test
    void deleteByUserIdDoesNotThrowException() {
        User user = userRepository.findByUsername("user123").orElseThrow();

        assertDoesNotThrow(() -> sut.deleteByUser_Id(user.getId()));
    }
}
