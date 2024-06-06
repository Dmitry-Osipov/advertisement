package rf.senla.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * DTO, представляющее сущность комментария.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO сущности комментария")
public class CommentDto {
    @Schema(description = "ID комментария", example = "1")
    private Long id;

    @Schema(description = "ID объявления", example = "1")
    @Positive(message = "ID объявления не может быть отрицательным или 0")
    @NotNull(message = "ID объявления не может быть пустым")
    private Long advertisementId;

    @Schema(description = "Имя пользователя", example = "John Doe")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String userName;

    @Schema(description = "Текст сообщения", example = "Hello!")
    @NotBlank(message = "Сообщение не может быть пустым")
    private String text;

    @Schema(description = "Время создания", example = "2024-05-09T14:55:46.765819", nullable = true)
    private LocalDateTime createdAt;
}
