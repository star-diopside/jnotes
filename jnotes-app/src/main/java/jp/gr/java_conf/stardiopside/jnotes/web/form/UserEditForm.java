package jp.gr.java_conf.stardiopside.jnotes.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.User;
import jp.gr.java_conf.stardiopside.jnotes.value.UserData;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@ToString(exclude = {"password", "confirmPassword"})
public class UserEditForm implements Serializable {

    @NotBlank
    private String username;

    @Size(min = 8)
    private String password;

    private String confirmPassword;

    @NotNull
    private Boolean enabled;

    @NotNull
    private List<UserRole> roles;

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer version;

    public UserEditForm(User user) {
        id = user.getId();
        username = user.getUsername();
        enabled = user.isEnabled();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
        version = user.getVersion();
        var pattern = Pattern.compile("^ROLE_(.*)$");
        roles = user.getAuthorities().stream()
                .map(a -> pattern.matcher(a.getAuthority()).replaceFirst("$1"))
                .map(UserRole::valueOf)
                .toList();
    }

    public UserData toUserData() {
        return new UserData(
                id,
                username,
                password,
                enabled,
                version,
                roles.stream().map(Enum::name).toList());
    }
}
