package jp.gr.java_conf.stardiopside.jnotes.value;

import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

public record FileInfoData(
        Long id,
        String fileName,
        @Nullable String contentType,
        Integer length,
        String hashValue,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version,
        Long fileDataId,
        LocalDateTime fileDataUpdatedAt,
        Integer fileDataVersion) {
}
