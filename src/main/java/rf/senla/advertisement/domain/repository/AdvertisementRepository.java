package rf.senla.advertisement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rf.senla.advertisement.domain.entity.Advertisement;

import java.util.List;

/**
 * Репозиторий для работы с сущностью объявления в базе данных.
 */
@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    /**
     * Найти список объявлений по его заголовку.
     * @param headline заголовок
     * @return Список объявлений
     */
    @Query("SELECT a FROM Advertisement a WHERE LOWER(a.headline) = LOWER(:headline)")
    List<Advertisement> findAllByHeadlineIgnoreCase(@Param("headline") String headline);
}
