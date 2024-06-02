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
     * Получает все объявления, отсортированные по рейтингу пользователя.
     * @param pageable Объект {@link Pageable} для управления пагинацией и сортировкой.
     * @return Список объявлений, отсортированный по рейтингу пользователя.
     */
    @Query("SELECT a FROM Advertisement a WHERE a.status = 'ACTIVE'")
    List<Advertisement> findAllInOrderByUserRating(Pageable pageable);

    /**
     * Получает объявления с ценой в указанном диапазоне, отсортированные по цене.
     * @param min Минимальная цена.
     * @param max Максимальная цена.
     * @param pageable Объект {@link Pageable} для управления пагинацией и сортировкой.
     * @return Страница объявлений с ценой в указанном диапазоне, отсортированных по цене.
     */
    @Query("SELECT a FROM Advertisement a " +
            "WHERE a.price BETWEEN :min AND :max " +
            "AND a.status = 'ACTIVE'")
    List<Advertisement> findByPriceBetweenInOrder(@Param("min") Integer min,
                                                  @Param("max") Integer max,
                                                  Pageable pageable);

    /**
     * Получает объявления с ценой в указанном диапазоне и с указанным заголовком,
     * отсортированные по цене.
     * @param min Минимальная цена.
     * @param max Максимальная цена.
     * @param headline Заголовок объявления.
     * @param pageable Объект {@link Pageable} для управления пагинацией и сортировкой.
     * @return Страница объявлений с ценой в указанном диапазоне и указанным заголовком, отсортированных по цене.
     */
    @Query("SELECT a FROM Advertisement a " +
            "WHERE a.price BETWEEN :min AND :max AND LOWER(a.headline) = LOWER(:headline) " +
            "AND a.status = 'ACTIVE'")
    List<Advertisement> findByPriceBetweenAndHeadlineIgnoreCaseInOrder(@Param("min") Integer min,
                                                                       @Param("max") Integer max,
                                                                       @Param("headline") String headline,
                                                                       Pageable pageable);

    /**
     * Получает объявления для указанного пользователя, отсортированные с учетом любого статуса.
     * @param user Пользователь, для которого нужно получить объявления.
     * @param pageable Объект {@link Pageable} для управления пагинацией и сортировкой.
     * @return Страница объявлений для указанного пользователя, отсортированных с учетом любого статуса.
     */
    @Query("SELECT a FROM Advertisement a WHERE a.user = :user ")
    List<Advertisement> findByUserInOrderWithAnyStatus(@Param("user") User user, Pageable pageable);

    /**
     * Получает активные объявления для указанного пользователя, отсортированные по рейтингу пользователя.
     * @param user Пользователь, для которого нужно получить активные объявления.
     * @param pageable Объект {@link Pageable} для управления пагинацией и сортировкой.
     * @return Страница активных объявлений для указанного пользователя, отсортированных по рейтингу пользователя.
     */
    @Query("SELECT a FROM Advertisement a WHERE a.user = :user AND a.status = 'ACTIVE'")
    List<Advertisement> findByUserInOrder(@Param("user") User user, Pageable pageable);
}
