package ying.backend_features.parameter_annotation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ying on 2017-04-15.
 */
@RestController
@RequestMapping("pa")
public class ParameterAnnotationController {

    @PermissionsNeed({"post_post"})
    @RequestMapping(value = "test", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String test(@UserAuthentication(UserAuthentication.MANDATORY) String auth) {
        return "{\"message\":\"parameter annotation test\"}";
    }
}
