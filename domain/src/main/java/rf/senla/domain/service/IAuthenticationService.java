package rf.senla.domain.service;

import rf.senla.domain.entity.User;

/**
 * Интерфейс сервиса для работы с аутентификацией и регистрацией пользователей.
 */
public interface IAuthenticationService {
    /**
     * Регистрация пользователя
     * @param user данные пользователя
     * @return токен
     */
    String signUp(User user);

    /**
     * Аутентификация пользователя
     * @param user данные пользователя
     * @return токен
     */
    String signIn(User user);

    /**
     * Отправка токена восстановления пароля на почту
     * @param email почта
     * @param username имя пользователя
     */
    void sendResetPasswordEmail(String email, String username);

    /**
     * Получение пользователя по токену восстановления пароля
     * @param token токен восстановления пароля
     * @return пользователь
     */
    User getByResetPasswordToken(String token);

    /**
     * Обновление пароля для пользователя
     * @param token токен восстановления пароля
     * @param newPassword новый пароль
     */
    void updatePassword(String token, String newPassword);
}
