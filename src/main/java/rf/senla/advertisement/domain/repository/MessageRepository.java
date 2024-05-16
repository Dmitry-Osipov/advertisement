package rf.senla.advertisement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rf.senla.advertisement.domain.entity.Message;

import java.util.List;

/**
 * Репозиторий для работы с сущностью сообщения в базе данных.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Возвращает список сообщений между двумя пользователями.
     * @param firstId идентификатор первого пользователя
     * @param secondId идентификатор второго пользователя
     * @return список сообщений между двумя пользователями
     */
    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender.id = :firstId AND m.recipient.id = :secondId) " +
            "OR (m.sender.id = :secondId AND m.recipient.id = :firstId) " +
            "ORDER BY m.sentAt DESC")
    List<Message> findMessagesBetweenUsers(Long firstId, Long secondId);

    /**
     * Получает список всех сообщений, в которых указанный пользователь является отправителем или получателем,
     * упорядоченных по времени отправки в порядке убывания.
     * @param userId Идентификатор пользователя, для которого нужно получить сообщения.
     * @return Список всех сообщений, в которых указанный пользователь является отправителем или получателем,
     * упорядоченных по времени отправки в порядке убывания.
     */
    @Query("SELECT m FROM Message m " +
            "WHERE m.sender.id = :userId OR m.recipient.id = :userId " +
            "ORDER BY m.sentAt DESC")
    List<Message> findAllByUserIdInOrderBySentAtDesc(Long userId);
}
