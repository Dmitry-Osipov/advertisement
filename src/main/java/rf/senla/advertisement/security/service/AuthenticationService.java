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
}
