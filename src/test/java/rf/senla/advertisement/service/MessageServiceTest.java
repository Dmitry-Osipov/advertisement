package rf.senla.advertisement.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.entity.AdvertisementStatus;
import rf.senla.advertisement.domain.entity.Message;
import rf.senla.advertisement.domain.exception.EntityContainedException;
import rf.senla.advertisement.domain.exception.NoEntityException;
import rf.senla.advertisement.domain.repository.MessageRepository;
import rf.senla.advertisement.domain.service.MessageService;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageServiceTest {
    private List<User> users;
    private List<Advertisement> advertisements;
    private List<Message> messages;
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private MessageService sut;

    @BeforeEach
    public void setUp() {
        User user1 = User.builder()
                .id(1L)
                .username("user123")
                .password("$2a$10$.PSEN9QPfyvpoXh9RQzdy.Wlok/5KO.iwcNYQOe.mmVgdTAeOO0AW")
                .phoneNumber("+7(123)456-78-90")
                .rating(0)
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
                .rating(100)
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
                .rating(200)
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
                .rating(200)
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
                .rating(300)
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
                .rating(350)
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
                .rating(400)
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
                .rating(500)
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
                .rating(500)
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
                .rating(500)
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
                .rating(0)
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
                .rating(0)
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
                .rating(0)
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
                .rating(0)
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
                .rating(0)
                .email("dimaosipov00@gmail.com")
                .boosted(false)
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
                .build();
        Advertisement advertisement2 = Advertisement.builder()
                .id(2L)
                .user(user2)
                .price(2000)
                .headline("Laptop")
                .description("A portable computer that is small and light enough to be used on one's lap, typically " +
                        "with a clamshell form factor and a built-in keyboard and display.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
        Advertisement advertisement3 = Advertisement.builder()
                .id(3L)
                .user(user3)
                .price(2000)
                .headline("Headphones")
                .description("A pair of small speakers worn over the ears to listen to audio from a connected device " +
                        "such as a music player, smartphone, or computer.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
        Advertisement advertisement4 = Advertisement.builder()
                .id(4L)
                .user(user4)
                .price(4000)
                .headline("Backpack")
                .description("A bag with shoulder straps that allows it to be carried on one's back, typically used " +
                        "for carrying personal belongings, books, or electronic devices.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
        Advertisement advertisement5 = Advertisement.builder()
                .id(5L)
                .user(user5)
                .price(5000)
                .headline("Sunglasses")
                .description("Eyewear designed to protect the eyes from sunlight or glare, typically featuring " +
                        "tinted lenses and frames that cover a larger area around the eyes.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
        Advertisement advertisement6 = Advertisement.builder()
                .id(6L)
                .user(user6)
                .price(5000)
                .headline("Watch")
                .description("A small timepiece worn on the wrist or carried in a pocket, typically designed to show " +
                        "the time and often other information such as date, day of the week, or chronograph functions.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
        Advertisement advertisement7 = Advertisement.builder()
                .id(7L)
                .user(user7)
                .price(7000)
                .headline("Sneakers")
                .description("Casual athletic shoes with a flexible sole and typically made of canvas or leather, " +
                        "suitable for walking, running, or other sports activities.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
        Advertisement advertisement8 = Advertisement.builder()
                .id(8L)
                .user(user8)
                .price(8000)
                .headline("Umbrella")
                .description("A portable device consisting of a collapsible canopy supported by a central rod, used " +
                        "for protection against rain or sunlight.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
        Advertisement advertisement9 = Advertisement.builder()
                .id(9L)
                .user(user9)
                .price(10000)
                .headline("Camera")
                .description("A device used to capture and record still images or moving pictures, typically " +
                        "consisting of a lens, image sensor, and electronic components.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
        Advertisement advertisement10 = Advertisement.builder()
                .id(10L)
                .user(user10)
                .price(10000)
                .headline("Perfume")
                .description("A fragrant liquid typically made from essential oils and alcohol, applied to the skin " +
                        "or clothing to produce a pleasant scent.")
                .status(AdvertisementStatus.ACTIVE)
                .build();

        Message message1 = Message.builder()
                .id(1L)
                .advertisement(advertisement2)
                .sender(user1)
                .recipient(user2)
                .text("Hi! What can you tell me about Laptop?")
                .sentAt(LocalDateTime.now())
                .read(true)
                .build();
        Message message2 = Message.builder()
                .id(2L)
                .advertisement(advertisement2)
                .sender(user2)
                .recipient(user1)
                .text("Good Laptop, long battery life, nice screen")
                .sentAt(LocalDateTime.now())
                .read(true)
                .build();
        Message message3 = Message.builder()
                .id(3L)
                .advertisement(advertisement2)
                .sender(user5)
                .recipient(user1)
                .text("Hello, How many?")
                .sentAt(LocalDateTime.now())
                .read(false)
                .build();
        Message message4 = Message.builder()
                .id(4L)
                .advertisement(advertisement3)
                .sender(user2)
                .recipient(user3)
                .text("Good afternoon. Is that a bargain?")
                .sentAt(LocalDateTime.now())
                .read(true)
                .build();
        Message message5 = Message.builder()
                .id(5L)
                .advertisement(advertisement3)
                .sender(user3)
                .recipient(user2)
                .text("No")
                .sentAt(LocalDateTime.now())
                .read(true)
                .build();
        Message message6 = Message.builder()
                .id(6L)
                .advertisement(advertisement4)
                .sender(user3)
                .recipient(user4)
                .text("Greetings, what color is the backpack?")
                .sentAt(LocalDateTime.now())
                .read(true)
                .build();
        Message message7 = Message.builder()
                .id(7L)
                .advertisement(advertisement4)
                .sender(user4)
                .recipient(user3)
                .text("Hello! Blue")
                .sentAt(LocalDateTime.now())
                .read(true)
                .build();
        Message message8 = Message.builder()
                .id(8L)
                .advertisement(advertisement5)
                .sender(user4)
                .recipient(user5)
                .text("Why are you selling sunglasses?")
                .sentAt(LocalDateTime.now())
                .read(true)
                .build();
        Message message9 = Message.builder()
                .id(9L)
                .advertisement(advertisement5)
                .sender(user5)
                .recipient(user4)
                .text("Didn't need it. Do you want it?")
                .sentAt(LocalDateTime.now())
                .read(true)
                .build();
        Message message10 = Message.builder()
                .id(10L)
                .advertisement(advertisement6)
                .sender(user5)
                .recipient(user6)
                .text("What brand of watch?")
                .sentAt(LocalDateTime.now())
                .read(true)
                .build();

        users = new ArrayList<>(List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11,
                user12, user13, user14, user15));
        advertisements = new ArrayList<>(List.of(advertisement1, advertisement2, advertisement3, advertisement4,
                advertisement5, advertisement6, advertisement7, advertisement8, advertisement9, advertisement10));
        messages = new ArrayList<>(List.of(message1, message2, message3, message4, message5, message6, message7,
                message8, message9, message10));
    }

    @AfterEach
    public void tearDown() {
        users = null;
        advertisements = null;
        messages = null;
    }

    @Test
    @WithMockUser(username = "user123")
    void getAllDoesNotThrowException() {
        User user = users.getFirst();
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(messageRepository.findAllByUserId(anyLong(), any())).thenReturn(messages);

        assertDoesNotThrow(() -> sut.getAll());

        verify(messageRepository, times(1)).findAllByUserId(anyLong(), any());
    }

    @Test
    @WithMockUser(username = "user123")
    void getAllBetweenUsersDoesNotThrowException() {
        User user = users.getFirst();
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(messageRepository.findMessagesBetweenUsers(anyLong(), anyLong(), any())).thenReturn(messages);

        assertDoesNotThrow(() -> sut.getAll(users.getLast(), 0, 1));

        verify(messageRepository, times(1)).findMessagesBetweenUsers(anyLong(), anyLong(), any());
    }

    @Test
    void saveDoesNotThrowException() {
        Message expected = Message.builder()
                .id(11L)
                .advertisement(advertisements.getLast())
                .sender(users.getFirst())
                .recipient(users.getLast())
                .text("Hello!")
                .read(false)
                .build();
        when(messageRepository.existsById(anyLong())).thenReturn(false);
        when(messageRepository.save(any())).thenReturn(expected);

        assertDoesNotThrow(() -> sut.save(expected));

        verify(messageRepository, times(1)).existsById(anyLong());
        verify(messageRepository, times(1)).save(any());
    }

    @Test
    void saveThrowsEntityContainedException() {
        when(messageRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(EntityContainedException.class, () -> sut.save(messages.getLast()));

        verify(messageRepository, times(1)).existsById(anyLong());
        verify(messageRepository, times(0)).save(any());
    }

    @Test
    void updateDoesNotThrowException() {
        when(messageRepository.existsById(anyLong())).thenReturn(true);
        when(messageRepository.save(any())).thenReturn(messages.getLast());

        assertDoesNotThrow(() -> sut.update(messages.getLast()));

        verify(messageRepository, times(1)).existsById(anyLong());
        verify(messageRepository, times(1)).save(any());
    }

    @Test
    void updateThrowsNoEntityException() {
        Message expected = Message.builder()
                .id(11L)
                .advertisement(advertisements.getLast())
                .sender(users.getFirst())
                .recipient(users.getLast())
                .text("Hello!")
                .read(false)
                .build();
        when(messageRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NoEntityException.class, () -> sut.update(expected));

        verify(messageRepository, times(1)).existsById(anyLong());
        verify(messageRepository, times(0)).save(any());
    }

    @Test
    void deleteDoesNotThrowException() {
        when(messageRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> sut.delete(messages.getLast()));

        verify(messageRepository, times(1)).existsById(anyLong());
        verify(messageRepository, times(1)).delete(any());
    }

    @Test
    void deleteNoEntityException() {
        Message expected = Message.builder()
                .id(11L)
                .advertisement(advertisements.getLast())
                .sender(users.getFirst())
                .recipient(users.getLast())
                .text("Hello!")
                .read(false)
                .build();
        when(messageRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NoEntityException.class, () -> sut.delete(expected));

        verify(messageRepository, times(1)).existsById(anyLong());
        verify(messageRepository, times(0)).delete(any());
    }
}
