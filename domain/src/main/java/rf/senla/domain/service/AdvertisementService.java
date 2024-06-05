package rf.senla.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.AdvertisementStatus;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.exception.ErrorMessage;
import rf.senla.domain.exception.NoEntityException;
import rf.senla.domain.exception.TechnicalException;
import rf.senla.domain.repository.AdvertisementRepository;
import rf.senla.domain.entity.User;
import rf.senla.domain.repository.CommentRepository;
import rf.senla.domain.repository.MessageRepository;

import java.util.List;

/**
 * Сервис для работы с объявлениями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdvertisementService implements IAdvertisementService {
    private final UserService userService;
    private final AdvertisementRepository repository;
    private final CommentRepository commentRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public Advertisement create(Advertisement entity, UserDetails sender) {
        log.info("Сохранение объявления {}", entity);
        if (entity.getId() != null && repository.existsById(entity.getId())) {
            log.error("Не удалось сохранить объявление {}", entity);
            throw new EntityContainedException(ErrorMessage.ADVERTISEMENT_ALREADY_EXISTS.getMessage());
        }

        entity.setUser(userService.getByUsername(sender.getUsername()));
        entity.setStatus(AdvertisementStatus.REVIEW);
        Advertisement advertisement = repository.save(entity);
        log.info("Удалось сохранить объявление {}", advertisement);
        return advertisement;
    }

    @Override
    @Transactional
    public Advertisement update(Advertisement entity) {
        log.info("Обновление объявления {}", entity);
        checkExistsById(entity, "Не удалось обновить объявление {}");
        Advertisement advertisement = repository.save(entity);
        log.info("Удалось обновить объявление {}", advertisement);
        return advertisement;
    }

    @Override
    @Transactional
    public void delete(Advertisement entity) {
        log.info("Удаление объявления {}", entity);
        checkExistsById(entity, "Не удалось удалить объявление {}");
        Long id = entity.getId();
        commentRepository.deleteByAdvertisement_Id(id);
        messageRepository.deleteByAdvertisement_Id(id);
        repository.deleteById(id);
        log.info("Удалось удалить объявление {}", entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Advertisement> getAll(Pageable  pageable) {
        log.info("Получение списка объявлений");
        List<Advertisement> list = repository.findAllWithActiveStatus(pageable);
        successfullyListLog(list);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Advertisement> getAll(Integer min, Integer max, String headline, Pageable pageable) {
        log.info("Получение списка объявлений по заголовку {}, в промежутке цен {} и {}, с пагинацией {}",
                headline, min, max, pageable);

        if (min > max) {
            log.error("Максимальная цена меньше минимальной");
            throw new TechnicalException(ErrorMessage.MIN_PRICE_IS_HIGHEST.getMessage());
        }

        List<Advertisement> list;
        if (headline == null) {
            list = repository.findByPriceBetweenWithActiveStatus(min, max, pageable);
        } else {
            list = repository.findByPriceBetweenAndHeadlineIgnoreCaseWithActiveStatus(min, max, headline, pageable);
        }
        successfullyListLog(list);

        return list;
    }

    @Override
    @Transactional
    public List<Advertisement> getAll(String username, Boolean active, Pageable pageable) {
        log.info("Получение списка объявлений пользователя - {}, флаг только активных объявлений - {}, " +
                "с пагинацией - {}", username, active, pageable);
        User user = userService.getByUsername(username);

        List<Advertisement> list;
        if (Boolean.FALSE.equals(active) || active == null) {
            list = repository.findByUser(user, pageable);
        } else {
            list = repository.findByUserWithActiveStatus(user, pageable);
        }
        successfullyListLog(list);

        return list;
    }

    @Override
    @Transactional(readOnly = true)
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
     * Служебный метод логирует данные списка
     * @param list список
     */
    private static void successfullyListLog(List<Advertisement> list) {
        log.info("Получен список из {} объявлений: {}", list.size(), list);
    }

    /**
     * Служебный метод проверяет содержание объявления в БД
     * @param entity объявление
     * @param s строка для записи лога
     */
    private void checkExistsById(Advertisement entity, String s) {
        if (!repository.existsById(entity.getId())) {
            log.error(s, entity);
            throw new NoEntityException(ErrorMessage.NO_ADVERTISEMENT_FOUND.getMessage());
        }
    }
}
