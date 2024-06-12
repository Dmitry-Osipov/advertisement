package rf.senla.web.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import rf.senla.domain.exception.ResetPasswordTokenException;
import rf.senla.domain.service.EmailService;
import rf.senla.domain.service.JwtService;
import rf.senla.domain.service.UserService;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.domain.service.AuthenticationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthenticationServiceTest {
    private List<User> users;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private AuthenticationService sut;

    @BeforeEach
    public void setUp() {
        User user1 = User.builder()
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
        User user2 = User.builder()
                .id(2L)
                .username("cool_guy")
                .password("$2a$10$pT4a.wJbqJ9S8egWxAsQDuGoW2/JtO3/sFNqKRywS1my1HrVk.riq")
                .phoneNumber("+7(456)789-01-23")
                .rating(100.0)
                .email("john.doe@gmail.com")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user3 = User.builder()
                .id(3L)
                .username("adventure_lover")
                .password("$2a$10$vUso4/3dhelewojnFMwe3eEuuYbDjhB2w8DD7whkUNI68AEmozmVO")
                .phoneNumber("+7(789)012-34-56")
                .rating(200.0)
                .email("jane.smith@yahoo.com")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user4 = User.builder()
                .id(4L)
                .username("soccer_fanatic")
                .password("$2a$10$9FTmJyd2uuYAhCs8zS29IOFu7L1A3Sgtwm7y2zk40AuAyOX7jk9YC")
                .phoneNumber("+7(234)567-89-01")
                .rating(200.0)
                .email("alexander.wilson@hotmail.com")
                .boosted(true)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user5 = User.builder()
                .id(5L)
                .username("bookworm")
                .password("$2a$10$7o45UjE92My4RzkKcp8PvOamK4PcbudQV3/Yb2F0C/3tfjG.46cDK")
                .phoneNumber("+7(567)890-12-34")
                .rating(300.0)
                .email("emily.jones@outlook.com")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user6 = User.builder()
                .id(6L)
                .username("tech_guru")
                .password("$2a$10$DCVbgoez.57rY4y24LWnL.IeUcbmf.QczNAkAaHFs00Jv0tvy/2Uq")
                .phoneNumber("+7(890)123-45-67")
                .rating(350.0)
                .email("david.brown@mail.ru")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user7 = User.builder()
                .id(7L)
                .username("music_lover")
                .password("$2a$10$rc70yvIMV6qt.uvtqgXY7eNUrlm7s9t0VVnmL10ZxQkSkChk3gr9q")
                .phoneNumber("+7(345)678-90-12")
                .rating(400.0)
                .email("sarah.wilson@icloud.com")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user8 = User.builder()
                .id(8L)
                .username("travel_bug")
                .password("$2a$10$Fy0RzoBw1LWvUu.G0SAoxOlDiVoLny4JcywrHCxxZZrRZyr5sMmxK")
                .phoneNumber("+7(678)901-23-45")
                .rating(500.0)
                .email("michael.johnson@aol.com")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user9 = User.builder()
                .id(9L)
                .username("fitness_freak")
                .password("$2a$10$J.nuQTavp.Q3J3X0ZtMutef1lsuZDA.icUtzpntSfh527ZCW1I3V.")
                .phoneNumber("+7(901)234-56-78")
                .rating(500.0)
                .email("laura.davis@yandex.ru")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user10 = User.builder()
                .id(10L)
                .username("movie_buff")
                .password("$2a$10$D5GA3XIYSPLuCg3kdhskSO3NrYToLWGGJo3CWIBXSMCINDfl2c5nC")
                .phoneNumber("+7(432)109-87-65")
                .rating(500.0)
                .email("james.miller@protonmail.com")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user11 = User.builder()
                .id(11L)
                .username("gaming_pro")
                .password("$2a$10$VeQVo/2UEOlQ3BO0zv6gJuUKY/Eeq8xSXg0mpMvNvfTTHWctXeE62")
                .phoneNumber("+7(210)987-65-43")
                .rating(0.0)
                .email("olivia.taylor@live.com")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user12 = User.builder()
                .id(12L)
                .username("art_enthusiast")
                .password("$2a$10$xWKGPXDUuxxnpTI8EkAZeeKubMAyjAxWQQKz.CtNlOrvph3FKoJoW")
                .phoneNumber("+7(098)765-43-21")
                .rating(0.0)
                .email("william.anderson@inbox.lv")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user13 = User.builder()
                .id(13L)
                .username("nature_lover")
                .password("$2a$10$TRq3w57OEgUfuZLXSYCRS..9LmukEPmrRVHv9QIed.b850ky/cIJy")
                .phoneNumber("+7(876)543-21-09")
                .rating(0.0)
                .email("sophia.thomas@bk.ru")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user14 = User.builder()
                .id(14L)
                .username("foodie")
                .password("$2a$10$TjJmASFFMmKK1iVgAwZaDel8TgWEurRYL.8jTs4ECE9FPaW13TbXG")
                .phoneNumber("+7(953)180-00-61")
                .rating(0.0)
                .email("jacob.moore@rambler.ru")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();
        User user15 = User.builder()
                .id(15L)
                .username("admin")
                .password("$2a$10$/v7NnuEmQ8wvQg6oK.RFkeX1fPF25xzQIFYSz2M7BTVLkbi1RExYe")
                .phoneNumber("+7(902)902-98-11")
                .rating(0.0)
                .email("dimaosipov00@gmail.com")
                .boosted(false)
                .role(Role.ROLE_ADMIN)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();

        users = new ArrayList<>(List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11,
                user12, user13, user14, user15));
    }

    @AfterEach
    public void tearDown() {
        users = null;
    }

    @Test
    void signUpReturnsCorrectData() {
        User user = users.getFirst();
        String expected = "secret token";
        when(jwtService.generateToken(any())).thenReturn(expected);
        when(passwordEncoder.encode(any())).thenReturn("password");

        String actual = assertDoesNotThrow(() -> sut.signUp(user));

        assertEquals(expected, actual);
        verify(userService, times(1)).create(any());
        verify(jwtService, times(1)).generateToken(any());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void signInReturnsCorrectData() {
        User user = users.getFirst();
        String expected = "secret token";
        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn(expected);

        String actual = assertDoesNotThrow(() -> sut.signIn(user));

        assertEquals(expected, actual);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    void sendResetPasswordEmailDoesNotThrowException() {
        User user = users.getFirst();
        when(userService.getByUsername(anyString())).thenReturn(user);
        when(userService.save(any())).thenReturn(user);

        assertDoesNotThrow(() -> sut.sendResetPasswordEmail(user.getEmail(), user.getUsername()));

        verify(userService, times(1)).getByUsername(anyString());
        verify(userService, times(1)).save(any());
        verify(emailService, times(1)).sendResetPasswordEmail(anyString(), anyString());
    }

    @Test
    void getByPasswordResetTokenDoesNotThrowException() {
        User user = users.getFirst();
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiryDate(new Date(System.currentTimeMillis() + 3600 * 1000));
        when(userService.getByResetPasswordToken(anyString())).thenReturn(user);

        assertDoesNotThrow(() -> sut.getByResetPasswordToken(token));

        verify(userService, times(1)).getByResetPasswordToken(anyString());
    }

    @Test
    void getByPasswordResetTokenThrowsResetPasswordTokenException() {
        User user = users.getFirst();
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiryDate(new Date(System.currentTimeMillis() - 3600 * 1000));
        when(userService.getByResetPasswordToken(anyString())).thenReturn(user);

        assertThrows(ResetPasswordTokenException.class, () -> sut.getByResetPasswordToken(token));

        verify(userService, times(1)).getByResetPasswordToken(anyString());
    }

    @Test
    void updatePasswordDoesNotThrowException() {
        User user = users.getFirst();
        when(userService.save(any())).thenReturn(user);
        when(userService.getByResetPasswordToken(anyString())).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("password");

        assertDoesNotThrow(() -> sut.updatePassword("token", "password123"));

        verify(userService, times(1)).save(any());
        verify(userService, times(1)).getByResetPasswordToken(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
    }
}