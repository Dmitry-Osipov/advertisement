package rf.senla.advertisement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rf.senla.advertisement.domain.entity.Comment;

/**
 * Репозиторий для работы с сущностью комментария в базе данных.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
