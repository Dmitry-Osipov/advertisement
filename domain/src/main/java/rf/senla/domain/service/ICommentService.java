package rf.senla.domain.service;

import rf.senla.domain.entity.Comment;

import java.util.List;

public interface ICommentService extends IService<Comment> {
    /**
     * Получение всех комментариев объявления с пагинацией.
     * @param advertisementId ID объявления
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return список комментариев
     */
    List<Comment> getAll(Long advertisementId, Integer page, Integer size);
}
