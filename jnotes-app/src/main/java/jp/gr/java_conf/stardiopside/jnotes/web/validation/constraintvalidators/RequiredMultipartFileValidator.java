package jp.gr.java_conf.stardiopside.jnotes.web.validation.constraintvalidators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.gr.java_conf.stardiopside.jnotes.web.validation.constraints.RequiredMultipartFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public class RequiredMultipartFileValidator implements ConstraintValidator<RequiredMultipartFile, MultipartFile> {

    @Override
    public boolean isValid(@Nullable MultipartFile value, ConstraintValidatorContext context) {
        return value == null || StringUtils.isNotEmpty(value.getOriginalFilename());
    }
}
