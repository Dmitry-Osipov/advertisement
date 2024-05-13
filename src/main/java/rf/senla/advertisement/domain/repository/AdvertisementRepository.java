package rf.senla.advertisement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.security.entity.User;

import java.util.List;

/**
 * Репозиторий для работы с сущностью объявления в базе данных.
 */
@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    /**
     * Найти список объявлений по его заголовку.
     * @param headline заголовок
     * @return список объявлений
     */
    @Query("SELECT a FROM Advertisement a WHERE LOWER(a.headline) = LOWER(:headline)")
    List<Advertisement> findAllByHeadlineIgnoreCase(@Param("headline") String headline);

    /**
     * Найти список объявлений по его заголовку в промежутке цен.
     * @param min минимальная цена
     * @param max максимальная цена
     * @param headline заголовок
     * @return список объявлений
     */
    List<Advertisement> findByPriceBetweenAndHeadlineIgnoreCase(Integer min, Integer max, String headline);

    /**
     * Найти список объявлений в промежутке цен.
     * @param min минимальная цена
     * @param max максимальная цена
     * @return список объявлений
     */
    List<Advertisement> findByPriceBetween(Integer min, Integer max);

    /**
     * Найти список объявлений пользователя.
     * @param user пользователь
     * @return список объявлений
     */
    List<Advertisement> findByUser(User user);
}
