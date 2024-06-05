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
import rf.senla.domain.dto.CommentDto;
import rf.senla.domain.service.ICommentService;

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

    /**
     * Получить список всех комментариев по ID объявления.
     * @param advertisementId id объявления
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
            @PathVariable Long advertisementId,
            // TODO: Pageable
            @Parameter(description = "Номер страницы", example = "0", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Размер страницы", example = "1", in = ParameterIn.QUERY)
            @RequestParam(value = "size", required = false) Integer size) {
        // TODO: MapStruct
        return ResponseEntity.ok(null);//converter.getListCommentDto(service.getAll(advertisementId, page, size)));
    }

    /**
     * Создать новый комментарий.
     * @param dto данные нового комментария в формате JSON
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
    public ResponseEntity<CommentDto> createComment(
            @Parameter(description = "Данные комментария", required = true,
                    content = @Content(schema = @Schema(implementation = CommentDto.class)))
            @Valid @RequestBody CommentDto dto) {
        // TODO: MapStruct
        // TODO: создание по текущему пользователю
        return ResponseEntity.ok(null);//converter.getDtoFromComment(service.save(converter.getCommentFromDto(dto))));
    }

    /**
     * Обновить существующий комментарий.
     * @param dto данные обновляемого комментария в формате JSON
     * @return ответ с кодом 200 (OK) и обновленными данными комментария в формате JSON
     */
    @PutMapping
    @PreAuthorize("#dto.userName == authentication.principal.username")
    @Operation(summary = "Обновить комментарий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"advertisementId\": 1," +
                                    "\"userName\": \"John Doe\",\"text\": \"Hi!\",\"createdAt\": " +
                                    "\"2024-05-09T14:55:46.765819\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<CommentDto> updateComment(
            @Parameter(description = "Данные комментария", required = true,
                    content = @Content(schema = @Schema(implementation = CommentDto.class)))
            @Valid @RequestBody CommentDto dto) {
        // TODO: MapStruct
        // TODO: обновление по текущему пользователю
        return ResponseEntity.ok(null);//converter.getDtoFromComment(service.update(converter.getCommentFromDto(dto))));
    }

    /**
     * Удалить комментарий.
     * @param dto данные удаляемого комментария в формате JSON
     * @return ответ с кодом 200 (OK) и сообщением об успешном удалении
     */
    @DeleteMapping
    @PreAuthorize("#dto.userName == authentication.principal.username or hasRole('ADMIN')")
    @Operation(summary = "Удалить комментарий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> deleteComment(
            @Parameter(description = "Данные комментария", required = true,
                    content = @Content(schema = @Schema(implementation = CommentDto.class)))
            @Valid @RequestBody CommentDto dto) {
        // TODO: MapStruct
        //service.delete(converter.getCommentFromDto(dto));
        return ResponseEntity.ok("Deleted comment with id " + dto.getId());
    }
}
