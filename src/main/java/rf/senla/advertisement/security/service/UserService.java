package rf.senla.advertisement.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.exception.EntityContainedException;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.exception.ErrorMessage;
import rf.senla.advertisement.security.exception.UserMismatchException;
import rf.senla.advertisement.security.repository.UserRepository;
import rf.senla.advertisement.security.utils.CurrentUserValidator;

import java.util.List;

/**
 * Сервис для управления пользователями.
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository repository;

    @Transactional
    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Transactional
    @Override
    public User update(User user) {
        if ((!CurrentUserValidator.isCurrentUser(user) && !user.getRole().equals(Role.ROLE_ADMIN))) {
            throw new UserMismatchException(ErrorMessage.USERS_DO_NOT_MATCH.getMessage());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return save(user);
    }

    @Transactional
    @Override
    public void delete(User user) {
        if ((!CurrentUserValidator.isCurrentUser(user) && !user.getRole().equals(Role.ROLE_ADMIN))) {
            throw new UserMismatchException(ErrorMessage.USERS_DO_NOT_MATCH.getMessage());
        }

        repository.deleteByUsername(user.getUsername());
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage()));
    }

    @Transactional
    @Override
    public User updatePassword(String username, String oldPassword, String newPassword) {
        User user = getByUsername(username);

        if (!CurrentUserValidator.isCurrentUser(user)) {
            throw new UserMismatchException(ErrorMessage.USERS_DO_NOT_MATCH.getMessage());
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException(ErrorMessage.PASSWORDS_DO_NOT_MATCH.getMessage());
        }

        newPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newPassword);
        return save(user);
    }

    @Transactional
    @Override
    public void setAdminRole(String username) {
        User user = getByUsername(username);
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }

    /**
     * Создание пользователя
     * @return созданный пользователь
     * @throws IllegalArgumentException - в случае, если данная сущность является null.
     * @throws OptimisticLockingFailureException - если сущность использует оптимистическую блокировку и имеет атрибут
     * version со значением, отличным от того, что находится в хранилище персистентности. Также выбрасывается, если
     * предполагается, что сущность присутствует, но не существует в базе данных.
     */
    @Transactional
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new EntityContainedException(ErrorMessage.USERNAME_ALREADY_EXISTS.getMessage());
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new EntityContainedException(ErrorMessage.EMAIL_ALREADY_EXISTS.getMessage());
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}
