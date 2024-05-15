package rf.senla.advertisement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.entity.Comment;

import java.util.List;

/**
 * Репозиторий для работы с сущностью комментария в базе данных.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Получает список комментариев, связанных с указанным объявлением, упорядоченных по времени создания в порядке
     * убывания.
     * @param advertisement Объявление, для которого нужно получить комментарии.
     * @return Список комментариев, связанных с объявлением, упорядоченных по времени создания в порядке убывания.
     */
    List<Comment> findByAdvertisementOrderByCreatedAtDesc(Advertisement advertisement);

    /**
     * Получает все комментарии из репозитория, упорядоченные по времени создания в порядке убывания.
     * @return Список всех комментариев в репозитории, упорядоченных по времени создания в порядке убывания.
     */
    List<Comment> findAllByOrderByCreatedAtDesc();
}
