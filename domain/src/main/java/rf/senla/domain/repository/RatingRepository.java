package rf.senla.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rf.senla.domain.entity.Rating;
import rf.senla.domain.entity.User;

/**
 * Репозиторий для работы с сущностями рейтинга
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    /**
     * Получение рейтинга пользователя
     * @param recipient пользователь
     * @return рейтинг пользователя
     */
    @Query("SELECT AVG(r.evaluation) FROM Rating r WHERE r.recipient = :recipient")
    Double getAverageRatingByRecipient(User recipient);

    /**
     * Проверка содержания в БД пары отправителя и получателя рейтинга
     * @param sender отправитель
     * @param recipient получатель
     * @return {@code true}, если пара есть, иначе {@code false}
     */
    boolean existsBySenderAndRecipient(User sender, User recipient);

    /**
     * Удаление рейтингов по отправителю или получателю
     * @param sender ID отправителя
     * @param recipient ID получателя
     */
    void deleteBySender_IdOrRecipient_Id(Long sender, Long recipient);
}
