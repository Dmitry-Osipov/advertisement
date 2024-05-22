package rf.senla.advertisement.security.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.exception.EntityContainedException;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.exception.ErrorMessage;
import rf.senla.advertisement.security.repository.UserRepository;

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
        if (!repository.existsByUsername(user.getUsername())) {
            throw new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return save(user);
    }

    @Transactional
    @Override
    public void delete(User user) {
        String username = user.getUsername();
        if (!repository.existsByUsername(username)) {
            throw new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage());
        }

        repository.deleteByUsername(username);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll(getPageable(0, 10)).getContent();
    }

    @Override
    public List<User> getAll(Integer page, Integer size) {
        return repository.findAll(getPageable(page, size)).getContent();
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

    @Transactional
    @Override
    public void create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new EntityContainedException(ErrorMessage.USERNAME_ALREADY_EXISTS.getMessage());
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new EntityContainedException(ErrorMessage.EMAIL_ALREADY_EXISTS.getMessage());
        }

        save(user);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    @Transactional
    @Override
    public void setBoosted(String username) {
        User user = getByUsername(username);
        user.setBoosted(true);
        save(user);
    }

    @Override
    public User getByResetPasswordToken(String token) {
        return repository.findByResetPasswordToken(token)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage()));
    }

    /**
     * Служебный метод возвращает пагинацию топовых пользователей.
     * @param page Порядковый номер страницы.
     * @param size Размер страницы.
     * @return Пагинация
     */
    private Pageable getPageable(Integer page, Integer size) {
        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 10;
        }

        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boosted", "rating"));
    }
}
