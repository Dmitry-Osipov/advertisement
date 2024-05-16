package rf.senla.advertisement.domain.repository;

import org.springframework.data.domain.Pageable;
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
     * Получает список комментариев, связанных с указанным объявлением, с пагинацией.
     * @param advertisement Объявление, для которого нужно получить комментарии.
     * @param pageable Пагинация.
     * @return Список комментариев, связанных с объявлением.
     */
    List<Comment> findByAdvertisement(Advertisement advertisement, Pageable pageable);
}
