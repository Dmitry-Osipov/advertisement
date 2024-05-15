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
     * Получает список объявлений с ценой в указанном диапазоне и упорядоченных в соответствии с указанным порядком.
     * @param min Минимальная цена.
     * @param max Максимальная цена.
     * @param ordering Порядок сортировки (asc - по возрастанию цены, desc - по убыванию цены, null - по убыванию
     * рейтинга пользователя).
     * @return Список объявлений с ценой в указанном диапазоне и упорядоченных в соответствии с указанным порядком.
     */
    @Query("SELECT a FROM Advertisement a " +
            "WHERE a.price BETWEEN :min AND :max AND a.status = 'ACTIVE' " +
            "ORDER BY " +
            "CASE WHEN :ordering = 'asc' THEN a.price END ASC, " +
            "CASE WHEN :ordering = 'desc' THEN a.price END DESC, " +
            "CASE WHEN :ordering IS NULL THEN a.user.rating END DESC")
    List<Advertisement> findByPriceBetweenInOrder(@Param("min") Integer min,
                                                  @Param("max") Integer max,
                                                  @Param("ordering") String ordering);

    /**
     * Получает список всех активных объявлений, упорядоченных по рейтингу пользователя в порядке убывания.
     * @return Список всех активных объявлений, упорядоченных по рейтингу пользователя в порядке убывания.
     */
    @Query("SELECT a FROM Advertisement a " +
            "WHERE a.status = 'ACTIVE' " +
            "ORDER BY a.user.rating DESC")
    List<Advertisement> findAllInOrderByUserRating();

    /**
     * Получает список объявлений с ценой в указанном диапазоне, содержащих указанный заголовок и упорядоченных в
     * соответствии с указанным порядком.
     * @param min Минимальная цена.
     * @param max Максимальная цена.
     * @param headline Заголовок объявления (игнорирует регистр).
     * @param ordering Порядок сортировки (asc - по возрастанию цены, desc - по убыванию цены, null - по убыванию
     * рейтинга пользователя).
     * @return Список объявлений с ценой в указанном диапазоне, содержащих указанный заголовок и упорядоченных в
     * соответствии с указанным порядком.
     */
    @Query("SELECT a FROM Advertisement a " +
            "WHERE a.price BETWEEN :min AND :max AND LOWER(a.headline) = LOWER(:headline) AND a.status = 'ACTIVE' " +
            "ORDER BY " +
            "CASE WHEN :ordering = 'asc' THEN a.price END ASC, " +
            "CASE WHEN :ordering = 'desc' THEN a.price END DESC, " +
            "CASE WHEN :ordering IS NULL THEN a.user.rating END DESC")
    List<Advertisement> findByPriceBetweenAndHeadlineIgnoreCaseInOrder(@Param("min") Integer min,
                                                                       @Param("max") Integer max,
                                                                       @Param("headline") String headline,
                                                                       @Param("ordering") String ordering);

    /**
     * Получает список объявлений пользователя, упорядоченных в соответствии с указанным порядком.
     * @param user Пользователь, для которого нужно получить объявления.
     * @param ordering Порядок сортировки (asc - по возрастанию цены, desc - по убыванию цены, null - по убыванию
     * рейтинга пользователя).
     * @return Список объявлений пользователя, упорядоченных в соответствии с указанным порядком.
     */
    @Query("SELECT a FROM Advertisement a " +
            "WHERE a.user = :user AND a.status = 'ACTIVE' " +
            "ORDER BY " +
            "CASE WHEN :ordering = 'asc' THEN a.price END ASC, " +
            "CASE WHEN :ordering = 'desc' THEN a.price END DESC, " +
            "CASE WHEN :ordering IS NULL THEN a.id END DESC")
    List<Advertisement> findByUserInOrder(@Param("user") User user, @Param("ordering") String ordering);

    /**
     * Получает список объявлений пользователя с любым статусом, упорядоченных в соответствии с указанным порядком.
     * @param user Пользователь, для которого нужно получить объявления.
     * @param ordering Порядок сортировки (asc - по возрастанию цены, desc - по убыванию цены, null - по убыванию
     * рейтинга пользователя).
     * @return Список объявлений пользователя с любым статусом, упорядоченных в соответствии с указанным порядком.
     */
    @Query("SELECT a FROM Advertisement a " +
            "WHERE a.user = :user " +
            "ORDER BY " +
            "CASE WHEN :ordering = 'asc' THEN a.price END ASC, " +
            "CASE WHEN :ordering = 'desc' THEN a.price END DESC, " +
            "CASE WHEN :ordering IS NULL THEN a.id END DESC")
    List<Advertisement> findByUserInOrderWithAnyStatus(@Param("user") User user, @Param("ordering") String ordering);
}
