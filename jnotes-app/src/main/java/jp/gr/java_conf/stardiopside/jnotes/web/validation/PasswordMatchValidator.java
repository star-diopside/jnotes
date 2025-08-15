package jp.gr.java_conf.stardiopside.jnotes.web.validation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class PasswordMatchValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BeanUtils.getPropertyDescriptor(clazz, "password") != null &&
                BeanUtils.getPropertyDescriptor(clazz, "confirmPassword") != null;
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        BeanWrapper wrapper = new BeanWrapperImpl(target);
        Object password = wrapper.getPropertyValue("password");
        Object confirmPassword = wrapper.getPropertyValue("confirmPassword");
        if (!Objects.equals(password, confirmPassword)) {
            errors.reject("PasswordMatchValidator.unmatchPassword");
        }
    }
}
