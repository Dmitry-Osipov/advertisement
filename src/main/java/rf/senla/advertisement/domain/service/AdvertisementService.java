package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.exception.AccessDeniedException;
import rf.senla.advertisement.domain.exception.ErrorMessage;
import rf.senla.advertisement.domain.exception.NoEntityException;
import rf.senla.advertisement.domain.repository.AdvertisementRepository;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.repository.UserRepository;
import rf.senla.advertisement.security.utils.CurrentUserValidator;

import java.util.List;

/**
 * Сервис для работы с объявлениями.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AdvertisementService implements IService<Advertisement> {
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    /**
     * Получить объявление по его заголовку.
     * @param headline заголовок объявления
     * @return найденное объявление
     * @throws NoEntityException если объявление не найдено
     */
    public Advertisement getByHeadline(String headline) {
        return advertisementRepository.findByHeadlineIgnoreCase(headline)
                .orElseThrow(() -> new NoEntityException(ErrorMessage.NO_ADVERTISEMENT_FOUND.getMessage()));
    }

    @Override
    public Advertisement save(Advertisement advertisement) {
        User user = userRepository.findByUsername(advertisement.getUser().getUsername())
                .orElseThrow(() -> new NoEntityException(ErrorMessage.NO_USER_FOUND.getMessage()));
        advertisement.setUser(user);
        return advertisementRepository.save(advertisement);
    }

    @Override
    public Advertisement update(Advertisement advertisement) {
        User user = advertisement.getUser();

        if (!CurrentUserValidator.isCurrentUser(user) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException(ErrorMessage.USER_IS_NOT_ADMIN_OR_AUTHOR.getMessage());
        }

        return advertisementRepository.save(advertisement);
    }

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
        return advertisementRepository.findAll();
    }
}
