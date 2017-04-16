package ying.backend_features.parameter_annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ying.backend_features.parameter_annotation.UserAuthenticationResolver;

import java.util.List;

/**
 * Created by ying on 2017-04-15.
 */
@Configuration
public class AnnotationResolverConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userAuthenticationResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Bean
    public UserAuthenticationResolver userAuthenticationResolver() {
        return new UserAuthenticationResolver();
    }
}
