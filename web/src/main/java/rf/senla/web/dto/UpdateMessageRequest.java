package rf.senla.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Запрос на обновление сообщения.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление сообщения")
public class UpdateMessageRequest {
    @Schema(description = "ID сообщения", example = "1")
    @Positive(message = "ID не может быть ниже 1")
    private Long id;

    @Schema(description = "Текст сообщения", example = "Hello!")
    @NotBlank(message = "Сообщение не может быть пустым")
    private String text;

    @Schema(description = "Статус прочтения", example = "true", nullable = true)
    private Boolean read;
}
