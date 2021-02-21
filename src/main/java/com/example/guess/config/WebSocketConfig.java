package com.example.guess.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Frequency;
import lombok.val;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig
    implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker( "/user");
    config.setApplicationDestinationPrefixes("/guess");
    config.setUserDestinationPrefix("/user");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    val upgradeStrategy = new TomcatRequestUpgradeStrategy();
    registry.addEndpoint("/guess")
        .withSockJS();
    registry.addEndpoint("/guess")
        .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
        .setAllowedOrigins("*");
  }

  @Override
  public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
    val resolver = new DefaultContentTypeResolver();
    val converter = new MappingJackson2MessageConverter();
    resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
    converter.setObjectMapper(new ObjectMapper());
    converter.setContentTypeResolver(resolver);
    messageConverters.add(converter);
    return false;
  }

}
