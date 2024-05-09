package rf.senla.advertisement.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.exception.EntityContainedException;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.repository.UserRepository;

/**
 * Сервис для управления пользователями.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * Сохранение пользователя
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Создание пользователя
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new EntityContainedException("Username already exists");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new EntityContainedException("Email already exists");
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
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

    /**
     * Выдача прав администратора пользователю
     * @param username имя пользователя
     */
    public void setAdminRole(String username) {
        User user = getByUsername(username);
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}
