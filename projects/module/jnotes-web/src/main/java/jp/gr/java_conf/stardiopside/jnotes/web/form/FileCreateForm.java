package jp.gr.java_conf.stardiopside.jnotes.web.form;

import jakarta.validation.constraints.NotNull;
import jp.gr.java_conf.stardiopside.jnotes.web.validation.constraints.RequiredMultipartFile;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileCreateForm {

    @NotNull
    @RequiredMultipartFile
    private @Nullable MultipartFile file;

}
