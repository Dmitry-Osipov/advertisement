package rf.senla.advertisement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rf.senla.advertisement.domain.entity.Advertisement;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью объявления в базе данных.
 */
@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    /**
     * Найти объявление по его заголовку.
     * @param headline заголовок
     * @return объект Optional, содержащий объявление, если найден, иначе пустой Optional
     */
    Optional<Advertisement> findByHeadlineIgnoreCase(String headline);
}
