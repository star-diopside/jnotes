package jp.gr.java_conf.stardiopside.jnotes.data.domain;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityContextAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication == null || !authentication.isAuthenticated())
                ? Optional.empty() : Optional.of(authentication.getName());
    }
}
