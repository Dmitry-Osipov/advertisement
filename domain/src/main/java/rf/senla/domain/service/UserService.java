package rf.senla.domain.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Rating;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.domain.exception.ErrorMessage;
import rf.senla.domain.repository.RatingRepository;
import rf.senla.domain.repository.UserRepository;

import java.util.List;

/**
 * Сервис для управления пользователями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("java:S6809")
public class UserService implements IUserService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository repository;
    private final RatingRepository ratingRepository;

    @Transactional
    @Override
    public User save(User entity) {
        log.info("Сохранение пользователя {}", entity);
        User user = repository.save(entity);
        log.info("Пользователь {} сохранён", user);
        return user;
    }

    @Transactional
    @Override
    public User update(User entity) {
        log.info("Обновление пользователя {}", entity);

        if (!repository.existsByUsername(entity.getUsername())) {
            log.error("Не найден пользователь {}", entity);
            throw new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage());
        }

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setRating(getRating(entity));
        User user = save(entity);
        log.info("Пользователь успешно обновлён {}", user);
        return user;
    }

    @Transactional
    @Override
    public void delete(User entity) {
        log.info("Удаление пользователя {}", entity);
        String username = entity.getUsername();
        if (!repository.existsByUsername(username)) {
            throw new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage());
        }

        repository.deleteByUsername(username);
        log.info("Пользователь {} удалён успешно", entity);
    }

    // TODO: remove
    @Transactional
    @Override
    public List<User> getAll() {
        log.info("Получение списка 10 топовых пользователей");
        List<User> list = correctRating(repository.findAll(getPageable(0, 10)).getContent());
        successfullyListLog(list);
        return list;
    }

    @Transactional
    @Override
    public List<User> getAll(Integer page, Integer size) {
        log.info("Получение списка пользователей с номером страницы {} и размером страницы {}", page, size);
        List<User> list = correctRating(repository.findAll(getPageable(page, size)).getContent());
        successfullyListLog(list);
        return list;
    }

    @Transactional
    @Override
    public User getByUsername(String username) {
        try {
            log.info("Получение пользователя по логину {}", username);
            User user = repository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage()));
            user.setRating(getRating(user));

            log.info("Пользователь {} успешно получен", user);
            return user;
        } catch (UsernameNotFoundException e) {
            log.error("Не удалось получить пользователя по логину {}", username);
            throw e;
        }
    }

    @Transactional
    @Override
    public User updatePassword(String username, String oldPassword, String newPassword) {
        log.info("Обновление пароля для пользователя {}", username);
        User user = getByUsername(username);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException(ErrorMessage.PASSWORDS_DO_NOT_MATCH.getMessage());
        }

        newPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newPassword);
        User entity = save(user);
        log.info("Удалось обновить пароль для пользователя {}", entity);
        return entity;
    }

    @Transactional
    @Override
    public void setAdminRole(String username) {
        log.info("Установление роли админа для пользователя {}", username);
        User user = getByUsername(username);
        user.setRole(Role.ROLE_ADMIN);
        save(user);
        log.info("Удалось установить роль админа для пользователя {}", username);
    }

    @Transactional
    @Override
    public void create(User user) {
        log.info("Создание нового пользователя {}", user.getUsername());
        if (repository.existsByUsername(user.getUsername())) {
            log.error("Не удалось создать пользователя из-за дублирования имени для пользователя {}", user);
            throw new EntityContainedException(ErrorMessage.USERNAME_ALREADY_EXISTS.getMessage());
        }

        if (repository.existsByEmail(user.getEmail())) {
            log.error("Не удалось создать пользователя из-за дублирования почты для пользователя {}", user);
            throw new EntityContainedException(ErrorMessage.EMAIL_ALREADY_EXISTS.getMessage());
        }

        save(user);
        log.info("Удалось создать нового пользователя {}", user.getUsername());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetailsService userDetailsService() {
        log.info("Вызов метода userDetailsService");
        return this::getByUsername;
    }

    @Transactional
    @Override
    public User setBoosted(UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Установка продвижения для пользователя {}", user);
        user.setBoosted(true);
        user = save(user);
        log.info("Удалось установить продвижение для пользователя {}", user);
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public User getByResetPasswordToken(String token) {
        try {
            log.info("Получение пользователя по токену восстановления пароля {}", token);
            User user = repository.findByResetPasswordToken(token)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage()));
            log.info("Удалось получить пользователя {} по токену восстановления пароля {}", user, token);
            return user;
        } catch (EntityNotFoundException e) {
            log.error("Не удалось получить пользователя по токену восстановления пароля {}", token);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Double getUserRating(User user) {
        log.error("Получение рейтинга для пользователя {}", user);
        Double rating = ratingRepository.findByAverageRatingByRecipient(user);
        if (rating != null) {
            log.info("Для пользователя {} получен рейтинг {}", user, rating);
        } else {
            log.warn("У пользователя {} нет рейтинга", user);
        }
        return rating;
    }

    @Transactional
    @Override
    public User addEvaluation(UserDetails sender, String username, Integer evaluation) {
        User currentUser = getByUsername(sender.getUsername());
        User recipient = getByUsername(username);
        log.info("Пользователь {} пытается поставить оценку {} пользователю {}", currentUser, evaluation, recipient);

        if (ratingRepository.existsBySenderAndRecipient(currentUser, recipient)) {
            log.error("Пользователь {} уже ставил оценку пользователю {}", currentUser, recipient);
            throw new EntityContainedException(ErrorMessage.SENDER_ALREADY_VOTED.getMessage());
        }

        Rating rating = Rating.builder()
                .sender(currentUser)
                .recipient(recipient)
                .evaluation(evaluation)
                .build();
        ratingRepository.save(rating);
        recipient.setRating(getUserRating(recipient));

        log.info("Пользователю {} удалось поставить оценку {} для пользователя {}", currentUser, evaluation, recipient);
        return update(recipient);
    }

    /**
     * Служебный метод корректирует рейтинг каждого пользователя переданного списка
     * @param users список пользователей
     * @return список пользователей
     */
    private List<User> correctRating(List<User> users) {
        log.info("Вызов корректировки рейтинга для списка пользователей: {}", users);
        List<User> result = users.stream()
                .map(user -> {
                    user.setRating(getRating(user));
                    return user;
                })
                .toList();
        log.info("Удалось скорректировать рейтинг для списка пользователей: {}", result);
        return result;
    }

    /**
     * Служебный метод получает рейтинг пользователя либо устанавливает дефолтное значение
     * @param user пользователь
     * @return рейтинг пользователя
     */
    private Double getRating(User user) {
        log.info("Вызван метод получения рейтинга пользователя {}", user);
        Double rating = getUserRating(user);
        if (rating == null) {
            rating = 0.0;
            log.warn("У пользователя {} не было рейтинга, установлено значение по умолчанию {}", user, rating);
        }

        log.info("Пользователю {} установлен рейтинг {}", user, rating);
        return rating;
    }

    // TODO: remove
    /**
     * Служебный метод возвращает пагинацию топовых пользователей.
     * @param page Порядковый номер страницы.
     * @param size Размер страницы.
     * @return Пагинация
     */
    private Pageable getPageable(Integer page, Integer size) {
        log.info("Вызван метод формирования пагинации для номера страницы - {} и размера страницы - {}", page, size);
        if (page == null) {
            page = 0;
            log.info("Для номера страницы присвоено дефолтное значение - {}", page);
        }

        if (size == null) {
            size = 10;
            log.info("Для размера страницы присвоено дефолтное значение - {}", size);
        }

        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boosted", "rating"));
    }

    /**
     * Служебный метод логирует данные списка
     * @param list список
     */
    private static void successfullyListLog(List<User> list) {
        log.info("Получен список из {} объявлений: {}", list.size(), list);
    }
}
