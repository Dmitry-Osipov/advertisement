package rf.senla.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.dto.SignInRequest;
import rf.senla.domain.dto.SignUpRequest;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.domain.exception.ErrorMessage;
import rf.senla.domain.exception.ResetPasswordTokenException;
import rf.senla.domain.dto.JwtAuthenticationResponse;

import java.util.Date;
import java.util.UUID;

/**
 * Сервис для аутентификации и регистрации пользователей.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final IUserService userService;
    private final IJwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        log.info("Регистрация пользователя {}", request.getUsername());
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .rating(0)
                .password(passwordEncoder.encode(request.getPassword()))
                .boosted(false)
                .role(Role.ROLE_USER)
                .build();
        userService.create(user);
        String token = jwtService.generateToken(user);
        log.info("Удалось зарегистрировать пользователя {}", request.getUsername());
        return new JwtAuthenticationResponse(token);
    }

    @Transactional(readOnly = true)
    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        log.info("Авторизация пользователя {}", request.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails user = userService.userDetailsService().loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);
        log.info("Удалось авторизовать пользователя {}", request.getUsername());
        return new JwtAuthenticationResponse(token);
    }

    @Transactional
    @Override
    public void createPasswordResetToken(User user) {
        log.info("Генерация токена восстановления пароля пользователю {}", user.getUsername());
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiryDate(new Date(System.currentTimeMillis() + 3600 * 1000));  // 1 hour expiry
        userService.save(user);
        log.info("Удалось сгенерировать токен восстановления пароля пользователю {}", user.getUsername());
    }

    @Transactional(readOnly = true)
    @Override
    public User getByResetPasswordToken(String token) {
        log.info("Получение пользователя по токену восстановления пароля {}", token);
        User user = userService.getByResetPasswordToken(token);
        log.info("Удалось получить пользователя по токену восстановления пароля {}", user.getUsername());
        return validateResetPasswordToken(user);
    }

    @Transactional
    @Override
    public void updatePassword(User user, String newPassword) {
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
}
