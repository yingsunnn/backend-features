package ying.backend_features.method_annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ying on 2017-04-15.
 */
@RestController
@RequestMapping(value = "ma")
public class ServicePermissionsNeedController {

    @Autowired
    private ServicePermissionsNeedService service;

    @RequestMapping(value = "test", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String test() {
        service.test();
        return "{\"message\":\"method annotation\"}";
    }
}
