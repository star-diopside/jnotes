package jp.gr.java_conf.stardiopside.jnotes.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jp.gr.java_conf.stardiopside.jnotes.web.validation.constraints.PropertyValuesMatch;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(exclude = {"password", "confirmPassword"})
@PropertyValuesMatch(propertyNames = {"password", "confirmPassword"}, message = "{validation.unmatchPassword}")
public class UserCreateForm implements Serializable {

    @NotBlank
    private String username;

    @NotNull
    @Size(min = 8)
    private String password;

    private String confirmPassword;

    @NotNull
    private Boolean enabled;

    @NotNull
    private List<UserRole> roles;

}
