package rf.senla.domain.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import rf.senla.domain.entity.Comment;

import java.util.List;

public interface ICommentService {
    /**
     * Сохранение комментария
     * @param comment комментарий
     * @param sender отправитель сообщения
     * @return сохранённый комментарий
     */
    Comment create(Comment comment, UserDetails sender);

    /** Обновление комментария
     * @param comment сущность
     * @param user отправитель сообщения
     * @return обновлённый комментарий
     */
    Comment update(Comment comment, UserDetails user);

    /** Удаление комментария
     * @param id ID комментария
     * @param user текущий пользователь
     */
    void delete(Long id, UserDetails user);

    /**
     * Получение всех комментариев объявления с пагинацией.
     * @param advertisementId ID объявления
     * @param pageable пагинация
     * @return список комментариев
     */
    List<Comment> getAll(Long advertisementId, Pageable pageable);
}
