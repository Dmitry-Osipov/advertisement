package rf.senla.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Класс для представления ответа на аутентификацию с использованием JWT.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с токеном доступа")
public class JwtAuthenticationResponse {
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String token;
}
