package rf.senla.web.utils;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import rf.senla.domain.dto.AdvertisementDto;
import rf.senla.domain.dto.CreateAdvertisementRequest;
import rf.senla.domain.entity.Advertisement;

import java.util.List;

// TODO: javadoc
@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdvertisementMapper {
    AdvertisementDto toDto(Advertisement advertisement);
    Advertisement toEntity(CreateAdvertisementRequest request);
    Advertisement toEntity(AdvertisementDto dto);
    List<AdvertisementDto> toDtos(List<Advertisement> advertisements);
}
