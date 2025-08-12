package jp.gr.java_conf.stardiopside.jnotes.data.repository;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
