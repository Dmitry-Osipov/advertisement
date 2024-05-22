package rf.senla.advertisement.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.security.dto.JwtAuthenticationResponse;
import rf.senla.advertisement.security.dto.SignInRequest;
import rf.senla.advertisement.security.dto.SignUpRequest;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.exception.ErrorMessage;
import rf.senla.advertisement.security.exception.ResetPasswordTokenException;

import java.util.Date;
import java.util.UUID;

/**
 * Сервис для аутентификации и регистрации пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final IUserService userService;
    private final IJwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
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
        return new JwtAuthenticationResponse(jwtService.generateToken(user));
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails user = userService.userDetailsService().loadUserByUsername(request.getUsername());
        return new JwtAuthenticationResponse(jwtService.generateToken(user));
    }

    @Transactional
    @Override
    public void createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiryDate(new Date(System.currentTimeMillis() + 3600 * 1000));  // 1 hour expiry
        userService.save(user);
    }

    @Override
    public User getByResetPasswordToken(String token) {
        User user = userService.getByResetPasswordToken(token);
        return validateResetPasswordToken(user);
    }

    @Transactional
    @Override
    public void updatePassword(User user, String newPassword) {
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiryDate(null);
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
    }

    /**
     * Служебный метод проводит валидацию токена восстановления пароля.
     * @param user пользователь
     * @return пользователь
     */
    private static User validateResetPasswordToken(User user) {
        if (user.getResetPasswordTokenExpiryDate().before(new Date())) {
            throw new ResetPasswordTokenException(ErrorMessage.TOKEN_EXPIRED.getMessage());
        }

        return user;
    }
}
