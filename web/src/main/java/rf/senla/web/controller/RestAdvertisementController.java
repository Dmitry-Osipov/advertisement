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
import rf.senla.domain.dto.AdvertisementDto;
import rf.senla.domain.dto.CreateAdvertisementRequest;
import rf.senla.domain.service.IAdvertisementService;
import rf.senla.domain.dto.DeleteByIdRequest;
import rf.senla.domain.dto.UpdateAdvertisementRequest;
import rf.senla.web.utils.AdvertisementMapper;

import java.util.List;

// TODO: swagger doc
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
    @Operation(summary = "Получить список объявлений с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class),
                            examples = @ExampleObject(value = "[ {\"id\": 1,\"userName\": \"John Doe\",\"price\": " +
                                    "2000,\"headline\": \"Smartphone\",\"description\": \"A smartphone is a portable " +
                                    "device that combines the functions of a cell phone and a personal computer\"," +
                                    "\"status\": \"ACTIVE\"}, {\"id\": 2,\"userName\": \"Alice Smith\"," +
                                    "\"price\": 1500,\"headline\": \"Laptop\",\"description\": \"A laptop computer " +
                                    "is a portable, personal computer with a clamshell form factor, suitable for " +
                                    "mobile use.\",\"status\": \"INACTIVE\"} ]"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<List<AdvertisementDto>> getAll(
            @PageableDefault(sort = {"user.boosted", "user.rating"}, direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(mapper.toDtos(service.getAll(pageable)));
    }

    /**
     * Получить список объявлений по заголовку в промежутке цен с условием сортировки и пагинацией.
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @param headline заголовок
     * @param pageable пагинация
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping("/filter")
    @Operation(summary = "Получить список объявлений по заголовку в промежутке цен с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class),
                            examples = @ExampleObject(value = "[ {\"id\": 1,\"userName\": \"John Doe\",\"price\": " +
                                    "2000,\"headline\": \"Smartphone\",\"description\": \"A smartphone is a portable " +
                                    "device that combines the functions of a cell phone and a personal computer\"," +
                                    "\"status\": \"ACTIVE\"}, {\"id\": 2,\"userName\": \"Alice Smith\"," +
                                    "\"price\": 1500,\"headline\": \"Laptop\",\"description\": \"A laptop computer " +
                                    "is a portable, personal computer with a clamshell form factor, suitable for " +
                                    "mobile use.\",\"status\": \"INACTIVE\"} ]"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<List<AdvertisementDto>> getAllByPriceAndHeadline(
            @Parameter(description = "Минимальная стоимость", example = "500", required = true, in = ParameterIn.QUERY)
            @RequestParam(value = "min") @Min(0) @Max(Integer.MAX_VALUE) Integer minPrice,

            @Parameter(description = "Максимальная стоимость", example = "20000", required = true, in = ParameterIn.QUERY)
            @RequestParam(value = "max") @Min(0) @Max(Integer.MAX_VALUE) Integer maxPrice,

            @Parameter(description = "Заголовок", example = "smartphone", in = ParameterIn.QUERY)
            @RequestParam(value = "headline", required = false) String headline,

            @PageableDefault(sort = {"user.boosted", "user.rating"}, direction = Sort.Direction.DESC)
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
    @Operation(summary = "Получить список объявлений по пользователю с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class),
                            examples = @ExampleObject(value = "[ {\"id\": 1,\"userName\": \"John Doe\",\"price\": " +
                                    "2000,\"headline\": \"Smartphone\",\"description\": \"A smartphone is a portable " +
                                    "device that combines the functions of a cell phone and a personal computer\"," +
                                    "\"status\": \"ACTIVE\"}, {\"id\": 2,\"userName\": \"John Doe\"," +
                                    "\"price\": 1500,\"headline\": \"Laptop\",\"description\": \"A laptop computer " +
                                    "is a portable, personal computer with a clamshell form factor, suitable for " +
                                    "mobile use.\",\"status\": \"INACTIVE\"} ]"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<List<AdvertisementDto>> getAllByUser(
            @Parameter(description = "Имя пользователя", example = "John Doe", required = true, in = ParameterIn.PATH)
            @PathVariable("username") @NotBlank @Size(min = 5, max = 50) String username,

            @Parameter(description = "Статус объявления", example = "true", in = ParameterIn.QUERY)
            @RequestParam(value = "active", required = false) Boolean active,

            @PageableDefault(sort = {"user.boosted", "user.rating"}, direction = Sort.Direction.DESC)
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
                            schema = @Schema(implementation = AdvertisementDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"userName\": \"John Doe\",\"price\": 2000," +
                                    "\"headline\": \"Smartphone\",\"description\": \"A smartphone is a portable " +
                                    "device that combines the functions of a cell phone and a personal computer\"," +
                                    "\"status\": \"ACTIVE\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
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
    @Operation(summary = "Обновить существующее объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"userName\": \"John Doe\",\"price\": 2000," +
                                    "\"headline\": \"Smartphone\",\"description\": \"A smartphone is a portable " +
                                    "device that combines the functions of a cell phone and a personal computer\"," +
                                    "\"status\": \"REVIEW\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
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
     * @param request объект {@link DeleteByIdRequest} с данными объявления для удаления
     * @param user текущий пользователь
     * @return объект {@link ResponseEntity} с сообщением об успешном удалении и кодом 200 OK в случае успеха
     */
    @DeleteMapping
    @Operation(summary = "Удалить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> delete(
            @Parameter(description = "Данные объявления", required = true,
                    content = @Content(schema = @Schema(implementation = DeleteByIdRequest.class)))
            @RequestBody @Valid DeleteByIdRequest request,
            @AuthenticationPrincipal UserDetails user) {
        service.delete(mapper.toEntity(request), user);
        return ResponseEntity.ok("Deleted advertisement with ID: " + request.getId());
    }

    /**
     * Обновить существующее объявление.
     * @param dto объект {@link AdvertisementDto} с обновленными данными объявления
     * @return объект {@link ResponseEntity} с обновленным объявлением и кодом 200 OK в случае успеха
     */
    @PutMapping("${spring.data.rest.admin-path}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить существующее объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"userName\": \"John Doe\",\"price\": 2000," +
                                    "\"headline\": \"Smartphone\",\"description\": \"A smartphone is a portable " +
                                    "device that combines the functions of a cell phone and a personal computer\"," +
                                    "\"status\": \"REVIEW\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<AdvertisementDto> updateByAdmin(
            @Parameter(description = "Данные объявления", required = true,
                    content = @Content(schema = @Schema(implementation = AdvertisementDto.class)))
            @RequestBody @Valid AdvertisementDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(mapper.toEntity(dto))));
    }

    /**
     * Удалить объявление.
     * @param request объект {@link AdvertisementDto} с данными объявления для удаления
     * @return объект {@link ResponseEntity} с сообщением об успешном удалении и кодом 200 OK в случае успеха
     */
    @DeleteMapping("${spring.data.rest.admin-path}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> deleteByAdmin(
            @Parameter(description = "Данные объявления", required = true,
                    content = @Content(schema = @Schema(implementation = AdvertisementDto.class)))
            @RequestBody @Valid DeleteByIdRequest request) {
        service.delete(mapper.toEntity(request));
        return ResponseEntity.ok("Deleted advertisement with ID: " + request.getId());
    }
}
