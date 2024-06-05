package rf.senla.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.domain.dto.MessageDto;
import rf.senla.domain.service.IMessageService;

import java.util.List;

/**
 * Контроллер для обработки запросов сообщений через REST API.
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Сообщения")
@RequestMapping("${spring.data.rest.base-path}/messages")
public class RestMessageController {
    private final IMessageService service;

    /**
     * Получает переписку между текущим пользователем и пользователем с указанным именем.
     * @param username Имя пользователя, с которым нужно получить переписку.
     * @param page Порядковый номер страницы.
     * @param size Размер страницы.
     * @return {@link ResponseEntity} со списком сообщений в формате {@link MessageDto} между текущим пользователем и
     * пользователем с указанным именем.
     */
    @GetMapping
    @Operation(summary = "Получить переписку с пользователем с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class),
                            examples = @ExampleObject(value = "[ {\"id\": 1,\"advertisementId\": 1,\"senderName\": " +
                                    "\"John Doe\",\"recipientName\": \"Laura Davis\",\"text\": \"Hello!\"," +
                                    "\"sentAt\": \"2024-05-09T14:55:46.765819\",\"read\": true}, {\"id\": 2," +
                                    "\"advertisementId\": 2,\"senderName\": \"Laura Davis\",\"recipientName\": " +
                                    "\"John Doe\",\"text\": \"Hi there!\",\"sentAt\": \"2024-05-10T10:20:30.123456\"," +
                                    "\"read\": false} ]"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<List<MessageDto>> getUserCorrespondence(
            @Parameter(description = "Имя пользователя", example = "John Doe", required = true, in = ParameterIn.QUERY)
            @RequestParam(value = "username") String username,
            // TODO: Pageable
            @Parameter(description = "Номер страницы", example = "0", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Размер страницы", example = "1", in = ParameterIn.QUERY)
            @RequestParam(value = "size", required = false) Integer size,
            @AuthenticationPrincipal UserDetails sender) {
        // TODO: MapStruct
        return ResponseEntity.ok(null);//converter.getListMessageDto(
                //service.getAll(sender, username, page, size)));
    }

    /**
     * Создает новое сообщение.
     * @param dto Сообщение в формате {@link MessageDto} для создания.
     * @return {@link ResponseEntity} с созданным сообщением в формате {@link MessageDto}.
     */
    @PostMapping
    @PreAuthorize("#dto.senderName == authentication.principal.username")
    @Operation(summary = "Создать новое сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"advertisementId\": 1,\"senderName\": " +
                                    "\"John Doe\",\"recipientName\": \"Laura Davis\",\"text\": \"Hello!\"," +
                                    "\"sentAt\": \"2024-05-09T14:55:46.765819\",\"read\": true}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<MessageDto> createMessage(
            @Parameter(description = "Данные сообщения", required = true,
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
            @Valid @RequestBody MessageDto dto) {
        // TODO: MapStruct
        // TODO: создание по текущему пользователю
        return ResponseEntity.ok(null);//converter.getDtoFromMessage(service.save(converter.getMessageFromDto(dto))));
    }

    /**
     * Обновляет существующее сообщение.
     * @param dto Сообщение в формате {@link MessageDto} для обновления.
     * @return {@link ResponseEntity} с обновленным сообщением в формате {@link MessageDto}.
     */
    @PutMapping
    @PreAuthorize("#dto.senderName == authentication.principal.username")
    @Operation(summary = "Обновить сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"advertisementId\": 1,\"senderName\": " +
                                    "\"John Doe\",\"recipientName\": \"Laura Davis\",\"text\": \"Hello!\"," +
                                    "\"sentAt\": \"2024-05-09T14:55:46.765819\",\"read\": false}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<MessageDto> updateMessage(
            @Parameter(description = "Данные сообщения", required = true,
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
            @Valid @RequestBody MessageDto dto) {
        // TODO: MapStruct
        // TODO: обновление по текущему пользователю
        return ResponseEntity.ok(null);//converter.getDtoFromMessage(service.update(converter.getMessageFromDto(dto))));
    }

    /**
     * Удаляет сообщение.
     * @param dto Сообщение в формате {@link MessageDto} для удаления.
     * @return {@link ResponseEntity} с информацией об удаленном сообщении.
     */
    @DeleteMapping
    @PreAuthorize("#dto.senderName == authentication.principal.username")
    @Operation(summary = "Удалить сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> deleteMessage(
            @Parameter(description = "Данные сообщения", required = true,
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
            @Valid @RequestBody MessageDto dto) {
        // TODO: MapStruct
        // TODO: Удаление по текущему пользователю
        //service.delete(converter.getMessageFromDto(dto));
        return ResponseEntity.ok("Deleted message with text: " + dto.getText());
    }
}
