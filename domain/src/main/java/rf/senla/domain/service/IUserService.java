package rf.senla.domain.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import rf.senla.domain.entity.User;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 */
public interface IUserService {
    /**
     * Сохранение пользователя
     * @param user пользователь
     * @return сохранённый пользователь
     */
    User save(User user);

    /** Обновление пользователя
     * @param user пользователь
     * @return обновлённый пользователь
     */
    User update(User user);

    /** Удаление пользователя
     * @param username имя пользователя
     */
    void deleteByUsername(String username);

    /**
     * Получение пользователя по имени пользователя
     * @return пользователь
     */
    User getByUsername(String username);

    /** Обновление пароля
     * @param username имя пользователя
     * @param oldPassword старый пароль
     * @param newPassword новый пароль
     * @return обновлённый пользователь
     */
    User updatePassword(String username, String oldPassword, String newPassword);

    /**
     * Выдача прав администратора пользователю
     * @param username имя пользователя
     */
    void setAdminRole(String username);

    /**
     * Создание пользователя
     */
    void create(User user);

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     * @return пользователь
     */
    UserDetailsService userDetailsService();

    /**
     * Продвижение пользователя
     * @param userDetails пользователь, которого требуется бустануть
     * @return Обновлённый пользователь
     */
    User setBoosted(UserDetails userDetails);

    /**
     * Получить список пользователей с пагинацией.
     * @param pageable пагинация
     * @return Список пользователей.
     */
    List<User> getAll(Pageable pageable);

    /**
     * Метод получения пользователя по токену восстановления пароля
     * @param token токен восстановления пароля
     * @return пользователь
     */
    User getByResetPasswordToken(String token);

    /**
     * Метод получения рейтинга пользователя
     * @param user пользователь, для которого требуется получить его рейтинг
     * @return рейтинг
     */
    Double getUserRating(User user);

    /**
     * Метод добавления рейтинга пользователю
     * @param sender отправитель
     * @param username получатель
     * @param evaluation рейтинг
     * @return обновлённый получатель
     */
    User addEvaluation(UserDetails sender, String username, Integer evaluation);
}
