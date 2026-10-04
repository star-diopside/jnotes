package jp.gr.java_conf.stardiopside.jnotes.data.repository;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
