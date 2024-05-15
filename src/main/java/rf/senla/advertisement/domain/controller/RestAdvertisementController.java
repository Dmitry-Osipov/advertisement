package rf.senla.advertisement.domain.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import rf.senla.advertisement.domain.dto.AdvertisementDto;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.service.IAdvertisementService;
import rf.senla.advertisement.domain.utils.DtoConverter;
import rf.senla.advertisement.security.service.IUserService;

import java.util.List;

/**
 * Контроллер для обработки запросов объявлений через REST API.
 */
@RestController
@RequestMapping("${spring.data.rest.base-path}/advertisements")
@Validated
@RequiredArgsConstructor
@Tag(name = "Работа с объявлениями")
public class RestAdvertisementController {
    private final IAdvertisementService service;
    private final IUserService userService;
    private final DtoConverter converter;

    /**
     * Получить все объявления.
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisements() {
        return ResponseEntity.ok(converter.getListAdvertisementDto(service.getAll()));
    }

    /**
     * Получить список объявлений по заголовку в промежутке цен с условием сортировки.
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @param headline заголовок
     * @param sortBy условие сортировки
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping("/search")
    public ResponseEntity<List<AdvertisementDto>> getAllByPriceAndHeadline(
            @RequestParam(value = "min", required = false) Integer minPrice,
            @RequestParam(value = "max", required = false) Integer maxPrice,
            @RequestParam(value = "headline", required = false) String headline,
            @RequestParam(value = "sort", required = false) String sortBy) {
        return ResponseEntity.ok(converter.getListAdvertisementDto(
                service.getAll(minPrice, maxPrice, headline, sortBy)));
    }

    /**
     * Получить список объявлений по пользователю.
     * @param username имя пользователя (логин)
     * @param sortBy условие сортировки
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping("/search/{username}")
    public ResponseEntity<List<AdvertisementDto>> getAllByUser(
            @PathVariable("username") String username,
            @RequestParam(value = "sort", required = false) String sortBy,
            @RequestParam(value = "active", required = false) Boolean active) {
        return ResponseEntity.ok(converter.getListAdvertisementDto(
                service.getAll(userService.getByUsername(username), sortBy, active)));
    }

    /**
     * Создать новое объявление.
     * @param dto объект {@link AdvertisementDto} с данными нового объявления
     * @return объект {@link ResponseEntity} с созданным объявлением и кодом 200 OK в случае успеха
     */
    @PostMapping
    public ResponseEntity<AdvertisementDto> createAdvertisement(@RequestBody @Valid AdvertisementDto dto) {
        Advertisement advertisement = service.save(converter.getAdvertisementFromDto(dto));
        return ResponseEntity.ok(converter.getDtoFromAdvertisement(advertisement));
    }

    /**
     * Обновить существующее объявление.
     * @param dto объект {@link AdvertisementDto} с обновленными данными объявления
     * @return объект {@link ResponseEntity} с обновленным объявлением и кодом 200 OK в случае успеха
     */
    @PutMapping
    public ResponseEntity<AdvertisementDto> updateAdvertisement(@RequestBody @Valid AdvertisementDto dto) {
        Advertisement advertisement = service.update(converter.getAdvertisementFromDto(dto));
        return ResponseEntity.ok(converter.getDtoFromAdvertisement(advertisement));
    }

    /**
     * Удалить объявление.
     * @param dto объект {@link AdvertisementDto} с данными объявления для удаления
     * @return объект {@link ResponseEntity} с сообщением об успешном удалении и кодом 200 OK в случае успеха
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAdvertisement(@RequestBody @Valid AdvertisementDto dto) {
        service.delete(converter.getAdvertisementFromDto(dto));
        return ResponseEntity.ok("Deleted advertisement with headline " + dto.getHeadline());
    }
}
