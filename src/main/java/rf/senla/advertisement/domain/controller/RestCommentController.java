package rf.senla.advertisement.domain.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.advertisement.domain.dto.CommentDto;
import rf.senla.advertisement.domain.service.CommentService;
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
    private final CommentService service;
    private final DtoConverter converter;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        return ResponseEntity.ok(converter.getListCommentDto(service.getAll()));
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(converter.getDtoFromComment(
                service.save(converter.getCommentFromDto(commentDto))));
    }
}
