package jp.gr.java_conf.stardiopside.jnotes.data.repository;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
}
