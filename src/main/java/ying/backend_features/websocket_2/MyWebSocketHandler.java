package ying.backend_features.websocket_2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ying on 2017-04-16.
 */

@Component
public class MyWebSocketHandler implements WebSocketHandler {
    private static Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    public static final CopyOnWriteArrayList<WebSocketSession> webSocketSessions = new CopyOnWriteArrayList<WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        Map<String, Object> attributes = webSocketSession.getAttributes();

        webSocketSessions.add(webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage.getPayloadLength() == 0)
            return;

        ObjectMapper mapper = new ObjectMapper();
        Post post = mapper.readValue(webSocketMessage.getPayload().toString(), Post.class);

        logger.debug("send post: " + post);

        webSocketSession.sendMessage(new TextMessage(mapper.writeValueAsString(post)));
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public static void boardcast(String title) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        for (WebSocketSession webSocketSession : webSocketSessions) {
            if (webSocketSession != null && webSocketSession.isOpen()) {
                Post post = new Post();
                post.setTitle(title);
                webSocketSession.sendMessage(new TextMessage(mapper.writeValueAsString(post)));
            }
        }
    }
}