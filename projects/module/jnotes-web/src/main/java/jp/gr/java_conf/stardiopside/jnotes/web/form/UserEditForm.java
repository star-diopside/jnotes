package jp.gr.java_conf.stardiopside.jnotes.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.User;
import jp.gr.java_conf.stardiopside.jnotes.value.UserData;
import jp.gr.java_conf.stardiopside.jnotes.web.validation.constraints.PropertyValuesMatch;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@ToString(exclude = {"password", "confirmPassword"})
@PropertyValuesMatch(propertyNames = {"password", "confirmPassword"}, message = "{validation.unmatchPassword}")
public class UserEditForm implements Serializable {

    @NotNull
    @NotBlank
    private @Nullable String username;

    @Size(min = 8)
    private @Nullable String password;

    private @Nullable String confirmPassword;

    @NotNull
    private @Nullable Boolean enabled;

    @NotNull
    private @Nullable List<UserRole> roles;

    @NotNull
    private @Nullable Long id;

    private @Nullable LocalDateTime createdAt;

    private @Nullable LocalDateTime updatedAt;

    @NotNull
    private @Nullable Integer version;

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
                Objects.requireNonNull(id),
                Objects.requireNonNull(username),
                password,
                Objects.requireNonNull(enabled),
                Objects.requireNonNull(version),
                Objects.requireNonNull(roles).stream().map(Enum::name).toList());
    }
}
