package ying.backend_features.websocket_2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by ying on 2017-04-16.
 */
@Component
@EnableWebSocket
public class WebSocketHandlerConfigurer implements WebSocketConfigurer {

    @Autowired
    MyWebSocketHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
//        webSocketHandlerRegistry.addHandler(handler, "/ws2/test").addInterceptors(new HandShake()).setAllowedOrigins("*").withSockJS();
        webSocketHandlerRegistry.addHandler(handler, "/ws2/post/{board_id}/sockjs").addInterceptors(new HandShake()).setAllowedOrigins("*").withSockJS();
    }
}
