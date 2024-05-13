package rf.senla.advertisement.domain.utils.comparator;

import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.security.entity.User;

import java.util.Comparator;

/**
 * Компаратор для сравнения объявлений на основе рейтинга пользователя и его статуса "boosted".
 * Объявления с продвижением пользователя ({@code boosted == true}) будут иметь приоритет перед объявлениями
 * пользователей без продвижения. В случае, если оба объявления имеют продвижение или не имеют его, сравнивается их
 * рейтинг.
 */
public final class RatingAdvertisementComparator implements Comparator<Advertisement> {
    @Override
    public int compare(Advertisement o1, Advertisement o2) {
        User firstUser = o1.getUser();
        User secondUser = o2.getUser();

        if (Boolean.TRUE.equals(firstUser.getBoosted()) && Boolean.FALSE.equals(secondUser.getBoosted())) {
            return -1;
        }

        if (Boolean.FALSE.equals(firstUser.getBoosted()) && Boolean.TRUE.equals(secondUser.getBoosted())) {
            return 1;
        }

        return secondUser.getRating() - firstUser.getRating();
    }
}
