package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.entity.Comment;
import rf.senla.advertisement.domain.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository repository;

    @Transactional
    @Override
    public Comment save(Comment entity) {
        return repository.save(entity);
    }

    @Transactional
    @Override
    public Comment update(Comment entity) {
        return repository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Comment entity) {
        repository.delete(entity);
    }

    @Override
    public List<Comment> getAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Comment> getAll(Advertisement advertisement) {
        return repository.findByAdvertisementOrderByCreatedAtDesc(advertisement);
    }
}
