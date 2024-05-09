package rf.senla.advertisement.security.service;

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

/**
 * Сервис для аутентификации и регистрации пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userService.create(user);
        return new JwtAuthenticationResponse(jwtService.generateToken(user));
    }

    /**
     * Аутентификация пользователя
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails user = userService.userDetailsService().loadUserByUsername(request.getUsername());
        return new JwtAuthenticationResponse(jwtService.generateToken(user));
    }
}
