package rf.senla.advertisement.domain.utils.comparator;

import rf.senla.advertisement.domain.entity.Comment;

import java.util.Comparator;

/**
 * Компаратор для сравнения комментариев по времени в порядке убывания.
 * Комментарии с более поздним временем создания будут иметь более высокий приоритет.
 */
public class CommentCreatedTimeComparator implements Comparator<Comment> {
    @Override
    public int compare(Comment o1, Comment o2) {
        return o2.getCreatedAt().compareTo(o1.getCreatedAt());
    }
}
