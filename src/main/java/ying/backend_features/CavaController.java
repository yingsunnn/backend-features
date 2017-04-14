package ying.backend_features;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ying on 2017-04-14.
 */
@RestController
@RequestMapping("test")
public class CavaController {

    @RequestMapping(value = "cava", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String cava() {
        return "{\"message\":\"Ça va bien\"}";
    }
}
