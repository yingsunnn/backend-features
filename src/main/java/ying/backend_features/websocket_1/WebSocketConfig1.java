package ying.backend_features.websocket_1;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig1 extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        // @SendTo path的前缀 @SendTo("/ws_send_to/chat/{room_id}")
        // 页面订阅路径的前缀 stompClient.subscribe('/ws_send_to/chat/' + roomId ...
        messageBrokerRegistry.enableSimpleBroker("/ws_send_to");
//        messageBrokerRegistry.setApplicationDestinationPrefixes("/wschat");
//        messageBrokerRegistry.setUserDestinationPrefix("/userTest");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        // Controller 中的 @MessageMapping("/ws1/chat/{room_id}")
        stompEndpointRegistry.addEndpoint("/ws1/chat/{room_id}").setAllowedOrigins("*").withSockJS();

    }
}
