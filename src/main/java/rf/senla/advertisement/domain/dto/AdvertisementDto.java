package rf.senla.advertisement.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO, представляющее сущность объявления.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO сущности объявления")
public class AdvertisementDto {
    @Schema(description = "ID объявления", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "John Doe")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String userName;

    @Schema(description = "Стоимость", example = "2000")
    @PositiveOrZero(message = "Цена не может быть отрицательной")
    private Integer price;

    @Schema(description = "Заголовок", example = "Smartphone")
    @Size(min = 2, max = 50, message = "Длина заголовка должна быть от 2 до 50 символов")
    @NotBlank(message = "Заголовок не может быть пустым")
    private String headline;

    @Schema(description = "Описание", example = "A smartphone is a portable device that combines the functions of a " +
            "cell phone and a personal computer")
    @Size(min = 3, message = "Длина описания должна быть от 3 символов")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @Schema(description = "Статус", example = "ACTIVE")
    @Size(min = 4, max = 50, message = "Длина статуса должна быть от 4 до 50 символов")
    @NotBlank(message = "Статус не может быть пустым")
    private String status;
}
