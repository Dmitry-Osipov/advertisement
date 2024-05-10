package rf.senla.advertisement.security.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import rf.senla.advertisement.security.entity.User;

public final class CurrentUserValidator {
    private CurrentUserValidator() {}

    public static boolean isCurrentUser(String username) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return currentUser.getUsername().equals(username);
    }
}
