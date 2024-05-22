package rf.senla.advertisement.security.service;

import rf.senla.advertisement.security.dto.JwtAuthenticationResponse;
import rf.senla.advertisement.security.dto.SignInRequest;
import rf.senla.advertisement.security.dto.SignUpRequest;
import rf.senla.advertisement.security.entity.User;

/**
 * Интерфейс сервиса для работы с аутентификацией и регистрацией пользователей.
 */
public interface IAuthenticationService {
    /**
     * Регистрация пользователя
     * @param request данные пользователя
     * @return токен
     */
    JwtAuthenticationResponse signUp(SignUpRequest request);

    /**
     * Аутентификация пользователя
     * @param request данные пользователя
     * @return токен
     */
    JwtAuthenticationResponse signIn(SignInRequest request);

    /**
     * Создание токена восстановления пароля
     * @param user пользователь, для которого создаётся токен
     */
    void createPasswordResetToken(User user);

    /**
     * Получение пользователя по токену восстановления пароля
     * @param token токен восстановления пароля
     * @return пользователь
     */
    User getByResetPasswordToken(String token);

    /**
     * Обновление пароля для пользователя
     * @param user пользователь
     * @param newPassword новый пароль
     */
    void updatePassword(User user, String newPassword);
}
