package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Message;
import rf.senla.advertisement.domain.exception.EntityContainedException;
import rf.senla.advertisement.domain.exception.ErrorMessage;
import rf.senla.advertisement.domain.exception.NoEntityException;
import rf.senla.advertisement.domain.repository.MessageRepository;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;

import java.util.List;

/**
 * Сервис работы с сообщениями.
 */
@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {
    private final MessageRepository repository;

    @Transactional
    @Override
    public Message save(Message entity) {
        if (entity.getId() != null && repository.existsById(entity.getId())) {
            throw new EntityContainedException(ErrorMessage.MESSAGE_ALREADY_EXISTS.getMessage());
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public Message update(Message entity) {
        if (!repository.existsById(entity.getId())) {
            throw new NoEntityException(ErrorMessage.NO_MESSAGE_FOUND.getMessage());
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Message entity) {
        if (!repository.existsById(entity.getId())) {
            throw new NoEntityException(ErrorMessage.NO_MESSAGE_FOUND.getMessage());
        }

        repository.delete(entity);
    }

    @Override
    public List<Message> getAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = getPageable(0, 10);
        return user.getRole().equals(Role.ROLE_ADMIN) ? repository.findAll(pageable).getContent() :
                repository.findAllByUserId(user.getId(), pageable);
    }

    @Override
    public List<Message> getAll(User user, Integer page, Integer size) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findMessagesBetweenUsers(currentUser.getId(), user.getId(), getPageable(page, size));
    }

    /**
     * Служебный метод формирует пагинацию по времени.
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return пагинация
     */
    private Pageable getPageable(Integer page, Integer size) {
        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 10;
        }

        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sentAt"));
    }
}
