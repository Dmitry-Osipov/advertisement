package rf.senla.advertisement.security.service;

import rf.senla.advertisement.security.dto.JwtAuthenticationResponse;
import rf.senla.advertisement.security.dto.SignInRequest;
import rf.senla.advertisement.security.dto.SignUpRequest;

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
}
