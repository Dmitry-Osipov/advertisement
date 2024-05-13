package rf.senla.advertisement.domain.utils.comparator;

import rf.senla.advertisement.domain.entity.Advertisement;

import java.util.Comparator;

/**
 * Компаратор для сравнения объявлений по цене в порядке возрастания.
 * Объявления с более низкой ценой будут иметь более высокий приоритет.
 */
public final class AscendingPriceAdvertisementComparator implements Comparator<Advertisement> {
    @Override
    public int compare(Advertisement o1, Advertisement o2) {
        return o1.getPrice().compareTo(o2.getPrice());
    }
}
