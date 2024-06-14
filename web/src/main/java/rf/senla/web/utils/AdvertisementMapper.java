package rf.senla.web.utils;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import rf.senla.web.dto.AdvertisementDto;
import rf.senla.web.dto.CreateAdvertisementRequest;
import rf.senla.domain.entity.Advertisement;
import rf.senla.web.dto.UpdateAdvertisementRequest;

import java.util.List;

/**
 * Маппер объявлений
 */
@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdvertisementMapper {
    /**
     * Метод маппит сущность {@link Advertisement} в DTO {@link AdvertisementDto}
     * @param advertisement объявление
     * @return DTO объявления
     */
    AdvertisementDto toDto(Advertisement advertisement);

    /**
     * Метод маппит запрос на создание объявления {@link CreateAdvertisementRequest} в сущность {@link Advertisement}
     * @param request запрос на создание объявления
     * @return объявление
     */
    Advertisement toEntity(CreateAdvertisementRequest request);

    /**
     * Метод маппит запрос на обновление объявления {@link UpdateAdvertisementRequest} в сущность {@link Advertisement}
     * @param request запрос на обновление объявления
     * @return объявление
     */
    Advertisement toEntity(UpdateAdvertisementRequest request);

    /**
     * Метод маппит DTO {@link AdvertisementDto} в сущность {@link Advertisement}
     * @param dto DTO объявления
     * @return объявление
     */
    Advertisement toEntity(AdvertisementDto dto);

    /**
     * Метод маппит список сущностей {@link Advertisement} в список DTO {@link AdvertisementDto}
     * @param advertisements список объявлений
     * @return список DTO объявлений
     */
    List<AdvertisementDto> toDtos(List<Advertisement> advertisements);
}
