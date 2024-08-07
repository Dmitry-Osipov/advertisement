package rf.senla.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Запрос на обновление объявления.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление объявления")
public class UpdateAdvertisementRequest {
    @Schema(description = "ID объявления", example = "1")
    @Positive(message = "ID не может быть меньше 1")
    private Long id;

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
}
