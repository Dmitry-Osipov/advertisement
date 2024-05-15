package rf.senla.advertisement.domain.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.advertisement.domain.dto.CommentDto;
import rf.senla.advertisement.domain.service.IAdvertisementService;
import rf.senla.advertisement.domain.service.ICommentService;
import rf.senla.advertisement.domain.utils.DtoConverter;

import java.util.List;

/**
 * Контроллер для обработки запросов комментариев через REST API.
 */
@RestController
@RequestMapping("${spring.data.rest.base-path}/comments")
@Validated
@RequiredArgsConstructor
@Tag(name = "Работа с комментариями")
public class RestCommentController {
    private final ICommentService service;
    private final IAdvertisementService advertisementService;
    private final DtoConverter converter;

    /**
     * Получить список всех комментариев.
     * @return ответ с кодом 200 (OK) и списком всех комментариев в формате JSON
     */
    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        return ResponseEntity.ok(converter.getListCommentDto(service.getAll()));
    }

    /**
     * Получить список всех комментариев по ID объявления.
     * @param advertisementId id объявления
     * @return ответ с кодом 200 (OK) и списком всех комментариев объявления в формате JSON
     */
    @GetMapping("/{advertisementId}")
    public ResponseEntity<List<CommentDto>> getCommentsByAdvertisementId(@PathVariable Long advertisementId) {
        return ResponseEntity.ok(converter.getListCommentDto(
                service.getAll(advertisementService.getById(advertisementId))));
    }

    /**
     * Создать новый комментарий.
     * @param dto данные нового комментария в формате JSON
     * @return ответ с кодом 200 (OK) и данными нового комментария в формате JSON
     */
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto dto) {
        return ResponseEntity.ok(converter.getDtoFromComment(service.save(converter.getCommentFromDto(dto))));
    }

    /**
     * Обновить существующий комментарий.
     * @param dto данные обновляемого комментария в формате JSON
     * @return ответ с кодом 200 (OK) и обновленными данными комментария в формате JSON
     */
    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto dto) {
        return ResponseEntity.ok(converter.getDtoFromComment(service.update(converter.getCommentFromDto(dto))));
    }

    /**
     * Удалить комментарий.
     * @param dto данные удаляемого комментария в формате JSON
     * @return ответ с кодом 200 (OK) и сообщением об успешном удалении
     */
    @DeleteMapping
    public ResponseEntity<String> deleteComment(@Valid @RequestBody CommentDto dto) {
        service.delete(converter.getCommentFromDto(dto));
        return ResponseEntity.ok("Deleted comment with id " + dto.getId());
    }
}
