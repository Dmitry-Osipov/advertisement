package rf.senla.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.AdvertisementStatus;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.web.dto.CreateMessageRequest;
import rf.senla.web.utils.AdvertisementMapper;
import rf.senla.web.utils.UserMapper;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class RestMessageControllerTest {
    private User sender;
    private User recipient;
    private Advertisement advertisement;
    // TODO: remove
//    private List<User> users;
//    private List<Advertisement> advertisements;
//    private List<Message> messages;
//    private List<UserDto> userDtos;
//    private List<AdvertisementDto> advertisementDtos;
//    private List<MessageDto> messageDtos;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AdvertisementMapper advertisementMapper;
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        sender = User.builder()
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

        recipient = User.builder()
                .id(2L)
                .username("cool_guy")
                .password("$2a$10$pT4a.wJbqJ9S8egWxAsQDuGoW2/JtO3/sFNqKRywS1my1HrVk.riq")
                .phoneNumber("+7(456)789-01-23")
                .rating(0.0)
                .email("john.doe@gmail.com")
                .boosted(false)
                .role(Role.ROLE_USER)
                .resetPasswordToken(null)
                .resetPasswordTokenExpiryDate(null)
                .build();

        advertisement = Advertisement.builder()
                .id(2L)
                .user(recipient)
                .price(2000)
                .headline("Laptop")
                .description("A portable computer that is small and light enough to be used on one's lap, typically " +
                        "with a clamshell form factor and a built-in keyboard and display.")
                .status(AdvertisementStatus.ACTIVE)
                .build();
    }

    @Test
    @Rollback
    @SneakyThrows
    void getUserCorrespondence() {
        User currentUser = sender;
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(currentUser, null, new ArrayList<>())
        );

        mockMvc.perform(get("/api/messages")
                        .param("username", "cool_guy")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @Rollback
    @SneakyThrows
    @WithMockUser("user123")
    void createMessage() {
        CreateMessageRequest message = new CreateMessageRequest();
        message.setAdvertisement(advertisementMapper.toDto(advertisement));
        message.setText("Hello!");
        message.setRecipient(userMapper.toDto(recipient));
        String request = objectMapper.writeValueAsString(message);

        mockMvc.perform(post("/api/messages")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sender").value(userMapper.toDto(sender)))
                .andExpect(jsonPath("$.recipient").value(userMapper.toDto(recipient)))
                .andExpect(jsonPath("$.advertisement").value(advertisementMapper.toDto(advertisement)))
                .andExpect(jsonPath("$.text").value("Hello!"))
                .andExpect(jsonPath("$.read").value(false));
    }
}
    // TODO: remove
//    @BeforeEach
//    void setUp() {
//        User user1 = User.builder()
//                .id(1L)
//                .username("user123")
//                .password("$2a$10$.PSEN9QPfyvpoXh9RQzdy.Wlok/5KO.iwcNYQOe.mmVgdTAeOO0AW")
//                .phoneNumber("+7(123)456-78-90")
//                .rating(0.0)
//                .email("storm-yes@yandex.ru")
//                .boosted(true)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto1 = UserDto.builder()
//                .id(1L)
//                .username("user123")
//                .phoneNumber("+7(123)456-78-90")
//                .rating(0.0)
//                .email("storm-yes@yandex.ru")
//                .boosted(true)
//                .role(Role.ROLE_USER)
//                .build();
//        User user2 = User.builder()
//                .id(2L)
//                .username("cool_guy")
//                .password("$2a$10$pT4a.wJbqJ9S8egWxAsQDuGoW2/JtO3/sFNqKRywS1my1HrVk.riq")
//                .phoneNumber("+7(456)789-01-23")
//                .rating(100.0)
//                .email("john.doe@gmail.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto2 = UserDto.builder()
//                .id(2L)
//                .username("cool_guy")
//                .phoneNumber("+7(456)789-01-23")
//                .rating(100.0)
//                .email("john.doe@gmail.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user3 = User.builder()
//                .id(3L)
//                .username("adventure_lover")
//                .password("$2a$10$vUso4/3dhelewojnFMwe3eEuuYbDjhB2w8DD7whkUNI68AEmozmVO")
//                .phoneNumber("+7(789)012-34-56")
//                .rating(200.0)
//                .email("jane.smith@yahoo.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto3 = UserDto.builder()
//                .id(3L)
//                .username("adventure_lover")
//                .phoneNumber("+7(789)012-34-56")
//                .rating(200.0)
//                .email("jane.smith@yahoo.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user4 = User.builder()
//                .id(4L)
//                .username("soccer_fanatic")
//                .password("$2a$10$9FTmJyd2uuYAhCs8zS29IOFu7L1A3Sgtwm7y2zk40AuAyOX7jk9YC")
//                .phoneNumber("+7(234)567-89-01")
//                .rating(200.0)
//                .email("alexander.wilson@hotmail.com")
//                .boosted(true)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto4 = UserDto.builder()
//                .id(4L)
//                .username("soccer_fanatic")
//                .phoneNumber("+7(234)567-89-01")
//                .rating(200.0)
//                .email("alexander.wilson@hotmail.com")
//                .boosted(true)
//                .role(Role.ROLE_USER)
//                .build();
//        User user5 = User.builder()
//                .id(5L)
//                .username("bookworm")
//                .password("$2a$10$7o45UjE92My4RzkKcp8PvOamK4PcbudQV3/Yb2F0C/3tfjG.46cDK")
//                .phoneNumber("+7(567)890-12-34")
//                .rating(300.0)
//                .email("emily.jones@outlook.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto5 = UserDto.builder()
//                .id(5L)
//                .username("bookworm")
//                .phoneNumber("+7(567)890-12-34")
//                .rating(300.0)
//                .email("emily.jones@outlook.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user6 = User.builder()
//                .id(6L)
//                .username("tech_guru")
//                .password("$2a$10$DCVbgoez.57rY4y24LWnL.IeUcbmf.QczNAkAaHFs00Jv0tvy/2Uq")
//                .phoneNumber("+7(890)123-45-67")
//                .rating(350.0)
//                .email("david.brown@mail.ru")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto6 = UserDto.builder()
//                .id(6L)
//                .username("tech_guru")
//                .phoneNumber("+7(890)123-45-67")
//                .rating(350.0)
//                .email("david.brown@mail.ru")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user7 = User.builder()
//                .id(7L)
//                .username("music_lover")
//                .password("$2a$10$rc70yvIMV6qt.uvtqgXY7eNUrlm7s9t0VVnmL10ZxQkSkChk3gr9q")
//                .phoneNumber("+7(345)678-90-12")
//                .rating(400.0)
//                .email("sarah.wilson@icloud.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto7 = UserDto.builder()
//                .id(7L)
//                .username("music_lover")
//                .phoneNumber("+7(345)678-90-12")
//                .rating(400.0)
//                .email("sarah.wilson@icloud.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user8 = User.builder()
//                .id(8L)
//                .username("travel_bug")
//                .password("$2a$10$Fy0RzoBw1LWvUu.G0SAoxOlDiVoLny4JcywrHCxxZZrRZyr5sMmxK")
//                .phoneNumber("+7(678)901-23-45")
//                .rating(500.0)
//                .email("michael.johnson@aol.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto8 = UserDto.builder()
//                .id(8L)
//                .username("travel_bug")
//                .phoneNumber("+7(678)901-23-45")
//                .rating(500.0)
//                .email("michael.johnson@aol.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user9 = User.builder()
//                .id(9L)
//                .username("fitness_freak")
//                .password("$2a$10$J.nuQTavp.Q3J3X0ZtMutef1lsuZDA.icUtzpntSfh527ZCW1I3V.")
//                .phoneNumber("+7(901)234-56-78")
//                .rating(500.0)
//                .email("laura.davis@yandex.ru")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto9 = UserDto.builder()
//                .id(9L)
//                .username("fitness_freak")
//                .phoneNumber("+7(901)234-56-78")
//                .rating(500.0)
//                .email("laura.davis@yandex.ru")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user10 = User.builder()
//                .id(10L)
//                .username("movie_buff")
//                .password("$2a$10$D5GA3XIYSPLuCg3kdhskSO3NrYToLWGGJo3CWIBXSMCINDfl2c5nC")
//                .phoneNumber("+7(432)109-87-65")
//                .rating(500.0)
//                .email("james.miller@protonmail.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto10 = UserDto.builder()
//                .id(10L)
//                .username("movie_buff")
//                .phoneNumber("+7(432)109-87-65")
//                .rating(500.0)
//                .email("james.miller@protonmail.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user11 = User.builder()
//                .id(11L)
//                .username("gaming_pro")
//                .password("$2a$10$VeQVo/2UEOlQ3BO0zv6gJuUKY/Eeq8xSXg0mpMvNvfTTHWctXeE62")
//                .phoneNumber("+7(210)987-65-43")
//                .rating(0.0)
//                .email("olivia.taylor@live.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto11 = UserDto.builder()
//                .id(11L)
//                .username("gaming_pro")
//                .phoneNumber("+7(210)987-65-43")
//                .rating(0.0)
//                .email("olivia.taylor@live.com")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user12 = User.builder()
//                .id(12L)
//                .username("art_enthusiast")
//                .password("$2a$10$xWKGPXDUuxxnpTI8EkAZeeKubMAyjAxWQQKz.CtNlOrvph3FKoJoW")
//                .phoneNumber("+7(098)765-43-21")
//                .rating(0.0)
//                .email("william.anderson@inbox.lv")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto12 = UserDto.builder()
//                .id(12L)
//                .username("art_enthusiast")
//                .phoneNumber("+7(098)765-43-21")
//                .rating(0.0)
//                .email("william.anderson@inbox.lv")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user13 = User.builder()
//                .id(13L)
//                .username("nature_lover")
//                .password("$2a$10$TRq3w57OEgUfuZLXSYCRS..9LmukEPmrRVHv9QIed.b850ky/cIJy")
//                .phoneNumber("+7(876)543-21-09")
//                .rating(0.0)
//                .email("sophia.thomas@bk.ru")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto13 = UserDto.builder()
//                .id(13L)
//                .username("nature_lover")
//                .phoneNumber("+7(876)543-21-09")
//                .rating(0.0)
//                .email("sophia.thomas@bk.ru")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user14 = User.builder()
//                .id(14L)
//                .username("foodie")
//                .password("$2a$10$TjJmASFFMmKK1iVgAwZaDel8TgWEurRYL.8jTs4ECE9FPaW13TbXG")
//                .phoneNumber("+7(953)180-00-61")
//                .rating(0.0)
//                .email("jacob.moore@rambler.ru")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto14 = UserDto.builder()
//                .id(14L)
//                .username("foodie")
//                .phoneNumber("+7(953)180-00-61")
//                .rating(0.0)
//                .email("jacob.moore@rambler.ru")
//                .boosted(false)
//                .role(Role.ROLE_USER)
//                .build();
//        User user15 = User.builder()
//                .id(15L)
//                .username("admin")
//                .password("$2a$10$/v7NnuEmQ8wvQg6oK.RFkeX1fPF25xzQIFYSz2M7BTVLkbi1RExYe")
//                .phoneNumber("+7(902)902-98-11")
//                .rating(0.0)
//                .email("dimaosipov00@gmail.com")
//                .boosted(false)
//                .role(Role.ROLE_ADMIN)
//                .resetPasswordToken(null)
//                .resetPasswordTokenExpiryDate(null)
//                .build();
//        UserDto userDto15 = UserDto.builder()
//                .id(15L)
//                .username("admin")
//                .phoneNumber("+7(902)902-98-11")
//                .rating(0.0)
//                .email("dimaosipov00@gmail.com")
//                .boosted(false)
//                .role(Role.ROLE_ADMIN)
//                .build();
//
//        Advertisement advertisement1 = Advertisement.builder()
//                .id(1L)
//                .user(user1)
//                .price(1000)
//                .headline("Smartphone")
//                .description("A portable device combining the functions of a mobile phone and a computer, typically " +
//                        "offering internet access, touchscreen interface, and various applications.")
//                .status(AdvertisementStatus.ACTIVE)
//                .build();
//        AdvertisementDto advertisementDto1 = AdvertisementDto.builder()
//                .id(1L)
//                .user(userDto1)
//                .price(1000)
//                .headline("Smartphone")
//                .description("A portable device combining the functions of a mobile phone and a computer, typically " +
//                        "offering internet access, touchscreen interface, and various applications.")
//                .status("ACTIVE")
//                .build();
//        Advertisement advertisement2 = Advertisement.builder()
//                .id(2L)
//                .user(user2)
//                .price(2000)
//                .headline("Laptop")
//                .description("A portable computer that is small and light enough to be used on one's lap, typically " +
//                        "with a clamshell form factor and a built-in keyboard and display.")
//                .status(AdvertisementStatus.ACTIVE)
//                .build();
//        AdvertisementDto advertisementDto2 = AdvertisementDto.builder()
//                .id(2L)
//                .user(userDto2)
//                .price(2000)
//                .headline("Laptop")
//                .description("A portable computer that is small and light enough to be used on one's lap, typically " +
//                        "with a clamshell form factor and a built-in keyboard and display.")
//                .status("ACTIVE")
//                .build();
//        Advertisement advertisement3 = Advertisement.builder()
//                .id(3L)
//                .user(user3)
//                .price(2000)
//                .headline("Headphones")
//                .description("A pair of small speakers worn over the ears to listen to audio from a connected device " +
//                        "such as a music player, smartphone, or computer.")
//                .status(AdvertisementStatus.ACTIVE)
//                .build();
//        AdvertisementDto advertisementDto3 = AdvertisementDto.builder()
//                .id(3L)
//                .user(userDto3)
//                .price(2000)
//                .headline("Headphones")
//                .description("A pair of small speakers worn over the ears to listen to audio from a connected device " +
//                        "such as a music player, smartphone, or computer.")
//                .status("ACTIVE")
//                .build();
//        Advertisement advertisement4 = Advertisement.builder()
//                .id(4L)
//                .user(user4)
//                .price(4000)
//                .headline("Backpack")
//                .description("A bag with shoulder straps that allows it to be carried on one's back, typically used " +
//                        "for carrying personal belongings, books, or electronic devices.")
//                .status(AdvertisementStatus.ACTIVE)
//                .build();
//        AdvertisementDto advertisementDto4 = AdvertisementDto.builder()
//                .id(4L)
//                .user(userDto4)
//                .price(4000)
//                .headline("Backpack")
//                .description("A bag with shoulder straps that allows it to be carried on one's back, typically used " +
//                        "for carrying personal belongings, books, or electronic devices.")
//                .status("ACTIVE")
//                .build();
//        Advertisement advertisement5 = Advertisement.builder()
//                .id(5L)
//                .user(user5)
//                .price(5000)
//                .headline("Sunglasses")
//                .description("Eyewear designed to protect the eyes from sunlight or glare, typically featuring " +
//                        "tinted lenses and frames that cover a larger area around the eyes.")
//                .status(AdvertisementStatus.ACTIVE)
//                .build();
//        AdvertisementDto advertisementDto5 = AdvertisementDto.builder()
//                .id(5L)
//                .user(userDto5)
//                .price(5000)
//                .headline("Sunglasses")
//                .description("Eyewear designed to protect the eyes from sunlight or glare, typically featuring " +
//                        "tinted lenses and frames that cover a larger area around the eyes.")
//                .status("ACTIVE")
//                .build();
//        Advertisement advertisement6 = Advertisement.builder()
//                .id(6L)
//                .user(user6)
//                .price(5000)
//                .headline("Watch")
//                .description("A small timepiece worn on the wrist or carried in a pocket, typically designed to show " +
//                        "the time and often other information such as date, day of the week, or chronograph functions.")
//                .status(AdvertisementStatus.ACTIVE)
//                .build();
//        AdvertisementDto advertisementDto6 = AdvertisementDto.builder()
//                .id(6L)
//                .user(userDto6)
//                .price(5000)
//                .headline("Watch")
//                .description("A small timepiece worn on the wrist or carried in a pocket, typically designed to show " +
//                        "the time and often other information such as date, day of the week, or chronograph functions.")
//                .status("ACTIVE")
//                .build();
//        Advertisement advertisement7 = Advertisement.builder()
//                .id(7L)
//                .user(user7)
//                .price(7000)
//                .headline("Sneakers")
//                .description("Casual athletic shoes with a flexible sole and typically made of canvas or leather, " +
//                        "suitable for walking, running, or other sports activities.")
//                .status(AdvertisementStatus.ACTIVE)
//                .build();
//        AdvertisementDto advertisementDto7 = AdvertisementDto.builder()
//                .id(7L)
//                .user(userDto7)
//                .price(7000)
//                .headline("Sneakers")
//                .description("Casual athletic shoes with a flexible sole and typically made of canvas or leather, " +
//                        "suitable for walking, running, or other sports activities.")
//                .status("ACTIVE")
//                .build();
//        Advertisement advertisement8 = Advertisement.builder()
//                .id(8L)
//                .user(user8)
//                .price(8000)
//                .headline("Umbrella")
//                .description("A portable device consisting of a collapsible canopy supported by a central rod, used " +
//                        "for protection against rain or sunlight.")
//                .status(AdvertisementStatus.ACTIVE)
//                .build();
//        AdvertisementDto advertisementDto8 = AdvertisementDto.builder()
//                .id(8L)
//                .user(userDto8)
//                .price(8000)
//                .headline("Umbrella")
//                .description("A portable device consisting of a collapsible canopy supported by a central rod, used " +
//                        "for protection against rain or sunlight.")
//                .status("ACTIVE")
//                .build();
//        Advertisement advertisement9 = Advertisement.builder()
//                .id(9L)
//                .user(user9)
//                .price(10000)
//                .headline("Camera")
//                .description("A device used to capture and record still images or moving pictures, typically " +
//                        "consisting of a lens, image sensor, and electronic components.")
//                .status(AdvertisementStatus.ACTIVE)
//                .build();
//        AdvertisementDto advertisementDto9 = AdvertisementDto.builder()
//                .id(9L)
//                .user(userDto9)
//                .price(10000)
//                .headline("Camera")
//                .description("A device used to capture and record still images or moving pictures, typically " +
//                        "consisting of a lens, image sensor, and electronic components.")
//                .status("ACTIVE")
//                .build();
//        Advertisement advertisement10 = Advertisement.builder()
//                .id(10L)
//                .user(user10)
//                .price(10000)
//                .headline("Perfume")
//                .description("A fragrant liquid typically made from essential oils and alcohol, applied to the skin " +
//                        "or clothing to produce a pleasant scent.")
//                .status(AdvertisementStatus.ACTIVE)
//                .build();
//        AdvertisementDto advertisementDto10 = AdvertisementDto.builder()
//                .id(10L)
//                .user(userDto10)
//                .price(10000)
//                .headline("Perfume")
//                .description("A fragrant liquid typically made from essential oils and alcohol, applied to the skin " +
//                        "or clothing to produce a pleasant scent.")
//                .status("ACTIVE")
//                .build();
//
//        Message message1 = Message.builder()
//                .id(1L)
//                .advertisement(advertisement2)
//                .sender(user1)
//                .recipient(user2)
//                .text("Hi! What can you tell me about Laptop?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        MessageDto messageDto1 = MessageDto.builder()
//                .id(1L)
//                .advertisement(advertisementDto2)
//                .sender(userDto1)
//                .recipient(userDto2)
//                .text("Hi! What can you tell me about Laptop?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        Message message2 = Message.builder()
//                .id(2L)
//                .advertisement(advertisement2)
//                .sender(user2)
//                .recipient(user1)
//                .text("Good Laptop, long battery life, nice screen")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        MessageDto messageDto2 = MessageDto.builder()
//                .id(2L)
//                .advertisement(advertisementDto2)
//                .sender(userDto2)
//                .recipient(userDto1)
//                .text("Good Laptop, long battery life, nice screen")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        Message message3 = Message.builder()
//                .id(3L)
//                .advertisement(advertisement2)
//                .sender(user5)
//                .recipient(user1)
//                .text("Hello, How many?")
//                .sentAt(LocalDateTime.now())
//                .read(false)
//                .build();
//        MessageDto messageDto3 = MessageDto.builder()
//                .id(3L)
//                .advertisement(advertisementDto2)
//                .sender(userDto5)
//                .recipient(userDto1)
//                .text("Hello, How many?")
//                .sentAt(LocalDateTime.now())
//                .read(false)
//                .build();
//        Message message4 = Message.builder()
//                .id(4L)
//                .advertisement(advertisement3)
//                .sender(user2)
//                .recipient(user3)
//                .text("Good afternoon. Is that a bargain?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        MessageDto messageDto4 = MessageDto.builder()
//                .id(4L)
//                .advertisement(advertisementDto3)
//                .sender(userDto2)
//                .recipient(userDto3)
//                .text("Good afternoon. Is that a bargain?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        Message message5 = Message.builder()
//                .id(5L)
//                .advertisement(advertisement3)
//                .sender(user3)
//                .recipient(user2)
//                .text("No")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        MessageDto messageDto5 = MessageDto.builder()
//                .id(5L)
//                .advertisement(advertisementDto3)
//                .sender(userDto3)
//                .recipient(userDto2)
//                .text("No")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        Message message6 = Message.builder()
//                .id(6L)
//                .advertisement(advertisement4)
//                .sender(user3)
//                .recipient(user4)
//                .text("Greetings, what color is the backpack?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        MessageDto messageDto6 = MessageDto.builder()
//                .id(6L)
//                .advertisement(advertisementDto4)
//                .sender(userDto3)
//                .recipient(userDto4)
//                .text("Greetings, what color is the backpack?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        Message message7 = Message.builder()
//                .id(7L)
//                .advertisement(advertisement4)
//                .sender(user4)
//                .recipient(user3)
//                .text("Hello! Blue")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        MessageDto messageDto7 = MessageDto.builder()
//                .id(7L)
//                .advertisement(advertisementDto4)
//                .sender(userDto4)
//                .recipient(userDto13)
//                .text("Hello! Blue")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        Message message8 = Message.builder()
//                .id(8L)
//                .advertisement(advertisement5)
//                .sender(user4)
//                .recipient(user5)
//                .text("Why are you selling sunglasses?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        MessageDto messageDto8 = MessageDto.builder()
//                .id(8L)
//                .advertisement(advertisementDto5)
//                .sender(userDto4)
//                .recipient(userDto5)
//                .text("Why are you selling sunglasses?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        Message message9 = Message.builder()
//                .id(9L)
//                .advertisement(advertisement5)
//                .sender(user5)
//                .recipient(user4)
//                .text("Didn't need it. Do you want it?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        MessageDto messageDto9 = MessageDto.builder()
//                .id(9L)
//                .advertisement(advertisementDto5)
//                .sender(userDto5)
//                .recipient(userDto4)
//                .text("Didn't need it. Do you want it?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        Message message10 = Message.builder()
//                .id(10L)
//                .advertisement(advertisement6)
//                .sender(user5)
//                .recipient(user6)
//                .text("What brand of watch?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//        MessageDto messageDto10 = MessageDto.builder()
//                .id(10L)
//                .advertisement(advertisementDto6)
//                .sender(userDto5)
//                .recipient(userDto6)
//                .text("What brand of watch?")
//                .sentAt(LocalDateTime.now())
//                .read(true)
//                .build();
//
//        users = new ArrayList<>(List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11,
//                user12, user13, user14, user15));
//        userDtos = new ArrayList<>(List.of(userDto1, userDto2, userDto3, userDto4, userDto5, userDto6, userDto7,
//                userDto8, userDto9, userDto10, userDto11, userDto12, userDto13, userDto14, userDto15));
//
//        advertisements = new ArrayList<>(List.of(advertisement1, advertisement2, advertisement3, advertisement4,
//                advertisement5, advertisement6, advertisement7, advertisement8, advertisement9, advertisement10));
//        advertisementDtos = new ArrayList<>(List.of(advertisementDto1, advertisementDto2, advertisementDto3,
//                advertisementDto4, advertisementDto5, advertisementDto6, advertisementDto7, advertisementDto8,
//                advertisementDto9, advertisementDto10));
//
//        messages = new ArrayList<>(List.of(message1, message2, message3, message4, message5, message6, message7,
//                message8, message9, message10));
//        messageDtos = new ArrayList<>(List.of(messageDto1, messageDto2, messageDto3, messageDto4, messageDto5,
//                messageDto6, messageDto7, messageDto8, messageDto9, messageDto10));
//    }
//
//    @AfterEach
//    public void tearDown() {
//        messages = null;
//        messageDtos = null;
//        advertisements = null;
//        advertisementDtos = null;
//        users = null;
//        userDtos = null;
//    }
