package rf.senla.web.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rf.senla.domain.repository.AdvertisementRepository;
import rf.senla.domain.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdvertisementRepositoryTest {
    @Autowired
    private AdvertisementRepository sut;
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveDoesNotThrowException() {
        // TODO: continue
    }
}
