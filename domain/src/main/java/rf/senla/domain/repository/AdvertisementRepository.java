package rf.senla.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.User;

import java.util.List;

/**
 * Репозиторий для работы с сущностью объявления в базе данных.
 */
@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    /**
     * Получить список объявлений
     * @param min минимальная цена
     * @param max максимальная цена
     * @param keyword ключевое слово в заголовке или описании
     * @param pageable пагинация
     * @return список объявлений
     */
    @Query("SELECT a FROM Advertisement a " +
            "WHERE a.price BETWEEN :min AND :max " +
            "AND ( (:keyword) IS NULL OR LOWER(a.headline) LIKE ('%' || LOWER(CAST(:keyword AS text)) || '%') ) " +
            "OR ( (:keyword) IS NULL OR LOWER(a.description) LIKE ('%' || LOWER(CAST(:keyword AS text)) || '%') ) " +
            "AND a.status = 'ACTIVE'")
    List<Advertisement> findAllWithActiveStatus(@Param("min") Integer min,
                                                @Param("max") Integer max,
                                                @Param("keyword") String keyword,
                                                Pageable pageable);

    /**
     * Получить список объявлений по пользователю
     * @param user пользователь
     * @param active флаг только активных заказов
     * @param pageable пагинация
     * @return список объявлений пользователя
     */
    @Query("SELECT a FROM Advertisement a " +
            "WHERE a.user = :user " +
            "AND ( (:active) IS NULL OR " +
            "(CASE WHEN :active = TRUE THEN a.status = 'ACTIVE' ELSE 1=1 END) )")
    List<Advertisement> findByUser(@Param("user") User user, @Param("active") Boolean active, Pageable pageable);

    /**
     * Удаление объявления по владельцу
     * @param id ID пользователя
     */
    void deleteByUser_Id(Long id);
}
