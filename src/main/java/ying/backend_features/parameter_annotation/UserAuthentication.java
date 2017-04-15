package ying.backend_features.parameter_annotation;

import java.lang.annotation.*;

/**
 * Handler UserAuthenticationResolver
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAuthentication {

    public static final String MANDATORY = "MANDATORY";
    public static final String OPTIONAL = "OPTIONAL";

    public String value() default MANDATORY;
}
