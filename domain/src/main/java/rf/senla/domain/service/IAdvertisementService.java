package rf.senla.domain.service;

import org.springframework.data.domain.Pageable;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.User;

import java.util.List;

/**
 * Сервис для работы с объявлениями.
 */
public interface IAdvertisementService {
    /**
     * Сохранение объявления
     * @param advertisement объявление
     * @return сохранённое объявление
     */
    Advertisement save(Advertisement advertisement);

    /** Обновление объявления
     * @param advertisement объявление
     * @return обновлённое объявление
     */
    Advertisement update(Advertisement advertisement);

    /** Удаление объявления
     * @param advertisement объявление
     */
    void delete(Advertisement advertisement);

    /**
     * Получение списка объявлений
     * @return список объявлений
     */
    List<Advertisement> getAll(Pageable pageable);

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
