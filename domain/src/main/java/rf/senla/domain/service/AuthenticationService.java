package rf.senla.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.domain.exception.ErrorMessage;
import rf.senla.domain.exception.ResetPasswordTokenException;

import java.util.Date;
import java.util.UUID;

/**
 * Сервис для аутентификации и регистрации пользователей.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("java:S6809")
public class AuthenticationService implements IAuthenticationService {
    private final IJwtService jwtService;
    private final IUserService userService;
    private final IEmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public String signUp(User user) {
        log.info("Регистрация пользователя {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBoosted(Boolean.FALSE);
        user.setRole(Role.ROLE_USER);
        user.setRating(0.0);
        userService.create(user);
        String token = jwtService.generateToken(user);
        log.info("Удалось зарегистрировать пользователя {}", user.getUsername());
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public String signIn(User user) {
        log.info("Авторизация пользователя {}", user.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        UserDetails currentUser = userService.userDetailsService().loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(currentUser);
        log.info("Удалось авторизовать пользователя {}", currentUser.getUsername());
        return token;
    }

    @Override
    @Transactional
    public void sendResetPasswordEmail(String email, String username) {
        log.info("Вызван метод отправки восстановления пароля для пользователя с логином {} на почту {}",
                username, email);
        User user = createPasswordResetToken(username);
        emailService.sendResetPasswordEmail(email, user.getResetPasswordToken());
        log.info("Удачно отправлен токен восстановления на почту {} для пользователя {}", email, username);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByResetPasswordToken(String token) {
        log.info("Получение пользователя по токену восстановления пароля {}", token);
        User user = userService.getByResetPasswordToken(token);
        log.info("Удалось получить пользователя по токену восстановления пароля {}", user.getUsername());
        return validateResetPasswordToken(user);
    }

    @Override
    @Transactional
    public void updatePassword(String token, String newPassword) {
        User user = userService.getByResetPasswordToken(token);
        log.info("Обновления пароля пользователю {}", user.getUsername());
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiryDate(null);
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
        log.info("Удалось обновить пароль пользователю {}", user.getUsername());
    }

    /**
     * Служебный метод проводит валидацию токена восстановления пароля.
     * @param user пользователь
     * @return пользователь
     */
    private static User validateResetPasswordToken(User user) {
        log.info("Валидация токена восстановления пароля пользователя {}", user.getUsername());
        if (user.getResetPasswordTokenExpiryDate().before(new Date())) {
            log.error("Валидация токена восстановления пароля провалена");
            throw new ResetPasswordTokenException(ErrorMessage.TOKEN_EXPIRED.getMessage());
        }
        log.info("Валидация токена восстановления пароля пользователя {} прошла успешно", user.getUsername());

        return user;
    }

    /**
     * Создание токена восстановления пароля для пользователя
     * @param username логин пользователя
     * @return пользователь
     */
    private User createPasswordResetToken(String username) {
        User user = userService.getByUsername(username);
        log.info("Генерация токена восстановления пароля пользователю {}", user.getUsername());
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiryDate(new Date(System.currentTimeMillis() + 3600 * 1000));  // 1 hour expiry
        user = userService.save(user);
        log.info("Удалось сгенерировать токен восстановления пароля пользователю {}", user.getUsername());
        return user;
    }
}
