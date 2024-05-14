package rf.senla.advertisement.domain.service;

import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.exception.NoEntityException;
import rf.senla.advertisement.security.entity.User;

import java.util.List;

/**
 * Сервис для работы с объявлениями.
 */
public interface IAdvertisementService extends IService<Advertisement> {
    /**
     * Получить объявления по заголовку.
     * @param headline заголовок объявления
     * @param sortBy условие сортировки
     * @return найденное объявление
     * @throws NoEntityException если объявление не найдено
     */
    List<Advertisement> getAll(String headline, String sortBy);

    /**
     * Получить объявления по заголовку в промежутке цен.
     * @param min минимальная цена
     * @param max максимальная цена
     * @param headline заголовок
     * @param sortBy условие сортировки
     * @return список объявлений
     */
    List<Advertisement> getAll(Integer min, Integer max, String headline, String sortBy);

    /**
     * Получить объявления по пользователю.
     * @param user пользователь
     * @param sortBy условие сортировки
     * @param active выводить только активные заказы
     * @return список объявлений
     */
    List<Advertisement> getAll(User user, String sortBy, Boolean active);

    /**
     * Получить объявление по его id.
     * @param id уникальный идентификатор объявления
     * @return объявление
     */
    Advertisement getById(Long id);
}
