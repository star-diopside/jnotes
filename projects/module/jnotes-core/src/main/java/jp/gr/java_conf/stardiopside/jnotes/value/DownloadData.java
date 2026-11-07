package jp.gr.java_conf.stardiopside.jnotes.value;

import lombok.Value;
import org.jspecify.annotations.Nullable;

@Value
public class DownloadData {
    String fileName;
    @Nullable String contentType;
    byte[] data;
}
