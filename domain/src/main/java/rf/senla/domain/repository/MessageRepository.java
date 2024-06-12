package rf.senla.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rf.senla.domain.entity.Message;

import java.util.List;

/**
 * Репозиторий для работы с сущностью сообщения в базе данных.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Возвращает список сообщений между двумя пользователями с пагинацией.
     * @param firstId идентификатор первого пользователя
     * @param secondId идентификатор второго пользователя
     * @param pageable пагинация
     * @return список сообщений между двумя пользователями
     */
    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender.id = :firstId AND m.recipient.id = :secondId) " +
            "OR (m.sender.id = :secondId AND m.recipient.id = :firstId)")
    List<Message> findMessagesBetweenUsers(Long firstId, Long secondId, Pageable pageable);

    /**
     * Удаление сообщений по отправителю или получателю
     * @param sender ID отправителя
     * @param recipient ID получателя
     */
    void deleteBySender_IdOrRecipient_Id(Long sender, Long recipient);

    /**
     * Удаление сообщений по привязанному объявлению
     * @param id ID объявления
     */
    void deleteByAdvertisement_Id(Long id);
}