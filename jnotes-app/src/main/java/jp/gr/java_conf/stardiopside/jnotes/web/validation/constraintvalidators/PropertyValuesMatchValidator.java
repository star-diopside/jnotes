package jp.gr.java_conf.stardiopside.jnotes.web.validation.constraintvalidators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.gr.java_conf.stardiopside.jnotes.web.validation.constraints.PropertyValuesMatch;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Gatherers;

public class PropertyValuesMatchValidator implements ConstraintValidator<PropertyValuesMatch, Object> {

    private String[] propertyNames = {};

    @Override
    public void initialize(PropertyValuesMatch constraintAnnotation) {
        this.propertyNames = constraintAnnotation.propertyNames();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);

        if (!Arrays.stream(propertyNames).allMatch(wrapper::isReadableProperty)) {
            return false;
        }

        return Arrays.stream(propertyNames)
                .map(wrapper::getPropertyValue)
                .gather(Gatherers.windowSliding(2))
                .allMatch(x -> x.size() == 1 || Objects.equals(x.get(0), x.get(1)));
    }
}
