package rf.senla.advertisement.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import rf.senla.domain.exception.EmailException;
import rf.senla.domain.service.EmailService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private MimeMessage mimeMessage;
    @InjectMocks
    private EmailService sut;

    @Test
    void sendResetPasswordEmailDoesNotThrowsException() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertDoesNotThrow(() -> sut.sendResetPasswordEmail("storm-yes@yandex.ru", "secret token"));

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendResetPasswordEmailThrowsEmailException() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(EmailException.class).when(mailSender).send(mimeMessage);

        assertThrows(EmailException.class,
                () -> sut.sendResetPasswordEmail("storm-yes@yandex.ru", "secret token"));

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }
}
