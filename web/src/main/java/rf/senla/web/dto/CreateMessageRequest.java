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
 * Запрос на сохранение сообщения.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на сохранение сообщения")
public class CreateMessageRequest {
    @Schema(description = "Объявление")
    @NotNull(message = "Объявление не может быть null")
    private AdvertisementDto advertisement;

    @Schema(description = "Получатель")
    @NotNull(message = "Получатель не может быть null")
    private UserDto recipient;

    @Schema(description = "Текст сообщения", example = "Hello!")
    @NotBlank(message = "Сообщение не может быть пустым")
    private String text;
}
