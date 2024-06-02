package rf.senla.advertisement.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import rf.senla.advertisement.domain.service.IService;
import rf.senla.advertisement.security.entity.User;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 */
public interface IUserService extends IService<User> {
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
     * @return Обновлённый пользователь
     */
    User setBoosted();

    /**
     * Получить список пользователей с пагинацией.
     * @param page Порядковый номер страницы.
     * @param size Размер страницы.
     * @return Список пользователей.
     */
    List<User> getAll(Integer page, Integer size);

    /**
     * Метод получения пользователя по токену восстановления пароля
     * @param token токен восстановления пароля
     * @return пользователь
     */
    User getByResetPasswordToken(String token);
}
