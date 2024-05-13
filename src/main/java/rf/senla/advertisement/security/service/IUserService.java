package rf.senla.advertisement.security.service;

import org.springframework.dao.OptimisticLockingFailureException;
import rf.senla.advertisement.domain.service.IService;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.exception.UserMismatchException;

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
     * @throws UserMismatchException если текущий и переданный пользователи не совпадают
     * @throws IllegalArgumentException если пароли не совпали
     * @throws OptimisticLockingFailureException - если сущность использует оптимистическую блокировку и имеет атрибут
     * version со значением, отличным от того, что находится в хранилище персистентности. Также выбрасывается, если
     * предполагается, что сущность присутствует, но не существует в базе данных
     */
    User updatePassword(String username, String oldPassword, String newPassword);

    /**
     * Выдача прав администратора пользователю
     * @param username имя пользователя
     */
    void setAdminRole(String username);
}
