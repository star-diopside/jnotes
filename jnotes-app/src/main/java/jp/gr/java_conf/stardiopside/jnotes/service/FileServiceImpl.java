package jp.gr.java_conf.stardiopside.jnotes.service;

import jakarta.transaction.Transactional;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileData;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileInfo;
import jp.gr.java_conf.stardiopside.jnotes.data.repository.FileInfoRepository;
import jp.gr.java_conf.stardiopside.jnotes.value.DownloadData;
import jp.gr.java_conf.stardiopside.jnotes.value.FileInfoData;
import jp.gr.java_conf.stardiopside.jnotes.value.FileInfoOnly;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    private final FileInfoRepository fileInfoRepository;

    public FileServiceImpl(FileInfoRepository fileInfoRepository) {
        this.fileInfoRepository = fileInfoRepository;
    }

    @Override
    @Transactional
    public List<FileInfoData> list() {
        return fileInfoRepository.findFileInfoData(Sort.by("id").ascending());
    }

    @Override
    @Transactional
    public Optional<FileInfoOnly> findFileInfo(Long id) {
        return fileInfoRepository.findById(id, FileInfoOnly.class);
    }

    @Override
    @Transactional
    public Optional<DownloadData> findDownloadData(Long id) {
        return fileInfoRepository.findById(id)
                .map(fileInfo -> new DownloadData(
                        fileInfo.getFileName(),
                        fileInfo.getContentType(),
                        fileInfo.getFileData().getData()));
    }

    @Override
    @Transactional
    public Optional<FileInfoData> findFileInfoData(Long id) {
        return fileInfoRepository.findFileInfoDataById(id);
    }

    @Override
    @Transactional
    public FileInfo save(MultipartFile file) {
        byte[] data;
        try {
            data = file.getBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        var fileInfo = FileInfo.builder()
                .fileName(Objects.requireNonNull(file.getOriginalFilename()))
                .contentType(file.getContentType())
                .length(data.length)
                .hashValue(new DigestUtils(MessageDigestAlgorithms.SHA3_256).digestAsHex(data))
                .build();
        var fileData = FileData.builder()
                .fileInfo(fileInfo)
                .data(data)
                .build();
        fileInfo.setFileData(fileData);
        fileInfoRepository.save(fileInfo);

        return fileInfo;
    }

    @Override
    @Transactional
    public Optional<FileInfo> update(@Nullable MultipartFile file, FileInfo fileInfo, Integer fileDataVersion) {
        return fileInfoRepository.findById(fileInfo.getId()).map(info -> {
            if (file != null && StringUtils.isNotEmpty(file.getOriginalFilename())) {
                byte[] data;
                try {
                    data = file.getBytes();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }

                info.setFileName(file.getOriginalFilename());
                info.setContentType(file.getContentType());
                info.setLength(data.length);
                info.setHashValue(new DigestUtils(MessageDigestAlgorithms.SHA3_256).digestAsHex(data));

                var fileData = info.getFileData();
                fileData.setData(data);
                fileData.setVersion(fileDataVersion);
            }

            if (StringUtils.isNotEmpty(fileInfo.getFileName())) {
                info.setFileName(fileInfo.getFileName());
            }
            info.setVersion(fileInfo.getVersion());

            return fileInfoRepository.save(info);
        });
    }

    @Override
    @Transactional
    public void delete(FileInfo fileInfo) {
        fileInfoRepository.delete(fileInfo);
    }
}
