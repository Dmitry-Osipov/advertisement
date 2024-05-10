package rf.senla.advertisement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rf.senla.advertisement.domain.entity.Conversation;

/**
 * Репозиторий для работы с сущностью переписки в базе данных.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
