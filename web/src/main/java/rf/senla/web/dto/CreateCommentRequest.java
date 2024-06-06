package rf.senla.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Запрос на создание комментария.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание комментария")
public class CreateCommentRequest {
    @Schema(description = "Объявление")
    @NotNull(message = "Объявление не может быть пустым")
    private AdvertisementDto advertisement;

    @Schema(description = "Текст комментария", example = "Hello!")
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
}
