package rf.senla.web.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import rf.senla.domain.entity.Rating;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.domain.exception.NoEntityException;
import rf.senla.domain.repository.AdvertisementRepository;
import rf.senla.domain.repository.CommentRepository;
import rf.senla.domain.repository.MessageRepository;
import rf.senla.domain.repository.RatingRepository;
import rf.senla.domain.repository.UserRepository;
import rf.senla.domain.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@SuppressWarnings("java:S5778")
class UserServiceTest {
    private List<User> users;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AdvertisementRepository advertisementRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RatingRepository ratingRepository;
    @InjectMocks
    private UserService sut;

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
    void getAllDoesNotThrowException() {
        when(userRepository.findAll((Pageable) any())).thenReturn(Page.empty());

        assertDoesNotThrow(() -> sut.getAll(Pageable.ofSize(20)));

        verify(userRepository, times(1)).findAll((Pageable) any());
    }

    @Test
    void getByUsernameDoesNotThrowException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(users.getFirst()));

        assertDoesNotThrow(() -> sut.getByUsername(anyString()));

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void getByUsernameThrowsUsernameNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> sut.getByUsername(anyString()));

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void updatePasswordDoesNotThrowException() {
        User user = users.getFirst();
        String expected = user.getPassword();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        User actual = assertDoesNotThrow(
                () -> sut.updatePassword(anyString(), "password123", "secret_password"));

        assertNotEquals(expected, actual.getPassword());
        assert new BCryptPasswordEncoder().matches("secret_password", actual.getPassword());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void updatePasswordThrowsUsernameNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> sut.updatePassword(anyString(), "password123", "secret_password"));

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void updatePasswordThrowsIllegalArgumentException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(users.getFirst()));

        assertThrows(IllegalArgumentException.class,
                () -> sut.updatePassword(anyString(), "secret_password", "password123"));

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void setAdminRoleDoesNotThrowException() {
        User expected = users.getFirst();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(expected));
        when(userRepository.save(any())).thenReturn(expected);

        assertDoesNotThrow(() -> sut.setAdminRole(anyString()));

        assertEquals(Role.ROLE_ADMIN, expected.getRole());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void setAdminRoleThrowsUsernameNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> sut.setAdminRole(anyString()));

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void setBoostedDoesNotThrowException() {
        User expected = users.getFirst();
        when(userRepository.save(any())).thenReturn(expected);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(expected));

        User actual = assertDoesNotThrow(() -> sut.setBoosted(expected));

        assertTrue(actual.getBoosted());
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void setBoostedThrowsUsernameNotFoundException() {
        User expected = users.getFirst();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> sut.setBoosted(expected));

        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void getByResetPasswordTokenDoesNotThrowException() {
        when(userRepository.findByResetPasswordToken(anyString())).thenReturn(Optional.of(users.getFirst()));

        assertDoesNotThrow(() -> sut.getByResetPasswordToken(anyString()));

        verify(userRepository, times(1)).findByResetPasswordToken(anyString());
    }

    @Test
    void getByResetPasswordTokenThrowsEntityNotFoundException() {
        when(userRepository.findByResetPasswordToken(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sut.getByResetPasswordToken(anyString()));

        verify(userRepository, times(1)).findByResetPasswordToken(anyString());
    }

    @Test
    void addEvaluationDoesNotThrowException() {
        User user = users.getFirst();
        when(ratingRepository.save(any())).thenReturn(new Rating());
        when(ratingRepository.existsBySenderAndRecipient(any(), any())).thenReturn(false);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        assertDoesNotThrow(() -> sut.addEvaluation(user, user.getUsername(), anyInt()));

        verify(ratingRepository, times(1)).save(any());
        verify(ratingRepository, times(1)).existsBySenderAndRecipient(any(), any());
        verify(userRepository, times(2)).findByUsername(anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void addEvaluationThrowsEntityContainedException() {
        User user = users.getFirst();
        when(ratingRepository.existsBySenderAndRecipient(any(), any())).thenReturn(true);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThrows(EntityContainedException.class, () -> sut.addEvaluation(user, user.getUsername(), anyInt()));

        verify(ratingRepository, times(0)).save(any());
        verify(ratingRepository, times(1)).existsBySenderAndRecipient(any(), any());
        verify(userRepository, times(2)).findByUsername(anyString());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void addEvaluationThrowsUsernameNotFoundException() {
        User user = users.getFirst();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> sut.addEvaluation(user, user.getUsername(), anyInt()));

        verify(ratingRepository, times(0)).save(any());
        verify(ratingRepository, times(0)).existsBySenderAndRecipient(any(), any());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void saveDoesNotThrowsException() {
        User user = users.getFirst();
        when(userRepository.save(any())).thenReturn(user);

        assertDoesNotThrow(() -> sut.save(user));

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void createDoesNotThrowException() {
        User expected = users.getFirst();
        when(userRepository.save(any())).thenReturn(expected);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        assertDoesNotThrow(() -> sut.create(expected));

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void createThrowsEntityContainedException() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(EntityContainedException.class, () -> sut.create(users.getFirst()));

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(0)).existsByEmail(anyString());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void updateDoesNotThrowException() {
        User user = users.getFirst();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        assertDoesNotThrow(() -> sut.update(user));

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateThrowsEntityContainedException() {
        User user = users.getFirst();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(EntityContainedException.class, () -> sut.update(user));

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(0)).existsByEmail(anyString());
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateThrowsNoEntityException() {
        User user = users.getFirst();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoEntityException.class, () -> sut.update(user));

        verify(userRepository, times(0)).existsByUsername(anyString());
        verify(userRepository, times(0)).existsByEmail(anyString());
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteByUsernameDoesNotThrowException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(users.getFirst()));

        assertDoesNotThrow(() -> sut.deleteByUsername(anyString()));

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).deleteByUsername(anyString());
        verify(messageRepository, times(1)).deleteBySender_IdOrRecipient_Id(anyLong(), anyLong());
        verify(commentRepository, times(1)).deleteByUser_Id(anyLong());
        verify(commentRepository, times(1)).deleteByAdvertisement_User_Id(anyLong());
        verify(advertisementRepository, times(1)).deleteByUser_Id(anyLong());
        verify(ratingRepository, times(1)).deleteBySender_IdOrRecipient_Id(anyLong(), anyLong());
    }

    @Test
    void deleteByUsernameThrowsUsernameNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> sut.deleteByUsername(anyString()));

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(0)).deleteByUsername(anyString());
        verify(messageRepository, times(0)).deleteBySender_IdOrRecipient_Id(anyLong(), anyLong());
        verify(commentRepository, times(0)).deleteByUser_Id(anyLong());
        verify(commentRepository, times(0)).deleteByAdvertisement_User_Id(anyLong());
        verify(advertisementRepository, times(0)).deleteByUser_Id(anyLong());
        verify(ratingRepository, times(0)).deleteBySender_IdOrRecipient_Id(anyLong(), anyLong());
    }
}
