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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import rf.senla.domain.service.IAdvertisementService;
import rf.senla.web.utils.DtoConverter;
import rf.senla.domain.service.IUserService;

import java.util.List;

/**
 * Контроллер для обработки запросов объявлений через REST API.
 */
@RestController
@RequestMapping("${spring.data.rest.base-path}/advertisements")
@Validated
@RequiredArgsConstructor
@Tag(name = "Объявления")
public class RestAdvertisementController {
    private final IAdvertisementService service;
    private final IUserService userService;
    private final DtoConverter converter;

    /**
     * Получить список 10 топовых объявлений.
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping
    @Operation(summary = "Получить 10 топовых объявлений")
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
    public ResponseEntity<List<AdvertisementDto>> getAdvertisements() {
        return ResponseEntity.ok(converter.getListAdvertisementDto(service.getAll()));
    }

    /**
     * Получить список объявлений по заголовку в промежутке цен с условием сортировки и пагинацией.
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @param headline заголовок
     * @param sortBy условие сортировки
     * @param page страница
     * @param size размер страницы
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping("/search")
    @Operation(summary = "Получить список объявлений по заголовку в промежутке цен с условием сортировки и пагинацией")
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
            @Parameter(description = "Минимальная стоимость", example = "500", in = ParameterIn.QUERY)
            @RequestParam(value = "min", required = false) Integer minPrice,
            @Parameter(description = "Максимальная стоимость", example = "20000", in = ParameterIn.QUERY)
            @RequestParam(value = "max", required = false) Integer maxPrice,
            @Parameter(description = "Заголовок", example = "smartphone", in = ParameterIn.QUERY)
            @RequestParam(value = "headline", required = false) String headline,
            @Parameter(description = "Условие сортировки", example = "asc", in = ParameterIn.QUERY)
            @RequestParam(value = "sort", required = false) String sortBy,
            @PageableDefault(value = 2, page = 0) Pageable pageable,  // TODO: доработать
            @Parameter(description = "Номер страницы", example = "0", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Размер страницы", example = "1", in = ParameterIn.QUERY)
            @RequestParam(value = "size", required = false) Integer size) {
        return ResponseEntity.ok(converter.getListAdvertisementDto(
                service.getAll(minPrice, maxPrice, headline, sortBy, page, size)));
    }

    /**
     * Получить список объявлений по пользователю с пагинацией.
     * @param username имя пользователя (логин)
     * @param sortBy условие сортировки
     * @param active требуется выводить только активные объявления
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping("/search/{username}")
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
            @PathVariable("username") String username,
            @Parameter(description = "Условие сортировки", example = "asc", in = ParameterIn.QUERY)
            @RequestParam(value = "sort", required = false) String sortBy,
            @Parameter(description = "Статус объявления", example = "true", in = ParameterIn.QUERY)
            @RequestParam(value = "active", required = false) Boolean active,
            @Parameter(description = "Номер страницы", example = "0", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Размер страницы", example = "1", in = ParameterIn.QUERY)
            @RequestParam(value = "size", required = false) Integer size) {
        return ResponseEntity.ok(converter.getListAdvertisementDto(
                service.getAll(userService.getByUsername(username), sortBy, active, page, size)));
    }

    /**
     * Создать новое объявление.
     * @param dto объект {@link AdvertisementDto} с данными нового объявления
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
    public ResponseEntity<AdvertisementDto> createAdvertisement(
            @Parameter(description = "Данные объявления", required = true,
                    content = @Content(schema = @Schema(implementation = AdvertisementDto.class)))
            @RequestBody @Valid AdvertisementDto dto) {
        return ResponseEntity.ok(converter.getDtoFromAdvertisement(
                service.save(converter.getAdvertisementFromDto(dto))));
    }

    /**
     * Обновить существующее объявление.
     * @param dto объект {@link AdvertisementDto} с обновленными данными объявления
     * @return объект {@link ResponseEntity} с обновленным объявлением и кодом 200 OK в случае успеха
     */
    @PutMapping
    @PreAuthorize("#dto.userName == authentication.principal.username or hasRole('ADMIN')")
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
    public ResponseEntity<AdvertisementDto> updateAdvertisement(
            @Parameter(description = "Данные объявления", required = true,
                    content = @Content(schema = @Schema(implementation = AdvertisementDto.class)))
            @RequestBody @Valid AdvertisementDto dto) {
        return ResponseEntity.ok(converter.getDtoFromAdvertisement(
                service.update(converter.getAdvertisementFromDto(dto))));
    }

    /**
     * Удалить объявление.
     * @param dto объект {@link AdvertisementDto} с данными объявления для удаления
     * @return объект {@link ResponseEntity} с сообщением об успешном удалении и кодом 200 OK в случае успеха
     */
    @DeleteMapping
    @PreAuthorize("#dto.userName == authentication.principal.username or hasRole('ADMIN')")
    @Operation(summary = "Удалить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> deleteAdvertisement(
            @Parameter(description = "Данные объявления", required = true,
                    content = @Content(schema = @Schema(implementation = AdvertisementDto.class)))
            @RequestBody @Valid AdvertisementDto dto) {
        service.delete(converter.getAdvertisementFromDto(dto));
        return ResponseEntity.ok("Deleted advertisement with headline " + dto.getHeadline());
    }
}
