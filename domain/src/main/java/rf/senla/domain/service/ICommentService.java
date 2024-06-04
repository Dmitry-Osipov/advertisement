package rf.senla.domain.service;

import rf.senla.domain.entity.Comment;

import java.util.List;

public interface ICommentService {
    /**
     * Сохранение комментария
     * @param comment комментарий
     * @return сохранённый комментарий
     */
    Comment save(Comment comment);

    /** Обновление комментария
     * @param comment сущность
     * @return обновлённый комментарий
     */
    Comment update(Comment comment);

    /** Удаление комментария
     * @param comment комментарий
     */
    void delete(Comment comment);

    /**
     * Получение всех комментариев объявления с пагинацией.
     * @param advertisementId ID объявления
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return список комментариев
     */
    List<Comment> getAll(Long advertisementId, Integer page, Integer size);
}
