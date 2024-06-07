package rf.senla.domain.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
import rf.senla.domain.exception.NoEntityException;
import rf.senla.domain.repository.AdvertisementRepository;
import rf.senla.domain.repository.CommentRepository;
import rf.senla.domain.repository.MessageRepository;
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
    private final UserRepository repository;
    private final RatingRepository ratingRepository;
    private final MessageRepository messageRepository;
    private final CommentRepository commentRepository;
    private final AdvertisementRepository advertisementRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public User save(User entity) {
        log.info("Сохранение пользователя {}", entity);
        User user = repository.save(entity);
        log.info("Пользователь {} сохранён", user);
        return user;
    }

    @Override
    @Transactional
    public void create(User user) {
        log.info("Создание нового пользователя {}", user.getUsername());
        checkUsernameAndEmail(user.getUsername(), user.getEmail());
        save(user);
        log.info("Удалось создать нового пользователя {}", user.getUsername());
    }

    @Override
    @Transactional
    public User update(User user) {
        log.info("Обновление пользователя по его dto {}", user);
        User entity = repository.findById(user.getId())
                .orElseThrow(() -> new NoEntityException(ErrorMessage.USER_NOT_FOUND.getMessage()));
        String username = user.getUsername();
        String email = user.getEmail();
        checkUsernameAndEmail(username, email);
        entity.setUsername(username);
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setEmail(email);
        entity.setRating(getRating(entity));
        entity = save(entity);
        log.info("Пользователь успешно обновлён {}", entity);
        return entity;
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        User user = getByUsername(username);
        log.info("Начало процесса удаления пользователя {}", user);
        Long id = user.getId();
        messageRepository.deleteBySender_IdOrRecipient_Id(id, id);
        commentRepository.deleteByUser_Id(id);
        commentRepository.deleteByAdvertisement_User_Id(id);
        advertisementRepository.deleteByUser_Id(id);
        ratingRepository.deleteBySender_IdOrRecipient_Id(id, id);
        repository.deleteByUsername(username);
        log.info("Процесс удаления пользователя {} завершён успешно", user);
    }

    @Override
    @Transactional
    public List<User> getAll(Pageable pageable) {
        log.info("Получение списка пользователей с пагинацией {}", pageable);
        List<User> list = correctRating(repository.findAll(pageable).getContent());
        log.info("Получен список из {} объявлений: {}", list.size(), list);
        return list;
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void setAdminRole(String username) {
        log.info("Установление роли админа для пользователя {}", username);
        User user = getByUsername(username);
        user.setRole(Role.ROLE_ADMIN);
        save(user);
        log.info("Удалось установить роль админа для пользователя {}", username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsService userDetailsService() {
        log.info("Вызов метода userDetailsService");
        return this::getByUsername;
    }

    @Override
    @Transactional
    public User setBoosted(UserDetails userDetails) {
        User user = getByUsername(userDetails.getUsername());
        log.info("Установка продвижения для пользователя {}", user);
        user.setBoosted(true);
        user = save(user);
        log.info("Удалось установить продвижение для пользователя {}", user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional
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
        recipient.setRating(getRating(recipient));

        recipient = save(recipient);
        log.info("Пользователю {} удалось добавить рейтинг {} для пользователя {}", currentUser, rating, recipient);
        return recipient;
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

    /**
     * Служебный метод получения рейтинга пользователя
     * @param user пользователь, для которого требуется получить его рейтинг
     * @return рейтинг
     */
    private Double getUserRating(User user) {
        log.error("Получение рейтинга для пользователя {}", user);
        Double rating = ratingRepository.getAverageRatingByRecipient(user);
        if (rating != null) {
            log.info("Для пользователя {} получен рейтинг {}", user, rating);
        } else {
            log.warn("У пользователя {} нет рейтинга", user);
        }
        return rating;
    }

    /**
     * Служебный метод проверяет уникальность переданных логина и почты
     * @param username логин
     * @param email почта
     * @throws EntityContainedException если логин или почта уже есть в БД
     */
    private void checkUsernameAndEmail(String username, String email) {
        if (repository.existsByUsername(username)) {
            log.error("Логин {} уже существует в БД", username);
            throw new EntityContainedException(ErrorMessage.USERNAME_ALREADY_EXISTS.getMessage());
        }

        if (repository.existsByEmail(email)) {
            log.error("Почта {} уже существует в БД", email);
            throw new EntityContainedException(ErrorMessage.EMAIL_ALREADY_EXISTS.getMessage());
        }
    }
}
