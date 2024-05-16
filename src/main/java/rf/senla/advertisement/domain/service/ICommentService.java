package rf.senla.advertisement.domain.service;

import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.entity.Comment;

import java.util.List;

public interface ICommentService extends IService<Comment> {
    /**
     * Получение всех комментариев объявления с пагинацией.
     * @param advertisement объявление
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return список комментариев
     */
    List<Comment> getAll(Advertisement advertisement, Integer page, Integer size);
}
