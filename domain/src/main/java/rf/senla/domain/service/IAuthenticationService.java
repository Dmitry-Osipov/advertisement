package rf.senla.domain.service;

import rf.senla.domain.dto.SignInRequest;
import rf.senla.domain.dto.SignUpRequest;
import rf.senla.domain.entity.User;
import rf.senla.domain.dto.JwtAuthenticationResponse;

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
