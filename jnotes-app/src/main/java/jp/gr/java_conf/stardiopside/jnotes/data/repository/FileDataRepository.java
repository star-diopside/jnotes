package jp.gr.java_conf.stardiopside.jnotes.data.repository;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
}
