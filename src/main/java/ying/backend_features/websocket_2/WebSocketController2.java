package ying.backend_features.websocket_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ying.backend_features.redis_options.RedisOptionsController;

import java.io.IOException;

/**
 * Created by ying on 2017-04-16.
 */
@RestController
public class WebSocketController2 {
    private static Logger logger = LoggerFactory.getLogger(RedisOptionsController.class);

    @RequestMapping(value = "ws2/boardcast/{title}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String websocketBoardcast (@PathVariable String title) throws IOException {
        MyWebSocketHandler.boardcast(title);

        return "{\"message\":\"success\"}";
    }
}
