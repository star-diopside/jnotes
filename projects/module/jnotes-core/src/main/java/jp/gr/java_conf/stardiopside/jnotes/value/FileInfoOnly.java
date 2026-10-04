package jp.gr.java_conf.stardiopside.jnotes.value;

import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

public record FileInfoOnly(
        Long id,
        String fileName,
        @Nullable String contentType,
        Integer length,
        String hashValue,
        LocalDateTime createdAt,
        @Nullable String createdBy,
        LocalDateTime updatedAt,
        @Nullable String updatedBy,
        Integer version
) {
}
