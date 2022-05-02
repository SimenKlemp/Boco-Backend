package edu.ntnu.idatt2106.boco.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config)
    {
        config.enableSimpleBroker("/chat-outgoing");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        registry
                .addEndpoint("/chat-connect")
                .setAllowedOrigins("http://localhost:8080");

        registry
                .addEndpoint("/chat-connect")
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS();
    }
}