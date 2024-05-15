package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Message;
import rf.senla.advertisement.domain.exception.ErrorMessage;
import rf.senla.advertisement.domain.repository.MessageRepository;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.exception.UserMismatchException;

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
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getUsername().equals(entity.getSender().getUsername())) {
            throw new UserMismatchException(ErrorMessage.USER_IS_NOT_AUTHOR.getMessage());
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public Message update(Message entity) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!entity.getSender().getUsername().equals(currentUser.getUsername())) {
            throw new UserMismatchException(ErrorMessage.USER_IS_NOT_AUTHOR.getMessage());
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Message entity) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!entity.getSender().getUsername().equals(currentUser.getUsername())) {
            throw new UserMismatchException(ErrorMessage.USER_IS_NOT_AUTHOR.getMessage());
        }

        repository.delete(entity);
    }

    @Override
    public List<Message> getAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getRole().equals(Role.ROLE_ADMIN) ? repository.findAll() :
                repository.findAllInOrderBySentAtDesc(user.getId());
    }

    @Override
    public List<Message> getAll(User user) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findMessagesBetweenUsers(currentUser.getId(), user.getId());
    }
}
