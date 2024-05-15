package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.entity.AdvertisementStatus;
import rf.senla.advertisement.domain.exception.AccessDeniedException;
import rf.senla.advertisement.domain.exception.ErrorMessage;
import rf.senla.advertisement.domain.exception.NoEntityException;
import rf.senla.advertisement.domain.exception.TechnicalException;
import rf.senla.advertisement.domain.repository.AdvertisementRepository;
import rf.senla.advertisement.domain.utils.comparator.AscendingPriceAdvertisementComparator;
import rf.senla.advertisement.domain.utils.comparator.DescendingPriceAdvertisementComparator;
import rf.senla.advertisement.domain.utils.comparator.RatingAdvertisementComparator;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.utils.validator.UserPermissionsValidator;

import java.util.List;
import java.util.stream.Stream;

/**
 * Сервис для работы с объявлениями.
 */
@Service
@RequiredArgsConstructor
public class AdvertisementService implements IAdvertisementService {
    private final AdvertisementRepository repository;

    @Transactional
    @Override
    public Advertisement save(Advertisement entity) {
        return repository.save(entity);
    }

    @Transactional
    @Override
    public Advertisement update(Advertisement entity) {
        User user = entity.getUser();

        if (!UserPermissionsValidator.validate(user)) {
            throw new AccessDeniedException(ErrorMessage.USER_IS_NOT_ADMIN_OR_AUTHOR.getMessage());
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Advertisement entity) {
        User user = entity.getUser();

        if (!UserPermissionsValidator.validate(user)) {
            throw new AccessDeniedException(ErrorMessage.USER_IS_NOT_ADMIN_OR_AUTHOR.getMessage());
        }

        repository.delete(entity);
    }

    @Override
    public List<Advertisement> getAll() {
        return advertisementsFilteredAndSorted(repository.findAll(), null);
    }

    @Override
    public List<Advertisement> getAll(String headline, String sortBy) {
        return advertisementsFilteredAndSorted(repository.findAllByHeadlineIgnoreCase(headline), sortBy);
    }

    @Override
    public List<Advertisement> getAll(Integer min, Integer max, String headline, String sortBy) {
        if (min == null) {
            min = 0;
        }

        if (max == null) {
            max = Integer.MAX_VALUE;
        }

        checkPrices(min, max);

        if (headline == null) {
            return advertisementsFilteredAndSorted(repository.findByPriceBetween(min, max), sortBy);
        }

        return advertisementsFilteredAndSorted(
                repository.findByPriceBetweenAndHeadlineIgnoreCase(min, max, headline), sortBy);
    }

    @Override
    public List<Advertisement> getAll(User user, String sortBy, Boolean active) {
        if (Boolean.FALSE.equals(active) || active == null) {
            return advertisementsSorted(repository.findByUser(user), sortBy);
        }

        return advertisementsFilteredAndSorted(repository.findByUser(user), sortBy);
    }

    @Override
    public Advertisement getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoEntityException(ErrorMessage.NO_ADVERTISEMENT_FOUND.getMessage()));
    }

    /**
     * Служебный метод проводит проверку цен.
     * @param min минимальная цена
     * @param max максимальная цена
     * @throws TechnicalException если минимальная цена больше максимальной или какая-либо цена меньше 0
     */
    private static void checkPrices(Integer min, Integer max) {
        if (min < 0) {
            throw new TechnicalException(ErrorMessage.PRICE_IS_NEGATIVE.getMessage());
        }

        if (min > max) {
            throw new TechnicalException(ErrorMessage.MIN_PRICE_IS_HIGHEST.getMessage());
        }
    }

    /**
     * Служебный метод фильтрует и сортирует список объявлений.
     * @param advertisements список объявлений, который требуется отсортировать
     * @param sortBy условие сортировки
     * @return отсортированный список объявлений
     */
    private static List<Advertisement> advertisementsFilteredAndSorted(List<Advertisement> advertisements,
                                                                       String sortBy) {
        List<Advertisement> sortedAdvertisements;
        switch (sortBy) {
            case "asc" ->  sortedAdvertisements =
                    getAdvertisementStream(advertisements)
                            .filter(advertisement -> advertisement.getStatus().equals(AdvertisementStatus.ACTIVE))
                            .sorted(new AscendingPriceAdvertisementComparator())
                            .toList();
            case "desc" -> sortedAdvertisements =
                    getAdvertisementStream(advertisements)
                            .filter(advertisement -> advertisement.getStatus().equals(AdvertisementStatus.ACTIVE))
                            .sorted(new DescendingPriceAdvertisementComparator())
                            .toList();
            case null, default -> sortedAdvertisements =
                    getAdvertisementStream(advertisements)
                            .filter(advertisement -> advertisement.getStatus().equals(AdvertisementStatus.ACTIVE))
                            .sorted(new RatingAdvertisementComparator())
                            .toList();
        }
        return sortedAdvertisements;
    }

    /**
     * Служебный метод сортирует список объявлений.
     * @param advertisements список объявлений, который требуется отсортировать
     * @param sortBy условие сортировки
     * @return отсортированный список объявлений
     */
    private static List<Advertisement> advertisementsSorted(List<Advertisement> advertisements, String sortBy) {
        List<Advertisement> sortedAdvertisements;
        switch (sortBy) {
            case "asc" ->  sortedAdvertisements =
                    getAdvertisementStream(advertisements).sorted(new AscendingPriceAdvertisementComparator()).toList();
            case "desc" -> sortedAdvertisements =
                    getAdvertisementStream(advertisements).sorted(new DescendingPriceAdvertisementComparator()).toList();
            case null, default -> sortedAdvertisements =
                    getAdvertisementStream(advertisements).sorted(new RatingAdvertisementComparator()).toList();
        }
        return sortedAdvertisements;
    }

    /**
     * Служебный метод формирует поток из списка объявлений.
     * @param advertisements список объявлений
     * @return поток объявлений
     */
    private static Stream<Advertisement> getAdvertisementStream(List<Advertisement> advertisements) {
        return advertisements.stream()
                .map(advertisement -> {
                    User user = advertisement.getUser();
                    if (user.getRating() == null) {
                        user.setRating(0);
                    }
                    return advertisement;
                });
    }
}
