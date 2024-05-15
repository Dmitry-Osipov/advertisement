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
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.utils.validator.UserPermissionsValidator;

import java.util.List;

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
        return repository.findAllInOrderByUserRating();
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
            return repository.findByPriceBetweenInOrder(min, max, sortBy);
        }

        return repository.findByPriceBetweenAndHeadlineIgnoreCaseInOrder(min, max, headline, sortBy);
    }

    @Override
    public List<Advertisement> getAll(User user, String sortBy, Boolean active) {
        if (Boolean.FALSE.equals(active) || active == null) {
            return repository.findByUserInOrderWithAnyStatus(user, sortBy);
        }

        return repository.findByUserInOrder(user, sortBy);
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
}
