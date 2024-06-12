package rf.senla.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Запрос на удаление сообщения.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на удаление через ID")
public class DeleteByIdRequest {
    @Schema(description = "ID сообщения", example = "1")
    @Positive(message = "ID не может быть ниже 1")
    private Long id;
}