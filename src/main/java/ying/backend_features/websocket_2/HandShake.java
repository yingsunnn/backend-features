package ying.backend_features.websocket_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Created by ying on 2017-04-16.
 */
public class HandShake implements HandshakeInterceptor {

    private static Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest,
                                   ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;

            // UserProfile auth verification.
            logger.debug("HandshakeInterceptor UserProfile Authentication.");
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest,
                               ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler,
                               Exception e) {

    }

}
