package rf.senla.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.web.dto.AdvertisementDto;
import rf.senla.web.dto.CreateAdvertisementRequest;
import rf.senla.domain.service.IAdvertisementService;
import rf.senla.web.dto.UpdateAdvertisementRequest;
import rf.senla.web.utils.AdvertisementMapper;

import java.util.List;

/**
 * Контроллер для обработки запросов объявлений через REST API.
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Объявления")
@RequestMapping("${spring.data.rest.base-path}/advertisements")
public class RestAdvertisementController {
    private final IAdvertisementService service;
    private final AdvertisementMapper mapper;

    /**
     * Получить список объявлений с пагинацией
     * @param pageable пагинация
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping
    @Deprecated(forRemoval = true)
    @Operation(summary = "Получить список объявлений с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class),
                            examples = @ExampleObject(value = "[ {\"id\": 4,\"user\": {\"id\": 4,\"username\": " +
                                    "\"soccer_fanatic\",\"phoneNumber\": \"+7(234)567-89-01\",\"rating\": " +
                                    "3.3333333333333335,\"email\": \"alexander.wilson@hotmail.com\",\"role\": " +
                                    "\"ROLE_USER\"},\"price\": 4000,\"headline\": \"Backpack\"," +
                                    "\"description\": \"A bag with shoulder straps that allows it to be carried on " +
                                    "one's back, typically used for carrying personal belongings, books, or " +
                                    "electronic devices.\",\"status\": \"ACTIVE\"},{\"id\": 1,\"user\": {\"id\": " +
                                    "1,\"username\": \"user123\",\"phoneNumber\": \"+7(123)456-78-90\",\"rating\":" +
                                    " 0.0,\"email\": \"storm-yes@yandex.ru\",\"role\": " +
                                    "\"ROLE_USER\"},\"price\": 1000,\"headline\": \"Smartphone\",\"description\": " +
                                    "\"A portable device combining the functions of a mobile phone and a computer, " +
                                    "typically offering internet access, touchscreen interface, and various " +
                                    "applications.\",\"status\": \"ACTIVE\"} ]")))
    })
    public ResponseEntity<List<AdvertisementDto>> getAll(
            @PageableDefault(sort = {"boosted", "user.rating"}, direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(mapper.toDtos(service.getAll(pageable)));
    }
    // TODO: слить нижний метод в верхний
    /**
     * Получить список объявлений по заголовку в промежутке цен с условием сортировки и пагинацией.
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @param headline заголовок
     * @param pageable пагинация
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping("/filter")
    @Deprecated(forRemoval = true)
    @Operation(summary = "Получить список объявлений по заголовку в промежутке цен с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class),
                            examples = @ExampleObject(value = "[ {\"id\": 7,\"user\": {\"id\": 7,\"username\": " +
                                    "\"music_lover\",\"phoneNumber\": \"+7(345)678-90-12\",\"rating\": 3.5," +
                                    "\"email\": \"sarah.wilson@icloud.com\",\"role\": " +
                                    "\"ROLE_USER\"},\"price\": 7000,\"headline\": \"Sneakers\",\"description\": " +
                                    "\"Casual athletic shoes with a flexible sole and typically made of canvas or " +
                                    "leather, suitable for walking, running, or other sports activities.\"," +
                                    "\"status\": \"ACTIVE\"},{\"id\": 5,\"user\": {\"id\": 5,\"username\": " +
                                    "\"bookworm\",\"phoneNumber\": \"+7(567)890-12-34\",\"rating\": 0.0,\"email\": " +
                                    "\"emily.jones@outlook.com\",\"role\": \"ROLE_USER\"}," +
                                    "\"price\": 5000,\"headline\": \"Sunglasses\",\"description\": \"Eyewear " +
                                    "designed to protect the eyes from sunlight or glare, typically featuring " +
                                    "tinted lenses and frames that cover a larger area around the eyes.\"," +
                                    "\"status\": \"ACTIVE\"} ]")))
    })
    public ResponseEntity<List<AdvertisementDto>> getAllByPriceAndHeadline(
            @Parameter(description = "Минимальная стоимость", example = "500", in = ParameterIn.QUERY)
            @RequestParam(value = "min", required = false) @Min(0) @Max(Integer.MAX_VALUE) Integer minPrice,

            @Parameter(description = "Максимальная стоимость", example = "20000", in = ParameterIn.QUERY)
            @RequestParam(value = "max", required = false) @Min(0) @Max(Integer.MAX_VALUE) Integer maxPrice,

            @Parameter(description = "Заголовок", example = "smartphone", in = ParameterIn.QUERY)
            @RequestParam(value = "headline", required = false) String headline,

            @PageableDefault(sort = {"boosted", "user.rating"}, direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(mapper.toDtos(service.getAll(minPrice, maxPrice, headline, pageable)));
    }

    /**
     * Получить список объявлений по пользователю с пагинацией.
     * @param username имя пользователя (логин)
     * @param active требуется выводить только активные объявления
     * @param pageable пагинация
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping("/{username}")
    @Operation(summary = "Получить список объявлений (активные или неактивные) по пользователю с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class),
                            examples = @ExampleObject(value = "[ {\"id\": 1,\"user\": {\"id\": 1,\"username\": " +
                                    "\"user123\",\"phoneNumber\": \"+7(123)456-78-90\",\"rating\": 0.0,\"email\": " +
                                    "\"storm-yes@yandex.ru\",\"role\": \"ROLE_USER\"},\"price\": " +
                                    "1000,\"headline\": \"Smartphone\",\"description\": \"A portable device " +
                                    "combining the functions of a mobile phone and a computer, typically offering " +
                                    "internet access, touchscreen interface, and various applications.\"," +
                                    "\"status\": \"ACTIVE\"} ]")))
    })
    public ResponseEntity<List<AdvertisementDto>> getAllByUser(
            @Parameter(description = "Имя пользователя", example = "John_Doe", required = true, in = ParameterIn.PATH)
            @PathVariable("username") @NotBlank @Size(min = 5, max = 50) String username,

            @Parameter(description = "Флаг только активных объявлений", example = "true", in = ParameterIn.QUERY)
            @RequestParam(value = "active", required = false) Boolean active,

            @PageableDefault(sort = {"boosted", "user.rating"}, direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(mapper.toDtos(service.getAll(username, active, pageable)));
    }

    /**
     * Создать новое объявление.
     * @param request объект {@link CreateAdvertisementRequest} с данными нового объявления,
     * @param user текущий пользователь
     * @return объект {@link ResponseEntity} с созданным объявлением и кодом 200 OK в случае успеха
     */
    @PostMapping
    @Operation(summary = "Создать новое объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class)))
    })
    public ResponseEntity<AdvertisementDto> create(
            @Parameter(description = "Данные объявления", required = true,
                    content = @Content(schema = @Schema(implementation = CreateAdvertisementRequest.class)))
            @RequestBody @Valid CreateAdvertisementRequest request,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(mapper.toDto(service.create(mapper.toEntity(request), user)));
    }

    /**
     * Обновить существующее объявление.
     * @param request объект {@link AdvertisementDto} с обновленными данными объявления,
     * @param user текущий пользователь
     * @return объект {@link ResponseEntity} с обновленным объявлением и кодом 200 OK в случае успеха
     */
    @PutMapping
    @Operation(summary = "Обновить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class)))
    })
    public ResponseEntity<AdvertisementDto> update(
            @Parameter(description = "Данные объявления", required = true,
                    content = @Content(schema = @Schema(implementation = UpdateAdvertisementRequest.class)))
            @RequestBody @Valid UpdateAdvertisementRequest request,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(mapper.toDto(service.update(mapper.toEntity(request), user)));
    }

    /**
     * Удалить объявление.
     * @param id ID объявления
     * @param user текущий пользователь
     * @return объект {@link ResponseEntity} с сообщением об успешном удалении и кодом 200 OK в случае успеха
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Deleted advertisement with ID: 1")))
    })
    public ResponseEntity<String> delete(
            @Parameter(description = "ID объявления", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable("id") @Positive Long id,
            @AuthenticationPrincipal UserDetails user) {
        service.delete(id, user);
        return ResponseEntity.ok("Deleted advertisement with ID: " + id);
    }

    /**
     * Продажа объявления.
     * @param id ID объявления
     * @return объект {@link ResponseEntity} с обновленным объявлением и кодом 200 OK в случае успеха
     */
    @PutMapping("/{id}/selling")
    @Operation(summary = "Продажа объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class)))
    })
    public ResponseEntity<AdvertisementDto> sell(
            @Parameter(description = "ID объявления", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable("id") @Positive Long id,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(mapper.toDto(service.sell(id, user)));
    }

    /**
     * Обновить существующее объявление.
     * @param dto объект {@link AdvertisementDto} с обновленными данными объявления
     * @return объект {@link ResponseEntity} с обновленным объявлением и кодом 200 OK в случае успеха
     */
    @PutMapping("${spring.data.rest.admin-path}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить объявление админом")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class)))
    })
    public ResponseEntity<AdvertisementDto> updateByAdmin(
            @Parameter(description = "Данные объявления", required = true,
                    content = @Content(schema = @Schema(implementation = AdvertisementDto.class)))
            @RequestBody @Valid AdvertisementDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(mapper.toEntity(dto))));
    }

    /**
     * Удалить объявление.
     * @param id ID объявления
     * @return объект {@link ResponseEntity} с сообщением об успешном удалении и кодом 200 OK в случае успеха
     */
    @DeleteMapping("${spring.data.rest.admin-path}/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить объявление админом")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Deleted advertisement with ID: 1")))
    })
    public ResponseEntity<String> deleteByAdmin(
            @Parameter(description = "ID объявления", example = "1", required = true, in = ParameterIn.PATH)
            @PathVariable("id") @Positive Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted advertisement with ID: " + id);
    }

    // TODO: test
    /**
     * Метод для продвижения объявления.
     * @param id ID объявления
     * @param user текущий пользователь
     * @return строка с сообщением об успешном продвижении
     */
    @PutMapping("/{id}/boosted")
    @Operation(summary = "Метод для продвижения пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain",
                    examples = @ExampleObject(value = "Advertisement with ID 1 has received a boost")))
    })
    public ResponseEntity<String> setBoosted(
            @PathVariable("id") @Positive Long id,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok("Advertisement with ID " + service.boost(id, user).getId() +
                " has received a boost");
    }
}
