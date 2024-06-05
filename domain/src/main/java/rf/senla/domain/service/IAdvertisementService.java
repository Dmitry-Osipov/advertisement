package rf.senla.domain.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import rf.senla.domain.entity.Advertisement;

import java.util.List;

/**
 * Сервис для работы с объявлениями.
 */
public interface IAdvertisementService {
    /**
     * Сохранение объявления
     * @param advertisement объявление
     * @param userDetails данные пользователя
     * @return сохранённое объявление
     */
    Advertisement create(Advertisement advertisement, UserDetails userDetails);

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
     * Получение списка объявлений с пагинацией
     * @param pageable пагинация
     * @return список объявлений
     */
    List<Advertisement> getAll(Pageable pageable);

    /**
     * Получить объявления по пользователю с пагинацией.
     * @param username логин пользователя
     * @param active выводить только активные заказы
     * @param pageable пагинация
     * @return список объявлений
     */
    List<Advertisement> getAll(String username, Boolean active, Pageable pageable);

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
     * @param pageable пагинация
     * @return список объявлений
     */
    List<Advertisement> getAll(Integer min, Integer max, String headline, Pageable pageable);
}
