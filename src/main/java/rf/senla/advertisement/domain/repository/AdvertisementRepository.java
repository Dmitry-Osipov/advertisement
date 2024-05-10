package rf.senla.advertisement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rf.senla.advertisement.domain.entity.Advertisement;

/**
 * Репозиторий для работы с сущностью объявления в базе данных.
 */
@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
