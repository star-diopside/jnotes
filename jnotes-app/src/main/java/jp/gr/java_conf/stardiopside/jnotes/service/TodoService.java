package jp.gr.java_conf.stardiopside.jnotes.service;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.Todo;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public interface TodoService {

    List<Todo> list();

    Optional<Todo> find(Long id);

    Around<OptionalLong> findAround(Long id);

    Node<Optional<Todo>, OptionalLong> findWithAround(Long id);

    Todo save(Todo todo);

    void delete(Todo todo);

}
