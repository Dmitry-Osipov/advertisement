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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.web.dto.CreateMessageRequest;
import rf.senla.web.dto.MessageDto;
import rf.senla.web.dto.UpdateMessageRequest;
import rf.senla.domain.service.IMessageService;
import rf.senla.web.utils.MessageMapper;

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
                            examples = @ExampleObject(value = "[ {\"id\": 1,\"advertisement\": {\"id\": 2,\"user\": " +
                                    "{\"id\": 2,\"username\": \"cool_guy\",\"phoneNumber\": \"+7(456)789-01-23\"," +
                                    "\"rating\": 0.0,\"email\": \"john.doe@gmail.com\",\"role\": " +
                                    "\"ROLE_USER\"},\"price\": 2000,\"headline\": \"Laptop\",\"description\": \"A " +
                                    "portable computer that is small and light enough to be used on one's lap, " +
                                    "typically with a clamshell form factor and a built-in keyboard and display.\"," +
                                    "\"status\": \"ACTIVE\"},\"sender\": {\"id\": 1,\"username\": \"user123\"," +
                                    "\"phoneNumber\": \"+7(123)456-78-90\",\"rating\": 0.0,\"email\": " +
                                    "\"storm-yes@yandex.ru\",\"role\": \"ROLE_USER\"}," +
                                    "\"recipient\": {\"id\": 2,\"username\": \"cool_guy\",\"phoneNumber\": " +
                                    "\"+7(456)789-01-23\",\"rating\": 0.0,\"email\": \"john.doe@gmail.com\"," +
                                    "\"role\": \"ROLE_USER\"},\"text\": \"Hi! What can you tell " +
                                    "me about Laptop?\",\"sentAt\": \"2024-06-08T15:46:29.347781\",\"read\": true}," +
                                    "{\"id\": 2,\"advertisement\": {\"id\": 2,\"user\": {\"id\": 2,\"username\": " +
                                    "\"cool_guy\",\"phoneNumber\": \"+7(456)789-01-23\",\"rating\": 0.0,\"email\": " +
                                    "\"john.doe@gmail.com\",\"role\": \"ROLE_USER\"}," +
                                    "\"price\": 2000,\"headline\": \"Laptop\",\"description\": \"A portable " +
                                    "computer that is small and light enough to be used on one's lap, typically " +
                                    "with a clamshell form factor and a built-in keyboard and display.\",\"status\": " +
                                    "\"ACTIVE\"},\"sender\": {\"id\": 2,\"username\": \"cool_guy\",\"phoneNumber\": " +
                                    "\"+7(456)789-01-23\",\"rating\": 0.0,\"email\": \"john.doe@gmail.com\"," +
                                    "\"role\": \"ROLE_USER\"},\"recipient\": {\"id\": 1," +
                                    "\"username\": \"user123\",\"phoneNumber\": \"+7(123)456-78-90\",\"rating\": " +
                                    "0.0,\"email\": \"storm-yes@yandex.ru\",\"role\": \"ROLE_USER\"},\"text\": " +
                                    "\"Good Laptop, long battery life, nice screen\",\"read\": true} ]")))
    })
    public ResponseEntity<List<MessageDto>> getUserCorrespondence(
            @Parameter(description = "Имя пользователя", example = "John_Doe", required = true, in = ParameterIn.QUERY)
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
                            schema = @Schema(implementation = MessageDto.class)))
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
                            schema = @Schema(implementation = MessageDto.class)))
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
     * @param id ID сообщения
     * @return {@link ResponseEntity} с информацией об удаленном сообщении.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Deleted message with ID: 1")))
    })
    public ResponseEntity<String> delete(
            @Parameter(description = "ID сообщения", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) Long id,
            @AuthenticationPrincipal UserDetails user) {
        service.delete(id, user);
        return ResponseEntity.ok("Deleted message with ID: " + id);
    }
}
