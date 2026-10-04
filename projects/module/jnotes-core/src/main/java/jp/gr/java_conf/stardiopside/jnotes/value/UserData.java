package jp.gr.java_conf.stardiopside.jnotes.value;

import java.util.List;

public record UserData(
        Long id,
        String username,
        String rawPassword,
        boolean enabled,
        Integer version,
        List<String> roles) {
}
