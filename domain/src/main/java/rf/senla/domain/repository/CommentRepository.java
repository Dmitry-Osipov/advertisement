package rf.senla.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rf.senla.domain.entity.Comment;

import java.util.List;

/**
 * Репозиторий для работы с сущностью комментария в базе данных.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Получает список комментариев, связанных с указанным объявлением, с пагинацией.
     * @param advertisementId ID объявления, для которого нужно получить комментарии.
     * @param pageable Пагинация.
     * @return Список комментариев, связанных с объявлением.
     */
    List<Comment> findByAdvertisement_Id(Long advertisementId, Pageable pageable);

    /**
     * Удаление комментариев пользователя
     * @param id ID пользователя
     */
    void deleteByUser_Id(Long id);

    /**
     * Удаление комментариев с объявлениями, у которых владельцем является пользователь
     * @param id ID пользователя
     */
    void deleteByAdvertisement_User_Id(Long id);

    /**
     * Удаление комментариев по объявлению
     * @param id ID объявления
     */
    void deleteByAdvertisement_Id(Long id);
}
