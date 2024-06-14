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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.RestController;
import rf.senla.web.dto.CommentDto;
import rf.senla.domain.service.ICommentService;
import rf.senla.web.dto.CreateCommentRequest;
import rf.senla.web.dto.UpdateCommentRequest;
import rf.senla.web.utils.CommentMapper;

import java.util.List;

/**
 * Контроллер для обработки запросов комментариев через REST API.
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Комментарии")
@RequestMapping("${spring.data.rest.base-path}/comments")
public class RestCommentController {
    private final ICommentService service;
    private final CommentMapper mapper;

    /**
     * Получить список всех комментариев по ID объявления.
     * @param advertisementId id объявления
     * @param pageable пагинация
     * @return ответ с кодом 200 (OK) и списком всех комментариев объявления в формате JSON
     */
    @GetMapping("/{advertisementId}")
    @Operation(summary = "Получить список всех комментариев по ID объявления с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class),
                            examples = @ExampleObject(value = "[ {\"id\": 1,\"advertisement\": {\"id\": 1,\"user\": " +
                                    "{\"id\": 1,\"username\": \"user123\",\"phoneNumber\": \"+7(123)456-78-90\"," +
                                    "\"rating\": 0.0,\"email\": \"storm-yes@yandex.ru\",\"boosted\": true,\"role\": " +
                                    "\"ROLE_USER\"},\"price\": 1000,\"headline\": \"Smartphone\",\"description\": " +
                                    "\"A portable device combining the functions of a mobile phone and a computer, " +
                                    "typically offering internet access, touchscreen interface, and various " +
                                    "applications.\",\"status\": \"ACTIVE\"},\"user\": {\"id\": 2,\"username\": " +
                                    "\"cool_guy\",\"phoneNumber\": \"+7(456)789-01-23\",\"rating\": 0.0,\"email\": " +
                                    "\"john.doe@gmail.com\",\"boosted\": false,\"role\": \"ROLE_USER\"},\"text\": " +
                                    "\"This smartphone is amazing! The camera quality is top-notch, and the battery " +
                                    "life lasts all day.\",\"createdAt\": \"2024-06-08T16:11:35.654512\"},{\"id\": " +
                                    "2,\"advertisement\": {\"id\": 1,\"user\": {\"id\": 1,\"username\": \"user123\"," +
                                    "\"phoneNumber\": \"+7(123)456-78-90\",\"rating\": 0.0,\"email\": " +
                                    "\"storm-yes@yandex.ru\",\"boosted\": true,\"role\": \"ROLE_USER\"},\"price\": " +
                                    "1000,\"headline\": \"Smartphone\",\"description\": \"A portable device " +
                                    "combining the functions of a mobile phone and a computer, typically offering " +
                                    "internet access, touchscreen interface, and various applications.\",\"status\": " +
                                    "\"ACTIVE\"},\"user\": {\"id\": 3,\"username\": \"adventure_lover\"," +
                                    "\"phoneNumber\": \"+7(789)012-34-56\",\"rating\": 0.0,\"email\": " +
                                    "\"jane.smith@yahoo.com\",\"boosted\": false,\"role\": \"ROLE_USER\"},\"text\": " +
                                    "\"I've been using this smartphone for a month now, and I'm impressed with its " +
                                    "performance. It's fast, reliable, and the screen is stunning.\",\"createdAt\": " +
                                    "\"2024-06-08T16:11:35.654512\"} ]")))
    })
    public ResponseEntity<List<CommentDto>> getCommentsByAdvertisementId(
            @Parameter(description = "ID объявления", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable @Min(1) @Max(Long.MAX_VALUE) Long advertisementId,

            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(mapper.toDtos(service.getAll(advertisementId, pageable)));
    }

    /**
     * Создать новый комментарий.
     * @param dto данные нового комментария в формате JSON
     * @param user текущий пользователь
     * @return ответ с кодом 200 (OK) и данными нового комментария в формате JSON
     */
    @PostMapping
    @Operation(summary = "Создать новый комментарий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"advertisementId\": 1," +
                                    "\"userName\": \"John_Doe\",\"text\": \"Hello!\",\"createdAt\": " +
                                    "\"2024-05-09T14:55:46.765819\"}")))
    })
    public ResponseEntity<CommentDto> create(
            @Parameter(description = "Данные комментария", required = true,
                    content = @Content(schema = @Schema(implementation = CreateCommentRequest.class)))
            @Valid @RequestBody CreateCommentRequest dto,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(mapper.toDto(service.create(mapper.toEntity(dto), user)));
    }

    /**
     * Обновить существующий комментарий.
     * @param dto данные обновляемого комментария в формате JSON
     * @param user текущий пользователь
     * @return ответ с кодом 200 (OK) и обновленными данными комментария в формате JSON
     */
    @PutMapping
    @Operation(summary = "Обновить комментарий")
    @PreAuthorize("#user.username == authentication.principal.username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)))
    })
    public ResponseEntity<CommentDto> update(
            @Parameter(description = "Данные комментария", required = true,
                    content = @Content(schema = @Schema(implementation = UpdateCommentRequest.class)))
            @Valid @RequestBody UpdateCommentRequest dto,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(mapper.toDto(service.update(mapper.toEntity(dto), user)));
    }

    /**
     * Удалить комментарий.
     * @param id ID комментария
     * @param user текущий пользователь
     * @return ответ с кодом 200 (OK) и сообщением об успешном удалении
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить комментарий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Deleted comment with ID: 1")))
    })
    public ResponseEntity<String> delete(
            @Parameter(description = "ID комментария", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) Long id,
            @AuthenticationPrincipal UserDetails user) {
        service.delete(id, user);
        return ResponseEntity.ok("Deleted comment with ID: " + id);
    }
}
