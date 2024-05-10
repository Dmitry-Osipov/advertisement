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
import org.springframework.web.bind.annotation.RestController;
import rf.senla.advertisement.domain.dto.AdvertisementDto;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.service.AdvertisementService;
import rf.senla.advertisement.domain.utils.DtoConverter;

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
    private final AdvertisementService service;

    /**
     * Получить объявление по его заголовку.
     * @param headline заголовок объявления
     * @return объект {@link ResponseEntity} с объявлением и кодом 200 OK в случае успеха
     */
    @GetMapping("/{headline}")
    public ResponseEntity<AdvertisementDto> getAdvertisementByHeadline(@PathVariable("headline") String headline) {
        return ResponseEntity.ok(DtoConverter.getDtoFromAdvertisement(service.getByHeadline(headline)));
    }

    /**
     * Получить все объявления.
     * @return объект {@link ResponseEntity} со списком объявлений и кодом 200 OK в случае успеха
     */
    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisements() {
        return ResponseEntity.ok(DtoConverter.getListAdvertisementDto(service.getAll()));
    }

    /**
     * Создать новое объявление.
     * @param dto объект {@link AdvertisementDto} с данными нового объявления
     * @return объект {@link ResponseEntity} с созданным объявлением и кодом 200 OK в случае успеха
     */
    @PostMapping
    public ResponseEntity<AdvertisementDto> createAdvertisement(@RequestBody @Valid AdvertisementDto dto) {
        Advertisement advertisement = service.save(DtoConverter.getAdvertisementFromDto(dto));
        return ResponseEntity.ok(DtoConverter.getDtoFromAdvertisement(advertisement));
    }

    /**
     * Обновить существующее объявление.
     * @param dto объект {@link AdvertisementDto} с обновленными данными объявления
     * @return объект {@link ResponseEntity} с обновленным объявлением и кодом 200 OK в случае успеха
     */
    @PutMapping
    public ResponseEntity<AdvertisementDto> updateAdvertisement(@RequestBody @Valid AdvertisementDto dto) {
        Advertisement advertisement = service.update(DtoConverter.getAdvertisementFromDto(dto));
        return ResponseEntity.ok(DtoConverter.getDtoFromAdvertisement(advertisement));
    }

    /**
     * Удалить объявление.
     * @param dto объект {@link AdvertisementDto} с данными объявления для удаления
     * @return объект {@link ResponseEntity} с сообщением об успешном удалении и кодом 200 OK в случае успеха
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAdvertisement(@RequestBody @Valid AdvertisementDto dto) {
        service.delete(DtoConverter.getAdvertisementFromDto(dto));
        return ResponseEntity.ok("Deleted advertisement with headline " + dto.getHeadline());
    }
}
