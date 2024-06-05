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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
import rf.senla.domain.dto.CreateMessageRequest;
import rf.senla.domain.dto.DeleteMessageRequest;
import rf.senla.domain.dto.MessageDto;
import rf.senla.domain.dto.UpdateMessageRequest;
import rf.senla.domain.service.IMessageService;
import rf.senla.web.utils.MessageMapper;

import java.util.List;

// TODO: swagger doc
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
    private final MessageMapper mapper;

    /**
     * Получает переписку между текущим пользователем и пользователем с указанным именем.
     * @param username Имя пользователя, с которым нужно получить переписку.
     * @param pageable пагинация
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
            @RequestParam(value = "username") @NotBlank @Size(min = 5, max = 50) String username,
            @PageableDefault(sort = {"sentAt"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails sender) {
        return ResponseEntity.ok(mapper.toDtos(service.getAll(sender, username, pageable)));
    }

    /**
     * Создает новое сообщение.
     * @param request Сообщение в формате {@link CreateMessageRequest} для создания.
     * @return {@link ResponseEntity} с созданным сообщением в формате {@link MessageDto}.
     */
    @PostMapping
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
    public ResponseEntity<MessageDto> create(
            @Parameter(description = "Данные сообщения", required = true,
                    content = @Content(schema = @Schema(implementation = CreateMessageRequest.class)))
            @Valid @RequestBody CreateMessageRequest request,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(mapper.toDto(service.create(mapper.toEntity(request), user)));
    }

    /**
     * Обновляет существующее сообщение.
     * @param request Сообщение в формате {@link UpdateMessageRequest} для обновления.
     * @return {@link ResponseEntity} с обновленным сообщением в формате {@link MessageDto}.
     */
    @PutMapping
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
    public ResponseEntity<MessageDto> update(
            @Parameter(description = "Данные сообщения", required = true,
                    content = @Content(schema = @Schema(implementation = UpdateMessageRequest.class)))
            @Valid @RequestBody UpdateMessageRequest request,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(mapper.toDto(service.update(mapper.toEntity(request), user)));
    }

    /**
     * Удаляет сообщение.
     * @param dto Сообщение в формате {@link DeleteMessageRequest} для удаления.
     * @return {@link ResponseEntity} с информацией об удаленном сообщении.
     */
    @DeleteMapping
    @Operation(summary = "Удалить сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> delete(
            @Parameter(description = "Данные сообщения", required = true,
                    content = @Content(schema = @Schema(implementation = DeleteMessageRequest.class)))
            @Valid @RequestBody DeleteMessageRequest dto,
            @AuthenticationPrincipal UserDetails user) {
        service.delete(mapper.toEntity(dto), user);
        return ResponseEntity.ok("Deleted message with id: " + dto.getId());
    }
}
