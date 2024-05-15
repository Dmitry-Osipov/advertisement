package rf.senla.advertisement.security.utils.validator;

import lombok.experimental.UtilityClass;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;

/**
 * Утилитарный класс для проверки, является ли указанный пользователь текущим пользователем или админом.
 */
@UtilityClass
public final class UserPermissionsValidator {
    /**
     * Проверяет, является ли указанный пользователь текущим пользователем или админом.
     * @param user Пользователь для проверки.
     * @return {@code true}, если указанный пользователь совпадает с текущим пользователем или имеет статус админа,
     * в противном случае - {@code false}.
     */
    public boolean validate(final User user) {
        return CurrentUserValidator.isCurrentUser(user) || user.getRole().equals(Role.ROLE_ADMIN);
    }
}
