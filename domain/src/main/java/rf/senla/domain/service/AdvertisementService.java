package rf.senla.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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
@SuppressWarnings("java:S6809")
public class AdvertisementService implements IAdvertisementService {
    private final IUserService userService;
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
    public Advertisement update(Advertisement entity, UserDetails sender) {
        log.info("Обновление объявления {}", entity);
        Advertisement advertisement = getById(entity.getId());
        checkSenderAndCurrentUser(sender, advertisement.getUser());
        advertisement.setPrice(entity.getPrice());
        advertisement.setHeadline(entity.getHeadline());
        advertisement.setDescription(entity.getDescription());
        advertisement = repository.save(advertisement);
        log.info("Удалось обновить объявление {}", advertisement);
        return advertisement;
    }

    @Override
    @Transactional
    public Advertisement update(Advertisement advertisement) {
        log.info("Обновление объявления {} админом", advertisement);

        if (!repository.existsById(advertisement.getId())) {
            log.error("Отсутствует объявление {}", advertisement);
            throw new NoEntityException(ErrorMessage.NO_ADVERTISEMENT_FOUND.getMessage());
        }

        advertisement.setUser(userService.getByUsername(advertisement.getUser().getUsername()));
        advertisement = repository.save(advertisement);
        log.info("Удалось обновить объявление {} админом", advertisement);
        return advertisement;
    }

    @Override
    @Transactional
    public void delete(Long id, UserDetails sender) {
        log.info("Удаление объявления с ID {}", id);
        Advertisement advertisement = getById(id);
        checkSenderAndCurrentUser(sender, advertisement.getUser());
        deleteAdvertisement(advertisement);
        log.info("Удалось удалить объявление {}", advertisement);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Удаление объявления с ID {} админом", id);
        Advertisement advertisement = getById(id);
        deleteAdvertisement(advertisement);
        log.info("Удалось удалить объявление {} админом", advertisement);
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

    @Override
    @Transactional
    public Advertisement sell(Long id, UserDetails sender) {
        log.info("Вызван метод продажи объявления с ID {} пользователем {}", id, sender.getUsername());
        Advertisement advertisement = getById(id);
        checkSenderAndCurrentUser(sender, advertisement.getUser());
        advertisement.setStatus(AdvertisementStatus.SOLD);
        advertisement = repository.save(advertisement);
        log.info("Удалось продать объявление {}", advertisement);
        return advertisement;
    }

    /**
     * Служебный метод проверяет совпадение переданного пользователя и пользователя из объявления
     * @param currentUser переданный пользователь
     * @param sender пользователь объявления
     */
    private static void checkSenderAndCurrentUser(UserDetails currentUser, UserDetails sender) {
        String expectedUsername = currentUser.getUsername();
        if (!sender.getUsername().equals(expectedUsername)) {
            log.error("Переданный пользователь {} и пользователь объявления {} не совпали",
                    expectedUsername, sender);
            throw new AccessDeniedException(ErrorMessage.SENDER_MISMATCH.getMessage());
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
     * Служебный метод удаляет объявление в репозиториях комментариев, сообщений и объявлений
     * @param advertisement объявление
     */
    private void deleteAdvertisement(Advertisement advertisement) {
        Long id = advertisement.getId();
        commentRepository.deleteByAdvertisement_Id(id);
        messageRepository.deleteByAdvertisement_Id(id);
        repository.deleteById(id);
    }
}
