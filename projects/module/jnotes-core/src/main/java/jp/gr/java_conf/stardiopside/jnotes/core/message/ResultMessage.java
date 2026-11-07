package jp.gr.java_conf.stardiopside.jnotes.core.message;

import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Locale;

@Data
public class ResultMessage {

    private final String code;
    private final Object[] args;

    public ResultMessage(String code, Object... args) {
        this.code = code;
        this.args = args;
    }

    public String getMessage(MessageSource messageSource) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public String getMessage(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(code, args, locale);
    }

    public String getMessage(MessageSourceAccessor messages) {
        return messages.getMessage(code, args);
    }

    public String getMessage(MessageSourceAccessor messages, Locale locale) {
        return messages.getMessage(code, args, locale);
    }
}
