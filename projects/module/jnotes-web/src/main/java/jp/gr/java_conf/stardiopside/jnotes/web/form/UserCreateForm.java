package jp.gr.java_conf.stardiopside.jnotes.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jp.gr.java_conf.stardiopside.jnotes.web.validation.constraints.PropertyValuesMatch;
import lombok.Data;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(exclude = {"password", "confirmPassword"})
@PropertyValuesMatch(propertyNames = {"password", "confirmPassword"}, message = "{validation.unmatchPassword}")
public class UserCreateForm implements Serializable {

    @NotNull
    @NotBlank
    private @Nullable String username;

    @NotNull
    @Size(min = 8)
    private @Nullable String password;

    private @Nullable String confirmPassword;

    @NotNull
    private @Nullable Boolean enabled;

    @NotNull
    private @Nullable List<UserRole> roles;

}
