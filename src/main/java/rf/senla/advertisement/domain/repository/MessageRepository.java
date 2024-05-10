package rf.senla.advertisement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rf.senla.advertisement.domain.entity.Message;

/**
 * Репозиторий для работы с сущностью сообщения в базе данных.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
