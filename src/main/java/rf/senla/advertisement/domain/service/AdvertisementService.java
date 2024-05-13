package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.exception.AccessDeniedException;
import rf.senla.advertisement.domain.exception.ErrorMessage;
import rf.senla.advertisement.domain.exception.NoEntityException;
import rf.senla.advertisement.domain.exception.TechnicalException;
import rf.senla.advertisement.domain.repository.AdvertisementRepository;
import rf.senla.advertisement.domain.utils.comparator.AscendingPriceAdvertisementComparator;
import rf.senla.advertisement.domain.utils.comparator.DescendingPriceAdvertisementComparator;
import rf.senla.advertisement.domain.utils.comparator.RatingAdvertisementComparator;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.repository.UserRepository;
import rf.senla.advertisement.security.utils.CurrentUserValidator;

import java.util.List;
import java.util.stream.Stream;

/**
 * Сервис для работы с объявлениями.
 */
@Service
@RequiredArgsConstructor
public class AdvertisementService implements IAdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Advertisement save(Advertisement advertisement) {
        User user = userRepository.findByUsername(advertisement.getUser().getUsername())
                .orElseThrow(() -> new NoEntityException(ErrorMessage.NO_USER_FOUND.getMessage()));
        advertisement.setUser(user);
        return advertisementRepository.save(advertisement);
    }

    @Transactional
    @Override
    public Advertisement update(Advertisement advertisement) {
        User user = advertisement.getUser();

        if (!CurrentUserValidator.isCurrentUser(user) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException(ErrorMessage.USER_IS_NOT_ADMIN_OR_AUTHOR.getMessage());
        }

        return advertisementRepository.save(advertisement);
    }

    @Transactional
    @Override
    public void delete(Advertisement advertisement) {
        User user = advertisement.getUser();

        if (!CurrentUserValidator.isCurrentUser(user) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException(ErrorMessage.USER_IS_NOT_ADMIN_OR_AUTHOR.getMessage());
        }

        advertisementRepository.delete(advertisement);
    }

    @Override
    public List<Advertisement> getAll() {
        return advertisementsSorted(advertisementRepository.findAll(), null);
    }

    @Override
    public List<Advertisement> getAll(String headline, String sortBy) {
        return advertisementsSorted(advertisementRepository.findAllByHeadlineIgnoreCase(headline), sortBy);
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
            return advertisementsSorted(advertisementRepository.findByPriceBetween(min, max), sortBy);
        }

        return advertisementsSorted(
                advertisementRepository.findByPriceBetweenAndHeadlineIgnoreCase(min, max, headline), sortBy);
    }

    @Override
    public List<Advertisement> getAll(User user, String sortBy) {
        return advertisementsSorted(advertisementRepository.findByUser(user), sortBy);
    }

    // TODO: следующий шаг - просмотр истории объявлений пользователя. Требуется решить, где располагать метод +
    //  добавить сервис миграций, чтобы подправить БД

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
