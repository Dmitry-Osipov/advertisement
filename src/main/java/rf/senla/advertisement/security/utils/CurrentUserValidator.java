package rf.senla.advertisement.security.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import rf.senla.advertisement.security.entity.User;

/**
 * Утилитарный класс для проверки, является ли указанный пользователь текущим пользователем.
 */
@UtilityClass
public final class CurrentUserValidator {
    /**
     * Проверяет, является ли указанный пользователь текущим пользователем.
     * @param user Пользователь для проверки.
     * @return {@code true}, если указанный пользователь совпадает с текущим пользователем, в противном случае -
     * {@code false}.
     */
    public boolean isCurrentUser(User user) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return currentUser.getUsername().equals(user.getUsername());
    }
}
