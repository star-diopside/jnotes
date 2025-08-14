package jp.gr.java_conf.stardiopside.jnotes.service;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileInfo;
import jp.gr.java_conf.stardiopside.jnotes.value.DownloadData;
import jp.gr.java_conf.stardiopside.jnotes.value.FileInfoData;
import jp.gr.java_conf.stardiopside.jnotes.value.FileInfoOnly;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FileService {

    List<FileInfoData> list();

    Optional<FileInfoOnly> findFileInfo(Long id);

    Optional<DownloadData> findDownloadData(Long id);

    Optional<FileInfoData> findFileInfoData(Long id);

    FileInfo save(MultipartFile file);

    Optional<FileInfo> update(@Nullable MultipartFile file, FileInfo fileInfo, Integer fileDataVersion);

    void delete(FileInfo fileInfo);

}
