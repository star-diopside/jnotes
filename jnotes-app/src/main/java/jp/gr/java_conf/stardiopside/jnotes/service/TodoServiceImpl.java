package jp.gr.java_conf.stardiopside.jnotes.service;

import jakarta.transaction.Transactional;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.Todo;
import jp.gr.java_conf.stardiopside.jnotes.data.repository.TodoRepository;
import jp.gr.java_conf.stardiopside.jnotes.value.Around;
import jp.gr.java_conf.stardiopside.jnotes.value.IdOnly;
import jp.gr.java_conf.stardiopside.jnotes.value.Node;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    @Transactional
    public List<Todo> list() {
        return todoRepository.findAll(Sort.by("id").ascending());
    }

    @Override
    @Transactional
    public Optional<Todo> find(Long id) {
        return todoRepository.findById(id);
    }

    @Override
    @Transactional
    public Around<OptionalLong> findAround(Long id) {
        var prev = todoRepository
                .findFirstByIdLessThanOrderByIdDesc(id, IdOnly.class)
                .stream().mapToLong(IdOnly::id).findFirst();
        var next = todoRepository
                .findFirstByIdGreaterThanOrderByIdAsc(id, IdOnly.class)
                .stream().mapToLong(IdOnly::id).findFirst();
        return new Around<>(prev, next);
    }

    @Override
    @Transactional
    public Node<Optional<Todo>, OptionalLong> findWithAround(Long id) {
        var todo = find(id);
        var around = findAround(id);
        return new Node<>(todo, around.prev(), around.next());
    }

    @Override
    @Transactional
    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    @Transactional
    public void delete(Todo todo) {
        todoRepository.delete(todo);
    }
}
