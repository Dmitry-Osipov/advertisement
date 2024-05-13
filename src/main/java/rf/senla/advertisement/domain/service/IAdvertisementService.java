package rf.senla.advertisement.domain.service;

import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.exception.NoEntityException;

import java.util.List;

/**
 * Сервис для работы с объявлениями.
 */
public interface IAdvertisementService extends IService<Advertisement> {
    /**
     * Получить объявление по его заголовку.
     * @param headline заголовок объявления
     * @return найденное объявление
     * @throws NoEntityException если объявление не найдено
     */
    List<Advertisement> getAllByHeadline(String headline);
}
