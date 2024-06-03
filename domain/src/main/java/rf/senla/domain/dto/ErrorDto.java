package rf.senla.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * DTO, представляющее сущность ошибки.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO сущности ошибки")
public class ErrorDto {
    @Schema(description = "Сообщение об ошибке", example = "Access Denied")
    private String message;
    @Schema(description = "URI ошибки", example = "uri=/api/users/My_Inc0rrect-user")
    private String description;
    @Schema(description = "Время отчёта об ошибке", example = "2024-05-21T17:13:28.410277843")
    private LocalDateTime time;
}
