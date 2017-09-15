package ying.backend_features.method_annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ying on 2017-04-15.
 */
@RestController
@RequestMapping(value = "ma")
public class ServicePermissionsNeedController {

    @Autowired
    private ServicePermissionsNeedService service;

    @ServicePermissionsNeed({"edit_all_posts", "edit_all_relies"})
    @RequestMapping(value = "test", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String test() {
        service.test();
        return "{\"message\":\"method annotation\"}";
    }

    /**
     * {
         "id":123,
         "username":"ying",
         "bio":"sdfsldf af jaslfks"
       }
     * @param userId
     * @param page
     * @param user
     * @return
     */
    @ServicePermissionsNeed({"edit_all_posts", "edit_all_relies"})
    @RequestMapping(value = "test/{user_id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String maTest(@PathVariable("user_id") String userId,
                         @RequestParam(value = "page", required = false) int page,
                         @RequestBody User user) {
        service.test();
        return "{\"message\":\"method annotation\"}";
    }
}
