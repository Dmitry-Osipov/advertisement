package rf.senla.advertisement.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rf.senla.advertisement.security.entity.User;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью пользователя в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Найти пользователя по его имени пользователя.
     * @param username имя пользователя
     * @return объект Optional, содержащий пользователя, если найден, иначе пустой Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверить существование пользователя по его имени пользователя.
     * @param username имя пользователя
     * @return {@code true}, если пользователь существует, иначе {@code false}
     */
    boolean existsByUsername(String username);

    /**
     * Проверить существование пользователя по его адресу электронной почты.
     * @param email адрес электронной почты пользователя
     * @return {@code true}, если пользователь существует, иначе {@code false}
     */
    boolean existsByEmail(String email);

    /**
     * Удалить пользователя по его имени пользователя.
     * @param username имя пользователя
     */
    @Modifying
    @Query("DELETE FROM User u WHERE u.username = :username")
    void deleteByUsername(String username);

    /**
     * Получить пользователя по токену восстановления пароля.
     * @param resetPasswordToken токен восстановления пароля
     * @return объект Optional, содержащий пользователя, если найден, иначе пустой Optional
     */
    Optional<User> findByResetPasswordToken(String resetPasswordToken);
}
