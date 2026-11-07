package jp.gr.java_conf.stardiopside.jnotes.value;

import org.jspecify.annotations.Nullable;

import java.util.List;

public record UserData(
        Long id,
        String username,
        @Nullable String rawPassword,
        boolean enabled,
        Integer version,
        List<String> roles) {
}
