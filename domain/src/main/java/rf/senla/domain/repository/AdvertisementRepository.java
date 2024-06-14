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
     * @param headline заголовок
     * @param description описание
     * @param pageable пагинация
     * @return список объявлений
     */
    @Query("SELECT a FROM Advertisement a " +
            "WHERE a.price BETWEEN :min AND :max " +
            "AND ( (:headline) is null OR LOWER(a.headline) LIKE ('%' || LOWER(CAST(:headline AS text)) || '%') ) " +
            "AND ( (:description) is null OR LOWER(a.description) LIKE " +
            "('%' || LOWER(CAST(:description AS text)) || '%') ) " +
            "AND a.status = 'ACTIVE'")
    List<Advertisement> findAllWithActiveStatus(@Param("min") Integer min,
                                                @Param("max") Integer max,
                                                @Param("headline") String headline,
                                                @Param("description") String description,
                                                Pageable pageable);

    /**
     * Получает объявления для указанного пользователя, отсортированные с учетом любого статуса.
     * @param user Пользователь, для которого нужно получить объявления.
     * @param pageable Объект {@link Pageable} для управления пагинацией и сортировкой.
     * @return Страница объявлений для указанного пользователя, отсортированных с учетом любого статуса.
     */
    List<Advertisement> findByUser(User user, Pageable pageable);

    /**
     * Получает активные объявления для указанного пользователя, отсортированные по рейтингу пользователя.
     * @param user Пользователь, для которого нужно получить активные объявления.
     * @param pageable Объект {@link Pageable} для управления пагинацией и сортировкой.
     * @return Страница активных объявлений для указанного пользователя, отсортированных по рейтингу пользователя.
     */
    @Query("SELECT a FROM Advertisement a WHERE a.user = :user AND a.status = 'ACTIVE'")
    List<Advertisement> findByUserWithActiveStatus(@Param("user") User user, Pageable pageable);

    /**
     * Удаление объявления по владельцу
     * @param id ID пользователя
     */
    void deleteByUser_Id(Long id);
}
