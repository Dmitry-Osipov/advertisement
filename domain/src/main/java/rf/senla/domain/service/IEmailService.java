package rf.senla.domain.service;

/**
 * Интерфейс сервиса для работы с почтой
 */
public interface IEmailService {
    /**
     * Метод отправки сообщения с дальнейшими инструкциями по восстановлению пароля
     * @param email почта
     * @param token токен восстановления пароля
     */
    void sendResetPasswordEmail(String email, String token);
}
