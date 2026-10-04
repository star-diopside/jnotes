package jp.gr.java_conf.stardiopside.jnotes.data.repository;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    <T> Optional<T> findFirstByIdLessThanOrderByIdDesc(Long id, Class<T> type);

    <T> Optional<T> findFirstByIdGreaterThanOrderByIdAsc(Long id, Class<T> type);

}
