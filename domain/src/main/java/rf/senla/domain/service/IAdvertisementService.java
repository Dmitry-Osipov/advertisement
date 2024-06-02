package rf.senla.domain.service;

import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.User;

import java.util.List;

/**
 * Сервис для работы с объявлениями.
 */
public interface IAdvertisementService extends IService<Advertisement> {
    /**
     * Получить объявления по пользователю с пагинацией.
     * @param user пользователь
     * @param sortBy условие сортировки
     * @param active выводить только активные заказы
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return список объявлений
     */
    List<Advertisement> getAll(User user, String sortBy, Boolean active, Integer page, Integer size);

    /**
     * Получить объявление по его id.
     * @param id уникальный идентификатор объявления
     * @return объявление
     */
    Advertisement getById(Long id);

    /**
     * Получить объявления по заголовку в промежутке цен с пагинацией.
     * @param min минимальная цена
     * @param max максимальная цена
     * @param headline заголовок
     * @param sortBy условие сортировки
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return список объявлений
     */
    List<Advertisement> getAll(Integer min, Integer max, String headline, String sortBy, Integer page, Integer size);
}
