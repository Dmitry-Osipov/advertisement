package rf.senla.web.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.exception.NoEntityException;
import rf.senla.web.dto.SignInRequest;
import rf.senla.web.dto.SignUpRequest;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.domain.service.AuthenticationService;
import rf.senla.domain.service.IJwtService;
import rf.senla.domain.service.IUserService;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthenticationServiceTest {
    @Mock
    private IUserService userService;
    @Mock
    private IJwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private AuthenticationService sut;

    @Test
    @Disabled
    void signUpDoesNotThrowException() {
        SignUpRequest expected = new SignUpRequest();
        expected.setUsername("superuser");
        expected.setPassword("123456789");
        expected.setEmail("superuser@example.ru");
        expected.setPhoneNumber("+7(900)800-00-12");
        when(jwtService.generateToken(any())).thenReturn("secret token");

//        assertDoesNotThrow(() -> sut.signUp(expected));

        verify(userService, times(1)).create(any());
        verify(jwtService, times(1)).generateToken(any());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    @Disabled
    void signUpThrowsEntityContainedException() {
        SignUpRequest expected = new SignUpRequest();
        expected.setUsername("user123");
        expected.setPassword("123456789");
        expected.setEmail("superuser@example.ru");
        expected.setPhoneNumber("+7(900)800-00-12");
        doThrow(EntityContainedException.class).when(userService).create(any());

//        assertThrows(EntityContainedException.class, () -> sut.signUp(expected));

        verify(userService, times(1)).create(any());
        verify(jwtService, times(0)).generateToken(any());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    @Disabled
    void signInDoesNotThrowException() {
        SignInRequest expected = new SignInRequest();
        expected.setUsername("user123");
        expected.setPassword("password123");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                expected.getUsername(), expected.getPassword(), Collections.emptyList());
        String token = "mock token";
        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtService.generateToken(any())).thenReturn(token);

//        assertEquals(token, assertDoesNotThrow(() -> sut.signIn(expected)).getToken());

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(expected.getUsername(), expected.getPassword()));
        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    @Disabled
    void signInThrowsNoEntityException() {
        SignInRequest expected = new SignInRequest();
        expected.setUsername("user123");
        expected.setPassword("password123");
        String token = "mock token";
        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(NoEntityException.class);
        when(jwtService.generateToken(any())).thenReturn(token);

//        assertThrows(NoEntityException.class, () -> sut.signIn(expected));

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(expected.getUsername(), expected.getPassword()));
        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
        verify(jwtService, times(0)).generateToken(any());
    }

    @Test
    @Disabled
    void createPasswordResetTokenDoesNotThrowException() {
        User expected = User.builder()
                .id(1L)
                .username("user123")
                .password("$2a$10$.PSEN9QPfyvpoXh9RQzdy.Wlok/5KO.iwcNYQOe.mmVgdTAeOO0AW")
                .phoneNumber("+7(123)456-78-90")
                .rating(0.0)
                .email("storm-yes@yandex.ru")
                .boosted(true)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        String token = UUID.randomUUID().toString();
        expected.setResetPasswordToken(token);
        expected.setResetPasswordTokenExpiryDate(new Date(System.currentTimeMillis() + 3600 * 1000));
        when(userService.save(any())).thenReturn(expected);

        assertDoesNotThrow(() -> sut.sendResetPasswordEmail(expected.getEmail(), expected.getUsername()));

        // TODO: обновить тест
        verify(userService, times(1)).save(any());
    }

    @Test
    void getByResetPasswordTokenDoesNotThrowException() {
        User expected = User.builder()
                .id(1L)
                .username("user123")
                .password("$2a$10$.PSEN9QPfyvpoXh9RQzdy.Wlok/5KO.iwcNYQOe.mmVgdTAeOO0AW")
                .phoneNumber("+7(123)456-78-90")
                .rating(0.0)
                .email("storm-yes@yandex.ru")
                .boosted(true)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        String token = UUID.randomUUID().toString();
        expected.setResetPasswordToken(token);
        expected.setResetPasswordTokenExpiryDate(new Date(System.currentTimeMillis() + 3600 * 1000));
        when(userService.getByResetPasswordToken(anyString())).thenReturn(expected);

        assertDoesNotThrow(() -> sut.getByResetPasswordToken(token));

        verify(userService, times(1)).getByResetPasswordToken(anyString());
    }

    @Test
    void getByResetPasswordTokenThrowsNoEntityException() {
        String token = UUID.randomUUID().toString();
        when(userService.getByResetPasswordToken(anyString())).thenThrow(NoEntityException.class);

        assertThrows(NoEntityException.class, () -> sut.getByResetPasswordToken(token));

        verify(userService, times(1)).getByResetPasswordToken(anyString());
    }

    @Test
    @Disabled
    void updatePasswordDoesNotThrowException() {
        User expected = User.builder()
                .id(1L)
                .username("user123")
                .password("$2a$10$.PSEN9QPfyvpoXh9RQzdy.Wlok/5KO.iwcNYQOe.mmVgdTAeOO0AW")
                .phoneNumber("+7(123)456-78-90")
                .rating(0.0)
                .email("storm-yes@yandex.ru")
                .boosted(true)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        when(userService.save(any())).thenReturn(expected);

        assertDoesNotThrow(() -> sut.updatePassword(expected.getResetPasswordToken(), "password123"));

        verify(userService, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(anyString());
    }
}
