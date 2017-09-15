package ying.backend_features.parameter_annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

@Component
public class UserAuthenticationResolver implements HandlerMethodArgumentResolver {

    private static Logger logger = LoggerFactory.getLogger(UserAuthenticationResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        boolean userAuthenticationFlag = methodParameter.hasParameterAnnotation(UserAuthentication.class);
//        boolean UserAuthenticationonFlag = methodParameter.hasMethodAnnotation(UserAuthentication.class);
        if (userAuthenticationFlag == false)
            return false;

        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        boolean userAuthenticationCheckFlag = methodParameter.hasMethodAnnotation(PermissionsNeed.class);
        if (userAuthenticationCheckFlag == false) {
            String methodName = methodParameter.getMethod().getName();
            String className = methodParameter.getClass().getName();
            throw new RuntimeException("Class name: " + className    + " method name: " + methodName + " Annotation PermissionsNeed doesn't exists.");
        }

        UserAuthentication userAuthentication = methodParameter.getParameterAnnotation(UserAuthentication.class);
        PermissionsNeed userAuthenticationCheck = methodParameter.getMethodAnnotation(PermissionsNeed.class);

        logger.debug("UserProfile auth : " + userAuthentication.value());
        logger.debug("Permissions need : " + Arrays.asList(userAuthenticationCheck.value()));

        String auth = nativeWebRequest.getHeader("Authentication");

        return auth;
    }
}
