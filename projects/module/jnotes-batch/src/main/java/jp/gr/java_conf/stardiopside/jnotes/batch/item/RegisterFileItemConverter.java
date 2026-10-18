package jp.gr.java_conf.stardiopside.jnotes.batch.item;

import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileData;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.FileInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class RegisterFileItemConverter implements ItemProcessor<Path, FileInfo> {

    @Override
    public @Nullable FileInfo process(Path item) throws Exception {
        byte[] data = Files.readAllBytes(item);
        String contentType = Objects.requireNonNullElse(Files.probeContentType(item),
                MediaType.APPLICATION_OCTET_STREAM_VALUE);

        var fileInfo = FileInfo.builder()
                .fileName(item.getFileName().toString())
                .contentType(contentType)
                .originalContentType(contentType)
                .length(data.length)
                .hashValue(new DigestUtils(MessageDigestAlgorithms.SHA3_256).digestAsHex(data))
                .build();
        var fileData = FileData.builder()
                .fileInfo(fileInfo)
                .data(data)
                .build();
        fileInfo.setFileData(fileData);

        return fileInfo;
    }
}
