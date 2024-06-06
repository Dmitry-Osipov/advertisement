package rf.senla.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Класс для представления запроса на удаление пользователя.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на удаление пользователя")
public class DeleteByUsernameRequest {
    @Schema(description = "Имя пользователя", example = "John Doe")
    private String username;
}
