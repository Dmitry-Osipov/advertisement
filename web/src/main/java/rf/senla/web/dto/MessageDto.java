package rf.senla.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * DTO, представляющее сущность сообщения.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO сущности сообщения")
public class MessageDto {
    @Schema(description = "ID сообщения", example = "1")
    private Long id;

    @Schema(description = "Объявление")
    @NotNull(message = "Объявление не может быть null")
    private AdvertisementDto advertisement;

    @Schema(description = "Отправитель")
    @NotNull(message = "Отправитель не может быть null")
    private UserDto sender;

    @Schema(description = "Получатель")
    @NotNull(message = "Получатель не может быть null")
    private UserDto recipient;

    @Schema(description = "Текст сообщения", example = "Hello!")
    @NotBlank(message = "Сообщение не может быть пустым")
    private String text;

    @Schema(description = "Время создания", example = "2024-05-09T14:55:46.765819", nullable = true)
    private LocalDateTime sentAt;

    @Schema(description = "Статус прочтения", example = "true", nullable = true)
    private Boolean read;
}
