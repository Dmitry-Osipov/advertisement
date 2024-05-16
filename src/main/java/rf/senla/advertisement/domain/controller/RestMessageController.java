package rf.senla.advertisement.domain.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.advertisement.domain.dto.MessageDto;
import rf.senla.advertisement.domain.service.MessageService;
import rf.senla.advertisement.domain.utils.DtoConverter;
import rf.senla.advertisement.security.service.UserService;

import java.util.List;

/**
 * Контроллер для обработки запросов сообщений через REST API.
 */
@RestController
@RequestMapping("${spring.data.rest.base-path}/messages")
@Validated
@RequiredArgsConstructor
@Tag(name = "Работа с сообщениями")
public class RestMessageController {
    private final MessageService service;
    private final UserService userService;
    private final DtoConverter converter;

    /**
     * Получает список последних 10 сообщений.
     * @return {@link ResponseEntity} со списком всех сообщений в формате {@link MessageDto}.
     */
    @GetMapping
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        return ResponseEntity.ok(converter.getListMessageDto(service.getAll()));
    }

    /**
     * Получает переписку между текущим пользователем и пользователем с указанным именем.
     * @param username Имя пользователя, с которым нужно получить переписку.
     * @param page Порядковый номер страницы.
     * @param size Размер страницы.
     * @return {@link ResponseEntity} со списком сообщений в формате {@link MessageDto} между текущим пользователем и
     * пользователем с указанным именем.
     */
    @GetMapping("/search")
    public ResponseEntity<List<MessageDto>> getUserCorrespondence(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return ResponseEntity.ok(converter.getListMessageDto(
                service.getAll(userService.getByUsername(username), page, size)));
    }

    /**
     * Создает новое сообщение.
     * @param dto Сообщение в формате {@link MessageDto} для создания.
     * @return {@link ResponseEntity} с созданным сообщением в формате {@link MessageDto}.
     */
    @PostMapping
    @PreAuthorize("#dto.senderName == authentication.principal.username")
    public ResponseEntity<MessageDto> createMessage(@Valid @RequestBody MessageDto dto) {
        return ResponseEntity.ok(converter.getDtoFromMessage(service.save(converter.getMessageFromDto(dto))));
    }

    /**
     * Обновляет существующее сообщение.
     * @param dto Сообщение в формате {@link MessageDto} для обновления.
     * @return {@link ResponseEntity} с обновленным сообщением в формате {@link MessageDto}.
     */
    @PutMapping
    @PreAuthorize("#dto.senderName == authentication.principal.username")
    public ResponseEntity<MessageDto> updateMessage(@Valid @RequestBody MessageDto dto) {
        return ResponseEntity.ok(converter.getDtoFromMessage(service.update(converter.getMessageFromDto(dto))));
    }

    /**
     * Удаляет сообщение.
     * @param dto Сообщение в формате {@link MessageDto} для удаления.
     * @return {@link ResponseEntity} с информацией об удаленном сообщении.
     */
    @DeleteMapping
    @PreAuthorize("#dto.senderName == authentication.principal.username")
    public ResponseEntity<String> deleteMessage(@Valid @RequestBody MessageDto dto) {
        service.delete(converter.getMessageFromDto(dto));
        return ResponseEntity.ok("Deleted message with text: " + dto.getText());
    }
}
