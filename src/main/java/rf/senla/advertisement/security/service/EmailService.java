package rf.senla.advertisement.security.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.security.exception.EmailException;

/**
 * Сервис для работы с почтовой рассылкой.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements IEmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendResetPasswordEmail(String email, String token) {
        log.info("Отправка ссылки восстановления пароля на почту {}", email);
        String subject = "Reset Password";
        String content = "To reset your password, click the link below:\n" +
                "http://localhost:8080/api/auth/reset-password?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            log.info("Удалось отправить ссылку восстановления пароля на почту {}", email);
        } catch (MessagingException | MailException e) {
            log.error("Не удалось отправить ссылку восстановления пароля на почту {} с заголовком {} и содержимым {}",
                    email, subject, content);
            throw new EmailException(e);
        }
    }
}
