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
import rf.senla.web.dto.DeleteByIdRequest;
import rf.senla.web.dto.UpdateCommentRequest;
import rf.senla.web.utils.CommentMapper;

import java.util.List;

// TODO: swagger doc
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
                            examples = @ExampleObject(value = "[ {\"id\": 1,\"advertisementId\": 1,\"userName\": " +
                                    "\"John Doe\",\"text\": \"Hello!\",\"createdAt\": \"2024-05-09T14:55:46.765819\"}, " +
                                    "{\"id\": 2,\"advertisementId\": 1,\"userName\": \"Jane Smith\",\"text\": " +
                                    "\"Hi there!\",\"createdAt\": \"2024-05-10T10:30:00.000000\"} ]"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
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
                                    "\"userName\": \"John Doe\",\"text\": \"Hello!\",\"createdAt\": " +
                                    "\"2024-05-09T14:55:46.765819\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
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
                            schema = @Schema(implementation = CommentDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"advertisementId\": 1," +
                                    "\"userName\": \"John Doe\",\"text\": \"Hi!\",\"createdAt\": " +
                                    "\"2024-05-09T14:55:46.765819\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
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
     * @param request данные удаляемого комментария в формате JSON
     * @param user текущий пользователь
     * @return ответ с кодом 200 (OK) и сообщением об успешном удалении
     */
    @DeleteMapping
    @Operation(summary = "Удалить комментарий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> delete(
            @Parameter(description = "Данные объявления", required = true,
                    content = @Content(schema = @Schema(implementation = DeleteByIdRequest.class)))
            @Valid @RequestBody DeleteByIdRequest request,
            @AuthenticationPrincipal UserDetails user) {
        service.delete(request.getId(), user);
        return ResponseEntity.ok("Deleted comment with id: " + request.getId());
    }
}
