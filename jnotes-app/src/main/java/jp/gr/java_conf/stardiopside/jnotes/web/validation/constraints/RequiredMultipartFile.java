package jp.gr.java_conf.stardiopside.jnotes.web.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.gr.java_conf.stardiopside.jnotes.web.validation.constraintvalidators.RequiredMultipartFileValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Repeatable(RequiredMultipartFile.List.class)
@Constraint(validatedBy = {RequiredMultipartFileValidator.class})
public @interface RequiredMultipartFile {

    String message() default "{jp.gr.java_conf.stardiopside.jnotes.web.validation.constraints.RequiredMultipartFile.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({
            ElementType.METHOD,
            ElementType.FIELD,
            ElementType.ANNOTATION_TYPE,
            ElementType.CONSTRUCTOR,
            ElementType.PARAMETER,
            ElementType.TYPE_USE
    })
    @interface List {
        RequiredMultipartFile[] value();
    }
}
