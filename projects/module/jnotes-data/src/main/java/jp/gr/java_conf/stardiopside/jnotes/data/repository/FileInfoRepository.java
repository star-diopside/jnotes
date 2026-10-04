package jp.gr.java_conf.stardiopside.jnotes.data.repository;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileInfo;
import jp.gr.java_conf.stardiopside.jnotes.value.FileInfoData;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

    <T> Optional<T> findById(Long id, Class<T> type);

    @Query("""
            select
              new jp.gr.java_conf.stardiopside.jnotes.value.FileInfoData(
                info.id,
                info.fileName,
                info.contentType,
                info.length,
                info.hashValue,
                info.createdAt,
                info.updatedAt,
                info.version,
                data.id,
                data.updatedAt,
                data.version
              )
            from
              FileInfo info
              inner join FileData data
              on info.id = data.fileInfoId
            """)
    List<FileInfoData> findFileInfoData(Sort sort);

    @Query("""
            select
              new jp.gr.java_conf.stardiopside.jnotes.value.FileInfoData(
                info.id,
                info.fileName,
                info.contentType,
                info.length,
                info.hashValue,
                info.createdAt,
                info.updatedAt,
                info.version,
                data.id,
                data.updatedAt,
                data.version
              )
            from
              FileInfo info
              inner join FileData data
              on info.id = data.fileInfoId
            where
              info.id = :id
            """)
    Optional<FileInfoData> findFileInfoDataById(Long id);

}
