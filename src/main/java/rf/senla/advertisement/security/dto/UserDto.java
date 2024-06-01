package rf.senla.advertisement.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rf.senla.advertisement.security.entity.Role;

/**
 * DTO, представляющее сущность пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO сущности пользователя")
@ToString
public class UserDto {
    @Schema(description = "ID пользователя", example = "1")
    @NotNull
    private Long id;

    @Schema(description = "Имя пользователя", example = "John Doe")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;

    @Schema(description = "Номер телефона", example = "+7(777)777-77-77")
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}", message = "Please use pattern +7(XXX)XXX-XX-XX")
    private String phoneNumber;

    @Schema(description = "Рейтинг", example = "100")
    private Integer rating;

    @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Schema(description = "Помощь в продвижении", example = "false")
    private Boolean boosted = false;

    @Schema(description = "Роль", example = "ROLE_USER")
    private Role role;
}
