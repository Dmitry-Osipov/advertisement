package rf.senla.advertisement.security.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Интерфейс сервиса для работы с JWT.
 */
public interface IJwtService {
    /**
     * Извлечение имени пользователя из токена
     * @param token токен
     * @return имя пользователя
     */
    String extractUsername(String token);

    /**
     * Генерация токена
     * @param userDetails данные пользователя
     * @return токен
     */
    String generateToken(UserDetails userDetails);

    /**
     * Проверка токена на валидность
     * @param token токен
     * @param userDetails данные пользователя
     * @return true, если токен валиден
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}
