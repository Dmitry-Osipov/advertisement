package rf.senla.web.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.AdvertisementStatus;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.exception.NoEntityException;
import rf.senla.domain.exception.TechnicalException;
import rf.senla.domain.repository.AdvertisementRepository;
import rf.senla.domain.repository.CommentRepository;
import rf.senla.domain.repository.MessageRepository;
import rf.senla.domain.service.AdvertisementService;
import rf.senla.domain.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@SuppressWarnings("java:S5778")
class AdvertisementServiceTest {
    private List<User> users;
    private List<Advertisement> advertisements;
    @Mock
    private UserService userService;
    @Mock
    private AdvertisementRepository advertisementRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private AdvertisementService sut;

    @BeforeEach
    public void setUp() {
        User user1 = User.builder()
                .id(1L)
                .username("user123")
                .password("$2a$10$.PSEN9QPfyvpoXh9RQzdy.Wlok/5KO.iwcNYQOe.mmVgdTAeOO0AW")
                .phoneNumber("+7(123)456-78-90")
                .rating(0.0)
                .email("storm-yes@yandex.ru")
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
                .role(Role.ROLE_ADMIN)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();

        Advertisement advertisement1 = Advertisement.builder()
                .id(1L)
                .user(user1)
                .price(1000)
                .headline("Smartphone")
                .description("A portable device combining the functions of a mobile phone and a computer, typically " +
                        "offering internet access, touchscreen interface, and various applications.")
                .status(AdvertisementStatus.ACTIVE)
                .boosted(false)
                .build();
        Advertisement advertisement2 = Advertisement.builder()
                .id(2L)
                .user(user2)
                .price(2000)
                .headline("Laptop")
                .description("A portable computer that is small and light enough to be used on one's lap, typically " +
                        "with a clamshell form factor and a built-in keyboard and display.")
                .status(AdvertisementStatus.ACTIVE)
                .boosted(false)
                .build();
        Advertisement advertisement3 = Advertisement.builder()
                .id(3L)
                .user(user3)
                .price(2000)
                .headline("Headphones")
                .description("A pair of small speakers worn over the ears to listen to audio from a connected device " +
                        "such as a music player, smartphone, or computer.")
                .status(AdvertisementStatus.ACTIVE)
                .boosted(false)
                .build();
        Advertisement advertisement4 = Advertisement.builder()
                .id(4L)
                .user(user4)
                .price(4000)
                .headline("Backpack")
                .description("A bag with shoulder straps that allows it to be carried on one's back, typically used " +
                        "for carrying personal belongings, books, or electronic devices.")
                .status(AdvertisementStatus.ACTIVE)
                .boosted(false)
                .build();
        Advertisement advertisement5 = Advertisement.builder()
                .id(5L)
                .user(user5)
                .price(5000)
                .headline("Sunglasses")
                .description("Eyewear designed to protect the eyes from sunlight or glare, typically featuring " +
                        "tinted lenses and frames that cover a larger area around the eyes.")
                .status(AdvertisementStatus.ACTIVE)
                .boosted(false)
                .build();
        Advertisement advertisement6 = Advertisement.builder()
                .id(6L)
                .user(user6)
                .price(5000)
                .headline("Watch")
                .description("A small timepiece worn on the wrist or carried in a pocket, typically designed to show " +
                        "the time and often other information such as date, day of the week, or chronograph functions.")
                .status(AdvertisementStatus.ACTIVE)
                .boosted(false)
                .build();
        Advertisement advertisement7 = Advertisement.builder()
                .id(7L)
                .user(user7)
                .price(7000)
                .headline("Sneakers")
                .description("Casual athletic shoes with a flexible sole and typically made of canvas or leather, " +
                        "suitable for walking, running, or other sports activities.")
                .status(AdvertisementStatus.ACTIVE)
                .boosted(false)
                .build();
        Advertisement advertisement8 = Advertisement.builder()
                .id(8L)
                .user(user8)
                .price(8000)
                .headline("Umbrella")
                .description("A portable device consisting of a collapsible canopy supported by a central rod, used " +
                        "for protection against rain or sunlight.")
                .status(AdvertisementStatus.ACTIVE)
                .boosted(false)
                .build();
        Advertisement advertisement9 = Advertisement.builder()
                .id(9L)
                .user(user9)
                .price(10000)
                .headline("Camera")
                .description("A device used to capture and record still images or moving pictures, typically " +
                        "consisting of a lens, image sensor, and electronic components.")
                .status(AdvertisementStatus.ACTIVE)
                .boosted(false)
                .build();
        Advertisement advertisement10 = Advertisement.builder()
                .id(10L)
                .user(user10)
                .price(10000)
                .headline("Perfume")
                .description("A fragrant liquid typically made from essential oils and alcohol, applied to the skin " +
                        "or clothing to produce a pleasant scent.")
                .status(AdvertisementStatus.ACTIVE)
                .boosted(false)
                .build();

        users = new ArrayList<>(List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11,
                user12, user13, user14, user15));
        advertisements = new ArrayList<>(List.of(advertisement1, advertisement2, advertisement3, advertisement4,
                advertisement5, advertisement6, advertisement7, advertisement8, advertisement9, advertisement10));
    }

    @AfterEach
    public void tearDown() {
        users = null;
        advertisements = null;
    }

    @Test
    void getAllWithCorrectDataDoesNotThrowException() {
        when(advertisementRepository.findAllWithActiveStatus(anyInt(), anyInt(), anyString(), any()))
                .thenReturn(advertisements);

        assertDoesNotThrow(() -> sut.getAll(0, 5000, "one", Pageable.ofSize(20)));

        verify(advertisementRepository, times(1))
                .findAllWithActiveStatus(anyInt(), anyInt(), anyString(), any());
    }

    @Test
    void getAllWithIncorrectDataThrowsException() {
        when(advertisementRepository.findAllWithActiveStatus(anyInt(), anyInt(), anyString(), any()))
                .thenReturn(advertisements);

        assertThrows(TechnicalException.class,
                () -> sut.getAll(50000, 0, null, Pageable.ofSize(20)));

        verify(advertisementRepository, times(0))
                .findAllWithActiveStatus(anyInt(), anyInt(), anyString(), any());
    }

    @Test
    void getAllByUserWithActiveStatusDoesNotThrowException() {
        when(userService.getByUsername(anyString())).thenReturn(users.getFirst());
        when(advertisementRepository.findByUser(any(), any(), any())).thenReturn(advertisements);

        assertDoesNotThrow(() -> sut.getAll(anyString(), true, Pageable.ofSize(20)));

        verify(userService, times(1)).getByUsername(anyString());
        verify(advertisementRepository, times(1)).findByUser(any(), any(), any());
    }

    @Test
    void getAllByUserWithAnyStatusDoesNotThrowException() {
        when(userService.getByUsername(anyString())).thenReturn(users.getFirst());
        when(advertisementRepository.findByUser(any(), any(), any())).thenReturn(advertisements);

        assertDoesNotThrow(() -> sut.getAll(anyString(), false, Pageable.ofSize(20)));

        verify(userService, times(1)).getByUsername(anyString());
        verify(advertisementRepository, times(1)).findByUser(any(), any(), any());
    }

    @Test
    void createDoestNotThrowException() {
        Advertisement expected = advertisements.getFirst();
        User user = users.getFirst();
        when(userService.getByUsername(anyString())).thenReturn(user);
        when(advertisementRepository.existsById(anyLong())).thenReturn(false);
        when(advertisementRepository.save(any())).thenReturn(expected);

        assertDoesNotThrow(() -> sut.create(expected, user));

        verify(advertisementRepository, times(1)).existsById(anyLong());
        verify(advertisementRepository, times(1)).save(any());
        verify(userService, times(1)).getByUsername(anyString());
    }

    @Test
    void createThrowsEntityContainedException() {
        Advertisement expected = advertisements.getFirst();
        User user = users.getFirst();
        when(advertisementRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(EntityContainedException.class, () -> sut.create(expected, user));

        verify(advertisementRepository, times(1)).existsById(anyLong());
        verify(advertisementRepository, times(0)).save(any());
        verify(userService, times(0)).getByUsername(anyString());
    }

    @Test
    void updateByUserDoesNotThrowException() {
        Advertisement expected = advertisements.getFirst();
        User user = users.getFirst();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(advertisementRepository.save(any())).thenReturn(expected);

        assertDoesNotThrow(() -> sut.update(expected, user));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(1)).save(any());
    }

    @Test
    void updateByUserThrowsNoEntityException() {
        Advertisement expected = advertisements.getFirst();
        User user = users.getFirst();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoEntityException.class, () -> sut.update(expected, user));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(0)).save(any());
    }

    @Test
    void updateByUserThrowsAccessDeniedException() {
        Advertisement expected = advertisements.getFirst();
        User user = users.getLast();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        assertThrows(AccessDeniedException.class, () -> sut.update(expected, user));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(0)).save(any());
    }

    @Test
    void updateByAdminDoesNotThrowException() {
        Advertisement expected = advertisements.getFirst();
        when(userService.getByUsername(anyString())).thenReturn(users.getFirst());
        when(advertisementRepository.existsById(anyLong())).thenReturn(true);
        when(advertisementRepository.save(any())).thenReturn(expected);

        assertDoesNotThrow(() -> sut.update(expected));

        verify(userService, times(1)).getByUsername(anyString());
        verify(advertisementRepository, times(1)).existsById(anyLong());
        verify(advertisementRepository, times(1)).save(any());
    }

    @Test
    void updateByAdminThrowsNoEntityException() {
        Advertisement expected = advertisements.getFirst();
        when(advertisementRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NoEntityException.class, () -> sut.update(expected));

        verify(userService, times(0)).getByUsername(anyString());
        verify(advertisementRepository, times(1)).existsById(anyLong());
        verify(advertisementRepository, times(0)).save(any());
    }

    @Test
    void deleteByUserDoesNotThrowException() {
        User user = users.getFirst();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.of(advertisements.getFirst()));

        assertDoesNotThrow(() -> sut.delete(anyLong(), user));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(1)).deleteById(anyLong());
        verify(commentRepository, times(1)).deleteByAdvertisement_Id(anyLong());
        verify(messageRepository, times(1)).deleteByAdvertisement_Id(anyLong());
    }

    @Test
    void deleteByUserThrowsNoEntityException() {
        User user = users.getFirst();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoEntityException.class, () -> sut.delete(anyLong(), user));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(0)).deleteById(anyLong());
        verify(commentRepository, times(0)).deleteByAdvertisement_Id(anyLong());
        verify(messageRepository, times(0)).deleteByAdvertisement_Id(anyLong());
    }

    @Test
    void deleteByUserThrowsAccessDeniedException() {
        User user = users.getLast();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.of(advertisements.getFirst()));

        assertThrows(AccessDeniedException.class, () -> sut.delete(anyLong(), user));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(0)).deleteById(anyLong());
        verify(commentRepository, times(0)).deleteByAdvertisement_Id(anyLong());
        verify(messageRepository, times(0)).deleteByAdvertisement_Id(anyLong());
    }

    @Test
    void deleteByAdminDoesNotThrowException() {
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.of(advertisements.getFirst()));

        assertDoesNotThrow(() -> sut.delete(anyLong()));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(1)).deleteById(anyLong());
        verify(commentRepository, times(1)).deleteByAdvertisement_Id(anyLong());
        verify(messageRepository, times(1)).deleteByAdvertisement_Id(anyLong());
    }

    @Test
    void deleteByAdminThrowsNoEntityException() {
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoEntityException.class, () -> sut.delete(anyLong()));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(0)).deleteById(anyLong());
        verify(commentRepository, times(0)).deleteByAdvertisement_Id(anyLong());
        verify(messageRepository, times(0)).deleteByAdvertisement_Id(anyLong());
    }

    @Test
    void getByIdDoesNotThrowException() {
        Advertisement expected = advertisements.getFirst();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        assertDoesNotThrow(() -> sut.getById(anyLong()));

        verify(advertisementRepository, times(1)).findById(anyLong());
    }

    @Test
    void getByIdThrowsNoEntityException() {
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoEntityException.class, () -> sut.getById(anyLong()));

        verify(advertisementRepository, times(1)).findById(anyLong());
    }

    @Test
    void sellDoesNotThrowException() {
        Advertisement expected = advertisements.getFirst();
        User user = users.getFirst();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(advertisementRepository.save(any())).thenReturn(expected);

        assertDoesNotThrow(() -> sut.sell(anyLong(), user));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(1)).save(any());
    }

    @Test
    void sellThrowsNoEntityException() {
        User user = users.getFirst();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoEntityException.class, () -> sut.sell(anyLong(), user));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(0)).save(any());
    }

    @Test
    void sellThrowsAccessDeniedException() {
        Advertisement advertisement = advertisements.getFirst();
        User user = users.getLast();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.of(advertisement));

        assertThrows(AccessDeniedException.class, () -> sut.sell(anyLong(), user));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(0)).save(any());
    }

    @Test
    void boostDoesNotThrowException() {
        Advertisement advertisement = advertisements.getFirst();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.of(advertisement));
        when(advertisementRepository.save(any())).thenReturn(advertisement);

        assertDoesNotThrow(() -> sut.boost(anyLong(), users.getFirst()));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(1)).save(any());
    }

    @Test
    void boostWithIncorrectUserThrowsException() {
        Advertisement advertisement = advertisements.getFirst();
        when(advertisementRepository.findById(anyLong())).thenReturn(Optional.of(advertisement));
        when(advertisementRepository.save(any())).thenReturn(advertisement);

        assertThrows(AccessDeniedException.class, () -> sut.boost(anyLong(), users.getLast()));

        verify(advertisementRepository, times(1)).findById(anyLong());
        verify(advertisementRepository, times(0)).save(any());
    }
}
