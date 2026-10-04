package jp.gr.java_conf.stardiopside.jnotes.value;

import org.jspecify.annotations.Nullable;

public record DownloadData(
        String fileName,
        @Nullable String contentType,
        byte[] data) {
}
