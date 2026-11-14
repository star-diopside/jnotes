package jp.gr.java_conf.stardiopside.jnotes.core.message;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.List;
import java.util.Locale;

public record ResultMessage(String code, List<?> args) {

    public ResultMessage(String code, Object... args) {
        this(code, List.of(args));
    }

    public String getMessage(MessageSource messageSource) {
        return messageSource.getMessage(code, args.toArray(), LocaleContextHolder.getLocale());
    }

    public String getMessage(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(code, args.toArray(), locale);
    }

    public String getMessage(MessageSourceAccessor messages) {
        return messages.getMessage(code, args.toArray());
    }

    public String getMessage(MessageSourceAccessor messages, Locale locale) {
        return messages.getMessage(code, args.toArray(), locale);
    }
}
