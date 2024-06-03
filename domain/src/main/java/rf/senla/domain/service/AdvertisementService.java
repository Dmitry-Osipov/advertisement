package rf.senla.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.exception.ErrorMessage;
import rf.senla.domain.exception.NoEntityException;
import rf.senla.domain.exception.TechnicalException;
import rf.senla.domain.repository.AdvertisementRepository;
import rf.senla.domain.entity.User;

import java.util.List;

/**
 * Сервис для работы с объявлениями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdvertisementService implements IAdvertisementService {
    private final AdvertisementRepository repository;
    private final IUserService userService;

    @Transactional
    @Override
    public Advertisement save(Advertisement entity) {
        log.info("Сохранение объявления {}", entity);
        if (entity.getId() != null && repository.existsById(entity.getId())) {
            log.error("Не удалось сохранить объявление {}", entity);
            throw new EntityContainedException(ErrorMessage.ADVERTISEMENT_ALREADY_EXISTS.getMessage());
        }

        Advertisement advertisement = repository.save(entity);
        log.info("Удалось сохранить объявление {}", advertisement);
        return advertisement;
    }

    @Transactional
    @Override
    public Advertisement update(Advertisement entity) {
        log.info("Обновление объявления {}", entity);
        if (!repository.existsById(entity.getId())) {
            log.error("Не удалось обновить объявление {}", entity);
            throw new NoEntityException(ErrorMessage.NO_ADVERTISEMENT_FOUND.getMessage());
        }

        Advertisement advertisement = repository.save(entity);
        log.info("Удалось обновить объявление {}", advertisement);
        return advertisement;
    }

    @Transactional
    @Override
    public void delete(Advertisement entity) {
        log.info("Удаление объявления {}", entity);
        if (!repository.existsById(entity.getId())) {
            log.error("Не удалось удалить объявление {}", entity);
            throw new NoEntityException(ErrorMessage.NO_ADVERTISEMENT_FOUND.getMessage());
        }

        repository.delete(entity);
        log.info("Удалось удалить объявление {}", entity);
    }

    // TODO: remove
    @Transactional(readOnly = true)
    @Override
    public List<Advertisement> getAll() {
        log.info("Получение списка объявлений");
        List<Advertisement> list = repository.findAllInOrderByUserRating(
                getPageable("rating", 0, 10));
        successfullyListLog(list);
        return list;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Advertisement> getAll(Integer min, Integer max, String headline, String sortBy,
                                      Integer page, Integer size) {
        log.info("Получение списка объявлений по заголовку {}, с сортировкой {}, в промежутке цен {} и {}, с " +
                "номером страницы {} и размером страницы {}", headline, sortBy, min, max, page, size);

        if (min == null) {
            min = 0;
            log.info("Задано дефолтное минимальное значение цены {}", min);
        }

        if (max == null) {
            max = Integer.MAX_VALUE;
            log.info("Задано дефолтное максимальное значение цены {}", max);
        }

        checkPrices(min, max);

        Pageable pageable = getPageable(sortBy, page, size);

        List<Advertisement> list;
        if (headline == null) {
            list = repository.findByPriceBetweenInOrder(min, max, pageable);
        } else {
            list = repository.findByPriceBetweenAndHeadlineIgnoreCaseInOrder(min, max, headline, pageable);
        }
        successfullyListLog(list);

        return list;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Advertisement> getAll(User user, String sortBy, Boolean active, Integer page, Integer size) {
        log.info("Получение списка объявлений пользователя - {}, с сортировкой - {}, флаг только активных " +
                "объявлений - {}, с номером страницы - {}, с размером страницы - {}", user, sortBy, active, page, size);
        Pageable pageable = getPageable(sortBy, page, size);

        List<Advertisement> list;
        if (Boolean.FALSE.equals(active) || active == null) {
            list = repository.findByUserInOrderWithAnyStatus(user, pageable);
        } else {
            list = repository.findByUserInOrder(user, pageable);
        }
        successfullyListLog(list);

        return list;
    }

    @Transactional(readOnly = true)
    @Override
    public Advertisement getById(Long id) {
        try {
            log.info("Получение объявления с ID {}", id);
            Advertisement advertisement = repository.findById(id)
                    .orElseThrow(() -> new NoEntityException(ErrorMessage.NO_ADVERTISEMENT_FOUND.getMessage()));
            log.info("Получено объявление {}", advertisement);
            return advertisement;
        } catch (NoEntityException e) {
            log.error("Не удалось получить объявление с ID {}", id);
            throw e;
        }
    }

    /**
     * Служебный метод проводит проверку цен.
     * @param min минимальная цена
     * @param max максимальная цена
     * @throws TechnicalException если минимальная цена больше максимальной или какая-либо цена меньше 0
     */
    private static void checkPrices(Integer min, Integer max) {
        log.info("Вызван метод проверки для цен {} и {}", min, max);

        if (min < 0) {
            log.error("Минимальная цена меньше 0");
            throw new TechnicalException(ErrorMessage.PRICE_IS_NEGATIVE.getMessage());
        }

        if (min > max) {
            log.error("Максимальная цена меньше минимальной");
            throw new TechnicalException(ErrorMessage.MIN_PRICE_IS_HIGHEST.getMessage());
        }

        log.info("Проверка для цен {} и {} пройдена успешно", min, max);
    }

    // TODO: remove
    /**
     * Служебный метод на основе переданного типа сортировки формирует пагинацию.
     * @param sortBy способ сортировки
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return пагинация
     */
    private static Pageable getPageable(String sortBy, Integer page, Integer size) {
        log.info("Вызван метод формирования пагинации для номера страницы - {}, размера страницы - {}, " +
                "с сортировкой - {}", page, size, sortBy);

        if (page == null) {
            page = 0;
            log.info("Для номера страницы присвоено дефолтное значение - {}", page);
        }

        if (size == null) {
            size = 10;
            log.info("Для размера страницы присвоено дефолтное значение - {}", size);
        }

        Sort sort;
        switch (sortBy) {
            case "asc" -> sort = Sort.by(Sort.Direction.ASC, "price");
            case "desc" -> sort = Sort.by(Sort.Direction.DESC, "price");
            case null, default -> sort = Sort.by(Sort.Direction.DESC, "user.boosted", "user.rating");
        }
        log.info("Для типа сортировки присвоено значение - {}", sort);

        return PageRequest.of(page, size, sort);
    }

    /**
     * Служебный метод логирует данные списка
     * @param list список
     */
    private static void successfullyListLog(List<Advertisement> list) {
        log.info("Получен список из {} объявлений: {}", list.size(), list);
    }
}
