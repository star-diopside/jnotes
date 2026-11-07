package jp.gr.java_conf.stardiopside.jnotes.web.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jp.gr.java_conf.stardiopside.jnotes.value.FileInfoData;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FileEditForm {

    private @Nullable MultipartFile file;

    @NotNull
    private @Nullable Long id;

    private @Nullable String fileName;

    @Size(max = 255)
    private @Nullable String updateFileName;

    private @Nullable MediaType contentType;

    private @Nullable Integer length;

    private @Nullable String hashValue;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private @Nullable LocalDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private @Nullable LocalDateTime updatedAt;

    @NotNull
    private @Nullable Integer fileInfoVersion;

    @NotNull
    private @Nullable Integer fileDataVersion;

    public FileEditForm(Long id) {
        this.id = id;
    }

    public FileEditForm(FileInfoData file) {
        id = file.id();
        fileName = file.fileName();
        contentType = file.contentType() == null ? null : MediaType.parseMediaType(file.contentType());
        length = file.length();
        hashValue = file.hashValue();
        createdAt = file.createdAt();
        updatedAt = ObjectUtils.max(file.updatedAt(), file.fileDataUpdatedAt());
        fileInfoVersion = file.version();
        fileDataVersion = file.fileDataVersion();
    }
}
