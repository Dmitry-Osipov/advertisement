package rf.senla.advertisement.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.security.entity.User;

import java.time.LocalDateTime;

/**
 * DTO, представляющее сущность сообщения.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO сущности сообщения")
public class MessageDto {
    @Schema(description = "ID сообщения", example = "1")
    private Long id;

    @Schema(description = "ID объявления", example = "1")
    @Positive(message = "ID объявления не может быть отрицательным или 0")
    @NotNull(message = "ID объявления не может быть пустым")
    private Long advertisementId;

    @Schema(description = "Имя отправителя", example = "John Doe")
    @Size(min = 5, max = 50, message = "Имя отправителя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя отправителя не может быть пустым")
    private String senderName;

    @Schema(description = "Имя получателя", example = "Laura Davis")
    @Size(min = 5, max = 50, message = "Имя получателя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя получателя не может быть пустым")
    private String recipientName;

    @Schema(description = "Текст сообщения", example = "Hello!")
    @NotBlank(message = "Сообщение не может быть пустым")
    private String text;

    @Schema(description = "Время создания", example = "2024-05-09T14:55:46.765819", nullable = true)
    private LocalDateTime sentAt;

    @Schema(description = "Статус прочтения", example = "true", nullable = true)
    private Boolean read;
}
