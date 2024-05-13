package rf.senla.advertisement.domain.utils.comparator;

import rf.senla.advertisement.domain.entity.Advertisement;

import java.util.Comparator;

/**
 * Компаратор для сравнения объявлений по цене в порядке убывания.
 * Объявления с более высокой ценой будут иметь более низкий приоритет.
 */
public final class DescendingPriceAdvertisementComparator implements Comparator<Advertisement> {
    @Override
    public int compare(Advertisement o1, Advertisement o2) {
        return o2.getPrice().compareTo(o1.getPrice());
    }
}
