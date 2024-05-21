package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.exception.EntityContainedException;
import rf.senla.advertisement.domain.exception.ErrorMessage;
import rf.senla.advertisement.domain.exception.NoEntityException;
import rf.senla.advertisement.domain.exception.TechnicalException;
import rf.senla.advertisement.domain.repository.AdvertisementRepository;
import rf.senla.advertisement.security.entity.User;

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
        if (entity.getId() != null && repository.existsById(entity.getId())) {
            throw new EntityContainedException(ErrorMessage.ADVERTISEMENT_ALREADY_EXISTS.getMessage());
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public Advertisement update(Advertisement entity) {
        if (!repository.existsById(entity.getId())) {
            throw new NoEntityException(ErrorMessage.NO_ADVERTISEMENT_FOUND.getMessage());
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Advertisement entity) {
        if (!repository.existsById(entity.getId())) {
            throw new NoEntityException(ErrorMessage.NO_ADVERTISEMENT_FOUND.getMessage());
        }

        repository.delete(entity);
    }

    @Override
    public List<Advertisement> getAll() {
        return repository.findAllInOrderByUserRating(getPageable("rating", 0, 10));
    }

    @Override
    public List<Advertisement> getAll(Integer min, Integer max, String headline, String sortBy,
                                      Integer page, Integer size) {
        if (min == null) {
            min = 0;
        }

        if (max == null) {
            max = Integer.MAX_VALUE;
        }

        checkPrices(min, max);

        Pageable pageable = getPageable(sortBy, page, size);

        if (headline == null) {
            return repository.findByPriceBetweenInOrder(min, max, pageable);
        }

        return repository.findByPriceBetweenAndHeadlineIgnoreCaseInOrder(min, max, headline, pageable);
    }

    @Override
    public List<Advertisement> getAll(User user, String sortBy, Boolean active, Integer page, Integer size) {
        Pageable pageable = getPageable(sortBy, page, size);

        if (Boolean.FALSE.equals(active) || active == null) {
            return repository.findByUserInOrderWithAnyStatus(user, pageable);
        }

        return repository.findByUserInOrder(user, pageable);
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
    private void checkPrices(Integer min, Integer max) {
        if (min < 0) {
            throw new TechnicalException(ErrorMessage.PRICE_IS_NEGATIVE.getMessage());
        }

        if (min > max) {
            throw new TechnicalException(ErrorMessage.MIN_PRICE_IS_HIGHEST.getMessage());
        }
    }

    /**
     * Служебный метод на основе переданного типа сортировки формирует пагинацию.
     * @param sortBy способ сортировки
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return пагинация
     */
    private Pageable getPageable(String sortBy, Integer page, Integer size) {
        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 10;
        }

        Sort sort;
        switch (sortBy) {
            case "asc" -> sort = Sort.by(Sort.Direction.ASC, "price");
            case "desc" -> sort = Sort.by(Sort.Direction.DESC, "price");
            case null, default -> sort = Sort.by(Sort.Direction.DESC, "user.boosted", "user.rating");
        }

        return PageRequest.of(page, size, sort);
    }
}
