package ying.backend_features.websocket_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ying.backend_features.redis_options.RedisOptionsController;

/**
 * Created by ying on 2017-04-16.
 */
@RestController
public class WebSocketController1 {
    private static Logger logger = LoggerFactory.getLogger(RedisOptionsController.class);

    /**
     * Websocket example
     */
    @MessageMapping("/ws1/chat/{room_id}")
    @SendTo("/ws_send_to/chat/{room_id}") // 页面订阅的路径 stompClient.subscribe('/ws_send_to/chat/' + roomId ...
    public DirectMessage reply(DirectMessage directMessage, @PathVariable("room_id") String roomId) throws Exception {
        logger.debug("room id: " + roomId + " directMessage: " + directMessage);
        return directMessage;
    }

}
