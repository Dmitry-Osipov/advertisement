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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rf.senla.advertisement.security.entity.Role;

import java.util.Collection;
import java.util.List;

/**
 * DTO, представляющее сущность пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO сущности пользователя")
public class UserDto implements UserDetails {
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

    /**
     * Получение роли пользователя в виде коллекции GrantedAuthority.
     * @return Роли пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Проверка, истек ли срок действия учетной записи пользователя.
     * @return Всегда возвращает {@code true}, так как срок действия учетной записи не учитывается.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверка, заблокирована ли учетная запись пользователя.
     * @return Всегда возвращает {@code true}, так как учетная запись пользователя не блокируется.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверка, истек ли срок действия учетных данных пользователя.
     * @return Всегда возвращает {@code true}, так как срок действия учетных данных не учитывается.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверка, активирована ли учетная запись пользователя.
     * @return Всегда возвращает {@code true}, так как учетная запись пользователя всегда активна.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
